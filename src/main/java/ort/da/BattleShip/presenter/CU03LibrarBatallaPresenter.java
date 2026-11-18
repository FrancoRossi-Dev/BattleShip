package ort.da.BattleShip.presenter;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.da.BattleShip.dto.CeldasDto;
import ort.da.BattleShip.dto.InicializarTableroDto;
import ort.da.BattleShip.dto.RondaDto;
import ort.da.BattleShip.model.Barco;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.BattleException;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.model.Ronda;
import ort.da.BattleShip.observador.ConexionNavegador;
import ort.da.BattleShip.observador.Observable;
import ort.da.BattleShip.observador.Observador;

import org.springframework.web.bind.annotation.PostMapping;


// CU03: LIBRAR BATALLA
// Se juega por rondas: el jugador dispara a aguas enemigas y la máquina responde enseguida (ronda atómica),
// hasta que una flota queda hundida. También sirve para ver batallas ya ganadas o perdidas.
@RestController
@RequestMapping("/librarBatalla")
@Scope("session")
public class CU03LibrarBatallaPresenter implements Observador {
    private Facade f;
    private Batalla batallaActual;

    private ConexionNavegador conexionNavegador;

    public CU03LibrarBatallaPresenter (Facade f, ConexionNavegador conexion) {
        this.f = f;
        this.conexionNavegador = conexion;
    }

    // ===================== OBSERVADOR (SSE) =====================

    // abre el canal SSE por el que el servidor le avisa cambios a la vista
    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    // cada ronda jugada (en esta u otra sesión) se reenvía por SSE;
    // cuando la batalla termina se muestra el nuevo estado y, si se perdió, los barcos enemigos
    @Override
    public void actualizar(Observable origen, Object evento) {
        if (evento.equals(Batalla.Eventos.RONDA_FINALIZADA)) {
            Ronda r = ((Batalla) origen).getUltimaRonda();
            conexionNavegador.enviarJSON(Commands.create(rondaJugada(r), bajasEnemigas(), numeroRonda()));
        }
        // CAMBIO_ESTADO llega después de RONDA_FINALIZADA (ver EstadoEnCombate.jugarRonda).
        // tableroEnemigo() ya decide si revelar los barcos (solo si se perdió)
        if (evento.equals(Batalla.Eventos.CAMBIO_ESTADO)) {
            conexionNavegador.enviarJSON(Commands.create(estadoBatalla(), numeroRonda(), tableroEnemigo()));
        }
    }

    // ===================== ENDPOINTS =====================

    // la vista se abrió: carga la batalla indicada y muestra lugar, estado, ronda, los dos océanos
    // con los disparos hechos y las bajas enemigas. Sin idBatalla se vuelve a la sala
    @PostMapping("/vistaConectada")
    public Commands vistaConectada(@RequestParam(required = false) Integer idBatalla) throws BattleException {
        if (idBatalla == null) return Commands.create(CommonCommands.redirigir("index.html"));
        batallaActual = f.getBatalla(idBatalla);
        batallaActual.agregarObservador(this);

        // también se entra a ver batallas ganadas o perdidas: el tablero queda habilitado igual y
        // un disparo en una batalla finalizada lo rechaza el modelo ("La batalla ya está finalizada")
        return Commands.create(lugarBatalla(), estadoBatalla(), numeroRonda(), inicializarTablero(),
                tableroPropio(), tableroEnemigo(), bajasEnemigas());
    }

    // clic en aguas enemigas: se juega una ronda (disparo del jugador y respuesta de la máquina).
    // Si la celda ya fue disparada o la batalla terminó, el modelo avisa el error.
    // Devuelve los dos disparos, las bajas y la ronda; el fin de la batalla llega por SSE
    @PostMapping("/jugarRonda")
    public Commands jugarRonda(@RequestParam int coordenadaVertical, @RequestParam int coordenadaHorizontal) throws BattleException {
        // nombres que envía BattleShipBoard al hacer clic: vertical = fila (Y), horizontal = columna (X)
        Ronda r = batallaActual.jugarRonda(coordenadaHorizontal, coordenadaVertical);
        return Commands.create(rondaJugada(r), bajasEnemigas(), numeroRonda());
    }

    // clic en aguas propias: no se permite, solo muestra el error
    @PostMapping("/aguasPropias")
    public Commands aguasPropias() throws BattleException {
        throw new BattleException("No puede disparar a sus propias aguas");
    }

    // botón "Volver a la sala de operaciones": la batalla queda pausada tal como está
    @PostMapping("/volverASala")
    public Commands volverASala() {
        return Commands.create(CommonCommands.redirigir("index.html"));
    }

    // la vista se cerró: deja de escuchar a la batalla
    @PostMapping("/vistaDesconectada")
    public void vistaDesconectada() {
        if (batallaActual != null) {
            batallaActual.quitarObservador(this);
        }
    }

    // ===================== COMMANDS =====================

    // como en Agenda (nombreUsuario, cantidadContactos), cada dato simple va en su propio comando
    private Command lugarBatalla() {
        return new Command("lugarBatalla", batallaActual.getLugar());
    }

    private Command estadoBatalla() {
        return new Command("estadoBatalla", batallaActual.nombreEstado());
    }

    private Command numeroRonda() {
        return new Command("numeroRonda", batallaActual.numeroRonda());
    }

    // aguas enemigas -> jugarRonda; aguas propias -> error "No puede disparar a sus propias aguas"
    private Command inicializarTablero() {
        return new Command("inicializar", new InicializarTableroDto(batallaActual,
                "/librarBatalla/aguasPropias", "/librarBatalla/jugarRonda"));
    }

    private Command tableroPropio() {
        return new Command("tableroPropio", new CeldasDto(batallaActual.celdasPropias(), true));
    }

    // si la batalla se perdió se revelan los barcos enemigos no descubiertos
    private Command tableroEnemigo() {
        return new Command("tableroEnemigo",
                new CeldasDto(batallaActual.celdasEnemigas(), batallaActual.revelaBarcosEnemigos()));
    }

    // formato pedido por la letra: "{4} {1} {2}", en el orden en que se hundieron.
    // vacío -> BattleShipBoard muestra "Ninguna"
    private Command bajasEnemigas() {
        String bajas = "";
        for (Barco b : batallaActual.bajasEnemigas()) {
            if (!bajas.isEmpty()) bajas += " ";
            bajas += "{" + b.getTamano() + "}";
        }
        return new Command("bajasEnemigas", bajas);
    }

    // compartido entre la respuesta de jugarRonda y el observador; los dos disparos van juntos (ronda atómica)
    private Command rondaJugada(Ronda r) {
        return new Command("rondaJugada", new RondaDto(r));
    }

}
