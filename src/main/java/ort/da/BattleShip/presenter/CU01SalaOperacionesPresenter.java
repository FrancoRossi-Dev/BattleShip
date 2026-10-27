package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Facade;

@RestController
public class CU01SalaOperacionesPresenter {

    private Facade f;

    public CU01SalaOperacionesPresenter (Facade facade) {
        this.f = facade;
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
