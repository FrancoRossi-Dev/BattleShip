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

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    /*
     * 1. Indica que desea iniciar una nueva batalla
     * 2. Solicita completar los siguientes datos:
     * - Lugar del combate:
     * - Nombre
     * - Táctica de juego (despliega las tácticas definidas)
     * - Océano:
     * - Cantidad de celdas horizontales
     * - Cantidad de celdas verticales
     * - Flota:
     * Se muestran los tipos de barcos definidos y se permite ingresar una cantidad
     * para cada uno de ellos. Mínimo 0, máximo 6.
     * - Muestra el botón “Continuar”
     * 
     */

    @PostMapping("/vistaConectada")
    public Commands vistaConectada(@RequestParam(required = false) Integer idBatalla) throws BattleException {
        // se cargan los desplegables para generar una nueva vista
        listaDeTacticas = f.getTacticas();
        listaDeTiposDeBarco = f.getTiposBarco();

        // si el usuario no ingresa una id, se crea una nueva
        if (idBatalla == null) {
            batalla = null;
            return Commands.create(listaTacticas(), listaTiposBarco(), habilitarFormulario(true));
        }
        // si ingresa una id al conectarse a este endpoint, se retoma el proceso de creción
        batalla = f.getBatalla(idBatalla);
        batalla.agregarObservador(this);

        Commands comandos = Commands.create(habilitarFormulario(false), lugarBatalla(), inicializarTablero(),
                mostrarTableroPropio(), barcosPendientes());
        if (batalla.flotaDesplegada()) comandos.add(confirmarBatallaLista());
        return comandos;
    }
    /*
     * 3. Ingresa los datos requeridos e indica la opción Continuar.
     * 4. La batalla es incorporada a la lista en la sala de operaciones con el
     * estado
     * “En preparación”.
     */

    /*
     * Flujos alternativos
     * 3. Si el nombre del lugar de combate no es proporcionado, el sistema asignara
     * un texto aleatorio de 6 caracteres alfabéticos
     * en mayúsculas.
     * 3. Si el nombre del lugar de combate ya existe se muestra el mensaje “No se
     * puede librar más de una batalla en el mismo
     * lugar, cambie de nombre”.
     * 3. Si alguna cantidad de celdas del océano es menor a 6 o mayor a 12, se
     * indica un error específico.
     * 3. Si se ingresan más de 6 unidades de un tipo de embarcación, se indica un
     * error específico.
     * 3. Si todos los tipos de naves tienen 0 unidades, se indica un error
     * específico.
     * 3. Si el tamaño de la flota no entra en el océano, se indica un error
     * específico. Para eso, el sistema debe validar que la
     * densidad de ocupación de un océano sea menor al 25%, es decir, los barcos
     * colocados ocupan menos de la cuarta parte
     * de las celdas disponibles.
     * (cantidad de celdas de la flota / cantidad de celdas del océano) < 0.25
     */

    /*
     * Muestra:
     * - Los océanos con el título “Desplegando la Flota”, lo cual permite al
     * usuario
     * ubicar sus barcos en su océano de aguas propias.
     * - El lugar del combate.
     * - La lista de barcos disponibles ordenados por longitud, de mayor a menor. Si
     * en la configuración de la flota se ingresó una cantidad mayor a 1 en un tipo
     * de barco, se muestra el nombre del barco tantas veces como esa cantidad.
     * El primer barco de la lista debe estar siempre seleccionado.
     * - La opción de elegir horizontal o vertical. Horizontal debe estar
     * seleccionado
     * por defecto.
     * - Mientras haya un elemento en la lista de barcos disponibles, se muestra la
     * leyenda de acción “Posicione su [Tipo de barco] en forma [Orientación]”. Por
     * ejemplo “Posicione su destructor en forma vertical”.
     * - Un botón “Librar batalla”.
     * - Un botón “Volver a la sala de operaciones”.
     */

    @PostMapping("/crearBatalla")
    public Commands crearBatalla(
            @RequestParam(defaultValue = "") String lugar,
            @RequestParam int posTactica,
            @RequestParam int ancho,
            @RequestParam int alto,
            // una cantidad por tipo de barco, en el orden de listaTiposBarco ("1,0,2,...")
            @RequestParam int[] cantidades) throws BattleException {

        // crea batalla a partir de los argumentos
        Batalla nueva = new Batalla(lugar, tacticaEn(posTactica), ancho, alto,
                listaDeTiposDeBarco, cantidades);

        // envía a la fachada para agregarla a la lista
        f.agregar(nueva);

        // persistencia
        batalla = nueva;
        batalla.agregarObservador(this);

        return Commands.create(habilitarFormulario(false), lugarBatalla(), inicializarTablero(),
                barcosPendientes());
    }

    /*
     * 5. Selecciona (haciendo click) una coordenada en el océano de aguas propias.
     * 6.
     * - El barco seleccionado es ubicado en el océano con la posición indicada. La
     * celda seleccionada corresponde a la proa del barco, es decir, el extremo
     * izquierdo (si el barco se coloca horizontalmente) o el extremo inferior (si el
     * barco se acomoda verticalmente).
     * - El barco es eliminado de la lista de barcos disponibles y el siguiente
     * elemento es seleccionado. La leyenda de acción se actualiza
     * adecuadamente. (Si no quedan barcos disponibles para ubicar, la leyenda de
     * acción muestra “Batalla preparada”
     * Los pasos 5 y 6 continúan hasta que todos los barcos están posicionados.
     *
     * Flujos alternativos
     * 5. Si el barco no puede ser ubicado en el océano, se indica un error y se ignora el intento. Un barco no puede ser ubicado
     * en el océano de aguas propias, si se da alguna de las siguientes condiciones:
     * - Parte del barco cae fuera de los límites del océano.
     * - Parte del barco colisiona (se superpone) con un barco ya ubicado.
     * - Parte del barco toca al menos una celda con un barco ya ubicado. (Siempre debe haber una celda con agua
     * entre dos barcos).
     * 5. Si el usuario interactúa con el océano de aguas enemigas durante este caso de uso, se muestra el mensaje “No puede
     * ubicar barcos en aguas enemigas”
     */

    @PostMapping("/desplegarFlota")
    public Commands desplegarFlota(
            // nombres que envía BattleShipBoard al hacer clic: vertical = fila (Y), horizontal = columna (X)
            @RequestParam int coordenadaVertical,
            @RequestParam int coordenadaHorizontal,
            @RequestParam String orientacion) throws BattleException {

        if (batalla == null) return Commands.create(CommonCommands.redirigir("index.html"));

        // el barco ubicado llega a la vista solo por SSE, desde actualizar() (evento BARCO_DESPLEGADO).
        // preguntar si es necesario devolver tambien aca
        // siempre se ubica el primer barco pendiente (el seleccionado en la vista)
        batalla.desplegarBarco(coordenadaHorizontal, coordenadaVertical, orientacion);

        // la leyenda de acción la arma el cliente; acá solo se avisa cuando no quedan barcos por ubicar
        // con la flota completa el tablero sigue habilitado: un nuevo clic lo rechaza el modelo
        // ("Todos los barcos ya fueron posicionados")
        if (batalla.flotaDesplegada()) {
            return Commands.create(confirmarBatallaLista());
        }

        return Commands.create();
    }




    // verificar si es necesario
    @PostMapping("/aguasEnemigas")
    public Commands aguasEnemigas() throws BattleException {
        throw new BattleException("No puede ubicar barcos en aguas enemigas");
    }

    /*
     * 7. Opcionalmente selecciona “Volver a la sala de operaciones”
     * 8. Ejecuta el CU01
     */

    @PostMapping("/volverASala")
    public Commands volverASala() {
        return Commands.create(CommonCommands.redirigir("index.html"));
    }

    /*
     * 8. Opcionalmente selecciona “Librar batalla”
     * 9. Ejecuta el CU03
     *
     * Flujos alternativos
     * 8. Si el usuario intenta librar batalla antes de posicionar todos sus barcos, se muestra el mensaje “Debe posicionar todos
     * sus barcos primero”.
     */

    @PostMapping("/librarBatalla")
    public Commands librarBatalla() throws BattleException {
        if (batalla == null)
            throw new BattleException("No hay una batalla en preparación");

        batalla.librarBatalla();
        return Commands.create(CommonCommands.redirigir("librarBatalla.html?idBatalla=" + f.idDe(batalla)));
    }

    @PostMapping("/vistaDesconectada")
    public void vistaDesconectada() {
        if (batalla != null) {
            batalla.quitarObservador(this);
        }
    }

    // funcion helper para coordinar posicion de tacticas
    private TacticaTipo tacticaEn(int pos) throws BattleException {
        if (pos < 0 || pos >= listaDeTacticas.size())
            throw new BattleException("Táctica desconocida");
        return listaDeTacticas.get(pos);
    }

    /*
     * CU02: PREPARAR UNA NUEVA BATALLA
     * Este caso de uso cuenta con dos pasos. En el primero se introducen y validan datos básicos para “Preparar una nueva
     * batalla”. Si el usuario no logra superar las validaciones, si abandona la vista o si selecciona “Volver a la sala de
     * operaciones” la información se pierde.
     *
     * Una vez que se avanza al segundo paso (“Desplegando la flota”), se conserva la situación actual de la nueva batalla. De
     * este modo, si el usuario abandona la vista o selecciona “Volver a la sala de operaciones”, puede volver a “Entrar” y
     * continuar con la configuración desde el punto en que la dejó.
     *
     * Postcondiciones
     *
     * El océano propio es establecido para la batalla con la disposición de la flota designado por el usuario.
     * El océano del enemigo es establecido con una distribución aleatoria, pero con las mismas reglas.
     */

    // COMMANDS

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

    // tras cada clic: solo las celdas del barco recién ubicado
    private Command mostrarBarcoUbicado(Barco b) {
        return new Command("barcoUbicado", new CeldasDto(b.getCeldas(), true));
    }

    // el diálogo solo necesita el lugar; como en Agenda, un dato único va directo sin DTO.
    // la vista pregunta y llama a /librarBatalla o /volverASala
    private Command confirmarBatallaLista() {
        return new Command("confirmarBatallaLista", batalla.getLugar());
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        if(evento.equals(Batalla.Eventos.BARCO_DESPLEGADO)) {
            Barco b = ((Batalla) origen).getUltimoBarcoColocado();
            conexionNavegador.enviarJSON(Commands.create(mostrarBarcoUbicado(b), barcosPendientes()));
        }
    }
}

