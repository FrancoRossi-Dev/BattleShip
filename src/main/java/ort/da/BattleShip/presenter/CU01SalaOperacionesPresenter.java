package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.da.BattleShip.dto.TotalBatallasDto;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.observador.ConexionNavegador;
import ort.da.BattleShip.observador.Observable;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/SalaOperaciones")
@Scope("session")
public class CU01SalaOperacionesPresenter {

    private Facade f;
    private List<Batalla> batallas;


    private ConexionNavegador conexionNavergador;

    public CU01SalaOperacionesPresenter (Facade facade, ConexionNavegador conexion) {
        this.f = facade;
        this.conexionNavergador = conexion;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter completar() 
    {
        conexionNavergador.conectarSSE();
        return consxionNavegador.getConexionSSE();
    }
    
    @Override 
    public void actualizar(Observable origen, Object evento) {
        if (true) {
            conexionNavergador.enviarJson(Commands.create("lo que me pinte"))
        }
    }

    @PostMapping("vistaConectada")
    public Commands vistaConectada() {
        batallas = f.getBatallas();
        return Commands.create(historialBatallas());
    }


    @PostMapping("nuevaBatalla")
    public Commands nuevaBatalla() {
        
        return Commands.create(CommonCommands.Redirection("nuevaBatalla.html"));
    }
    


    @PostMapping("reanudarBatalla")
    public Commands reanudarBatalla(@RequestParam String BatallaId) {
        
        return null;
    }
    

    private Command historialBatallas() {
        return new Command("historialBatallas", new TotalBatallasDto(batallas,
                f.cantidadGanadas(), f.cantidadPerdidas(), f.porcentajeExito()));
    }
    
    /*
    
    CU01: SALA DE OPERACIONES 
    Esta la vista principal del juego. Este caso de uso puede ejecutarse múltiples veces en simultaneo en diferentes 
    sesiones/navegadores y siempre debe mostrar la misma información.  
    
    Flujo normal 
    
    Usuario Aplicación 
    1. Muestra: 
    - Total de batallas ganadas: ## 
    - Total de batallas perdidas: ## 
    - Éxito de la campaña: ##% (porcentaje que refleja las batallas ganadas sobre el total de batallas) 
    
    - Lista de las batallas con los siguientes datos: 
    - Lugar del combate 
    - Dimensiones del océano (ancho x alto) en número de celdas 
    - Estado (En preparación | En combate | Ganada | Perdida) 
    - Integridad de la flota propia (partes sanas / tamaño total) 
    - Integridad de la flota enemiga (partes sanas / tamaño total) 
    - Una opción “Entrar” (botón o link en cada renglón) 
    - Opción para “Iniciar nueva batalla”. 
    2. Opcionalmente selecciona la 
    opción de Iniciar nueva batalla. 
    3. Ejecuta el CU02: Preparar una nueva batalla 
    4. Opcionalmente selecciona la 
    opción Entrar en una batalla. 
    4. Ejecuta el CU03: Librar batalla
    */
    
}
