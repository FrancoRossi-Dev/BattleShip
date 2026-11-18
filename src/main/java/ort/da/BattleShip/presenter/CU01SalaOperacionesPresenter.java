package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.da.BattleShip.dto.TotalBatallasDto;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.BattleException;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.observador.ConexionNavegador;
import ort.da.BattleShip.observador.Observable;
import ort.da.BattleShip.observador.Observador;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

// CU01: SALA DE OPERACIONES
// Vista principal: muestra todas las batallas con sus totales y permite crear una nueva o entrar a una.
// Puede estar abierta en varias sesiones a la vez y todas ven lo mismo (se actualiza por SSE).
@RestController
@RequestMapping("/salaOperaciones")
@Scope("session")
public class CU01SalaOperacionesPresenter implements Observador {

    private Facade f;

    private ConexionNavegador conexionNavegador;

    public CU01SalaOperacionesPresenter (Facade facade, ConexionNavegador conexion) {
        this.f = facade;
        this.conexionNavegador = conexion;
    }

    // ===================== OBSERVADOR (SSE) =====================

    // abre el canal SSE por el que el servidor le avisa cambios a la vista
    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE()
    {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    // cuando cambia alguna batalla (en cualquier sesión) se reenvía la lista actualizada por SSE
    @Override
    public void actualizar(Observable origen, Object evento) {
        if (Facade.Eventos.ACTUALIZACION_DE_LISTA.equals(evento)) {
            conexionNavegador.enviarJSON(Commands.create(listaBatallas()));
        }
    }

    // ===================== ENDPOINTS =====================

    // la vista se abrió: se suscribe a la fachada para enterarse de cambios
    // y devuelve la lista de batallas con los totales de ganadas, perdidas y % de éxito
    @PostMapping("/vistaConectada")
    public Commands vistaConectada() {
        f.agregarObservador(this);
        return Commands.create(listaBatallas());
    }

    // botón "Iniciar nueva batalla": manda a la vista de CU02 sin batalla elegida
    @PostMapping("/nuevaBatalla")
    public Commands nuevaBatalla() {
        return Commands.create(CommonCommands.redirigir("nuevaBatalla.html"));
    }

    // botón "Entrar" de una batalla: si sigue en preparación se continúa el despliegue (CU02);
    // si está en combate o terminada, se va a librar/ver la batalla (CU03)
    @PostMapping("/reanudarBatalla")
    public Commands reanudarBatalla(@RequestParam int idBatalla) throws BattleException {
        Batalla b = f.getBatalla(idBatalla);
        if (b.enPreparacion()) {
            return Commands.create(CommonCommands.redirigir("nuevaBatalla.html?idBatalla=" + idBatalla));
        }
        return Commands.create(CommonCommands.redirigir("librarBatalla.html?idBatalla=" + idBatalla));
    }

    // la vista se cerró: deja de escuchar a la fachada
    @PostMapping("/vistaDesconectada")
    public void vistaDesconectada() {
        f.quitarObservador(this);
    }

    // ===================== COMMANDS =====================

    // le pregunta a la fachada los datos necesarios para la lista de batallas
    private Command listaBatallas() {
        return new Command("listaBatallas", new TotalBatallasDto(f.getBatallas(),
                f.cantidadGanadas(), f.cantidadPerdidas(), f.porcentajeExito()));
    }

}
