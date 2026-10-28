package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.dto.BatallaDto;
import ort.da.BattleShip.dto.TotalBatallasDto;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.Facade;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/SalaOperaciones")
public class CU01SalaOperacionesPresenter {

    private Facade f;
    private List<Batalla> batallas;

    public CU01SalaOperacionesPresenter (Facade facade) {
        this.f = facade;
    }


    @PostMapping("conectarVista")
    public Commands conectarVista() {
        batallas = f.getBatallas();
        
        
        return Commands.create(MostrarHistorialBatallas(batallas));
    }


    @PostMapping("nuevaBatalla")
    public Commands nuevaBatalla() {
        
        return Commands.create(CommonCommands.Redirection("nuevaBatalla.html"));
    }
    


    @PostMapping("reanudarBatalla")
    public Commands reanudarBatalla(@RequestParam String BatallaId) {
        
        return null;
    }
    

    private Command MostrarHistorialBatallas(List<Batalla> batallas) {
        int victorias = 0, derrotas = 0;

        for(Batalla b : batallas) {
            if (b.getGanador().equals("humano")) {victorias++;}
            else {derrotas++;}
            }
        int porcentaje = victorias/batallas.size();
        return new Command("Historial Batallas", new TotalBatallasDto(batallas, victorias, derrotas, porcentaje));
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
