package ort.da.BattleShip.presenter;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.da.BattleShip.dto.CeldasDto;
import ort.da.BattleShip.dto.InicializarTableroDto;
import ort.da.BattleShip.dto.TacticaDto;
import ort.da.BattleShip.dto.TipoBarcoDto;
import ort.da.BattleShip.model.Barco;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.BattleException;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.model.TacticaTipo;
import ort.da.BattleShip.model.TipoBarco;
import ort.da.BattleShip.observador.ConexionNavegador;
import ort.da.BattleShip.observador.Observable;
import ort.da.BattleShip.observador.Observador;

import java.util.ArrayList;
import java.util.List;

// CU02: PREPARAR UNA NUEVA BATALLA
// Paso 1: se completa el formulario (lugar, táctica, océano y flota) y se crea la batalla "En preparación".
// Paso 2: se ubican los barcos uno por uno en aguas propias; se puede salir y retomar desde la sala.
@RestController
@RequestMapping("/nuevaBatalla")
@Scope("session")
public class CU02PrepararNuevaBatallaPresenter implements Observador {

    private Facade f;
    private List<TacticaTipo> listaDeTacticas = new ArrayList<>();
    private List<TipoBarco> listaDeTiposDeBarco = new ArrayList<>();
    private Batalla batalla;

    private ConexionNavegador conexionNavegador;

    public CU02PrepararNuevaBatallaPresenter(Facade facade, ConexionNavegador conexion) {
        this.f = facade;
        this.conexionNavegador = conexion;
    }

    // ===================== OBSERVADOR (SSE) =====================

    // abre el canal SSE por el que el servidor le avisa cambios a la vista
    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    // cada barco ubicado se reenvía por SSE; si la batalla pasa a combate
    // (desde esta u otra sesión) la vista se manda a CU03
    @Override
    public void actualizar(Observable origen, Object evento) {
        if(evento.equals(Batalla.Eventos.BARCO_DESPLEGADO)) {
            Barco b = ((Batalla) origen).getUltimoBarcoColocado();
            conexionNavegador.enviarJSON(Commands.create(mostrarBarcoUbicado(b), barcosPendientes()));
        }
        if (evento.equals(Batalla.Eventos.CAMBIO_ESTADO)) {
            try {
                conexionNavegador.enviarJSON(Commands.create(CommonCommands.redirigir("librarBatalla.html?idBatalla=" + f.idDe(batalla))));
            } catch (BattleException e) {
                System.out.println("Error al pasar a librar batalla: " + e.getMessage());
            }
        }
    }

    // ===================== ENDPOINTS =====================

    // la vista se abrió. Sin idBatalla: batalla nueva, se muestra el formulario con tácticas y tipos de barco.
    // Con idBatalla: se retoma el despliegue donde quedó (tablero, barcos ya ubicados y pendientes);
    // si otra sesión ya la empezó, se manda directo a librar batalla (CU03)
    @PostMapping("/vistaConectada")
    public Commands vistaConectada(@RequestParam(required = false) Integer idBatalla) throws BattleException {
        listaDeTacticas = f.getTacticas();
        listaDeTiposDeBarco = f.getTiposBarco();

        if (idBatalla == null) {
            batalla = null;
            return Commands.create(listaTacticas(), listaTiposBarco(), habilitarFormulario(true));
        }

        batalla = f.getBatalla(idBatalla);
        if (!batalla.enPreparacion())
            return Commands.create(
                CommonCommands.redirigir("librarBatalla.html?idBatalla=" + f.idDe(batalla)));

        batalla.agregarObservador(this);

        // si la flota ya estaba desplegada, barcosPendientes llega vacío y la vista abre el diálogo
        return Commands.create(
            habilitarFormulario(false),
            lugarBatalla(),
            inicializarTablero(),
            mostrarTableroPropio(),
            barcosPendientes()
        );
    }

    // botón "Continuar": crea la batalla con los datos del formulario (el modelo valida y avisa errores),
    // la agrega a la sala de operaciones y pasa al paso de desplegar la flota
    @PostMapping("/crearBatalla")
    public Commands crearBatalla(
            @RequestParam(defaultValue = "") String lugar,
            @RequestParam(defaultValue = "") String posTactica,
            @RequestParam(defaultValue = "") String ancho,
            @RequestParam(defaultValue = "") String alto,
            @RequestParam(defaultValue = "") String cantidades) throws BattleException {

        Batalla nueva = new Batalla(
            lugar,
            tacticaEn(numero(posTactica, "la táctica")),
            numero(ancho, "las celdas horizontales"),
            numero(alto, "las celdas verticales"),
            listaDeTiposDeBarco,
            cantidadesPorTipo(cantidades));

        f.agregar(nueva);

        batalla = nueva;
        batalla.agregarObservador(this);

        return Commands.create(
            habilitarFormulario(false),
            lugarBatalla(),
            inicializarTablero(),
            barcosPendientes()
        );
    }

    // clic en aguas propias: ubica el primer barco pendiente en esa celda con la orientación elegida.
    // Si no entra o toca otro barco, el modelo avisa el error. Devuelve el barco ubicado y los que faltan
    @PostMapping("/desplegarFlota")
    public Commands desplegarFlota(
            // nombres que envía BattleShipBoard al hacer clic: vertical = fila (Y), horizontal = columna (X)
            @RequestParam int coordenadaVertical,
            @RequestParam int coordenadaHorizontal,
            @RequestParam String orientacion) throws BattleException {

        if (batalla == null) return
            Commands.create(CommonCommands.redirigir("index.html"));

        batalla.desplegarBarco(coordenadaHorizontal, coordenadaVertical, orientacion);

        return Commands.create(
            mostrarBarcoUbicado(batalla.getUltimoBarcoColocado()),
            barcosPendientes()
        );
    }

    // clic en aguas enemigas: no se permite, solo muestra el error
    @PostMapping("/aguasEnemigas")
    public Commands aguasEnemigas() throws BattleException {
        throw new BattleException("No puede ubicar barcos en aguas enemigas");
    }

    // botón "Volver a la sala de operaciones": la batalla queda guardada como está
    @PostMapping("/volverASala")
    public Commands volverASala() {
        return Commands.create(CommonCommands.redirigir("index.html"));
    }

    // botón "Librar batalla": inicia el combate y pasa a CU03.
    // Si faltan barcos por ubicar el modelo lanza el error y no se redirige
    @PostMapping("/librarBatalla")
    public Commands librarBatalla() throws BattleException {
        if (batalla == null)
            throw new BattleException("No hay una batalla en preparación");

        batalla.librarBatalla();
        return Commands.create(CommonCommands.redirigir("librarBatalla.html?idBatalla=" + f.idDe(batalla)));
    }

    // la vista se cerró: deja de escuchar a la batalla
    @PostMapping("/vistaDesconectada")
    public void vistaDesconectada() {
        if (batalla != null) {
            batalla.quitarObservador(this);
        }
    }

    // ===================== COMMANDS =====================

    // la vista usa la posición de cada uno como valor del formulario
    private Command listaTacticas() {
        return new Command("listaTacticas", TacticaDto.fromList(listaDeTacticas));
    }

    private Command listaTiposBarco() {
        return new Command("listaTiposBarco", TipoBarcoDto.fromTipos(listaDeTiposDeBarco));
    }

    private Command habilitarFormulario(boolean h) {
        return new Command("habilitarFormulario", h);
    }

    private Command lugarBatalla() {
        return new Command("lugarBatalla", batalla.getLugar());
    }

    // aguas propias -> desplegarFlota; aguas enemigas -> error "No puede ubicar barcos en aguas enemigas"
    private Command inicializarTablero() {
        return new Command("inicializar", new InicializarTableroDto(batalla,
                "/nuevaBatalla/desplegarFlota", "/nuevaBatalla/aguasEnemigas"));
    }

    // tipos de los barcos que faltan ubicar, de mayor a menor (repetidos si hay más de uno del tipo);
    // la vista marca el primero como seleccionado, arma la leyenda de acción y usa el tamaño
    // para la vista previa del barco sobre el océano. Mismos datos que listaTiposBarco
    private Command barcosPendientes() {
        return new Command("barcosPendientes", TipoBarcoDto.fromBarcos(batalla.barcosPendientes()));
    }

    // al retomar una batalla: océano propio completo, la vista lo recorre celda por celda
    private Command mostrarTableroPropio() {
        return new Command("tableroPropio", new CeldasDto(batalla.celdasPropias(), true));
    }

    // tras cada clic: solo las celdas del barco recién ubicado.
    // compartido entre la respuesta de desplegarFlota y el observador
    private Command mostrarBarcoUbicado(Barco b) {
        return new Command("barcoUbicado", new CeldasDto(b.getCeldas(), true));
    }

    // ===================== UTILIDADES =====================

    // pasa el texto del formulario a número; si no lo es, avisa en qué campo está el error
    private int numero(String texto, String campo) throws BattleException {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new BattleException("Debe ingresar un número entero en " + campo);
        }
    }

    // convierte el texto "1,0,2,..." del formulario en una cantidad por tipo de barco,
    // en el mismo orden que listaDeTiposDeBarco; informa si falta alguna o no es un número
    private int[] cantidadesPorTipo(String texto) throws BattleException {
        String[] partes = texto.split(",", -1);
        if (partes.length != listaDeTiposDeBarco.size())
            throw new BattleException("Debe ingresar una cantidad para cada tipo de barco");
        int[] cantidades = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            cantidades[i] = numero(partes[i], "la cantidad de " + listaDeTiposDeBarco.get(i).getNombre());
        }
        return cantidades;
    }

    // la vista manda la posición de la táctica en el desplegable; acá se busca cuál es
    private TacticaTipo tacticaEn(int pos) throws BattleException {
        if (pos < 0 || pos >= listaDeTacticas.size())
            throw new BattleException("Táctica desconocida");
        return listaDeTacticas.get(pos);
    }
}
