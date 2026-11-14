package ort.da.BattleShip.presenter;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.BattleException;
import ort.da.BattleShip.model.Celda;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.observador.Observable;
import ort.da.BattleShip.observador.Observador;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// CU03: LIBRAR BATALLA 
@RestController
@RequestMapping("/librarBatalla")
@Scope("session")
public class CU03LibrarBatallaPresenter implements Observador {
    private Facade f;
    private Batalla batallaActual;

    public CU03LibrarBatallaPresenter (Facade f) {
        this.f = f;
    }

    /*
     1. Muestra: 
    - El nombre del lugar de la batalla 
    - El numero de ronda (comenzando con 1 e incrementándose cada 
    vez que toca el turno al humano) 
    - El estado de la batalla. 
    - Los océanos con el siguiente contenido: 
    - La disposición de barcos en aguas propias. 
    - Los disparos efectuados (si hubiera) indicando si fueron agua o 
    impacto tanto en aguas propias como enemigas. 
    - La lista de bajas enemigas en formato “{#} {#} {#}...” donde cada 
    elemento entre llaves representa un barco hundido y el valor # 
    corresponde al tamaño de este. La lista se debe mostrar en la misma 
    secuencia en que fueron hundidos (cronológicamente) 

    - Un botón “Volver a la sala de operaciones” 
    */
    @PostMapping("/vistaConectada")
    public Commands vistaConectada(@RequestParam int idBatalla) throws BattleException {
        batallaActual = f.getBatalla(idBatalla);

        batallaActual.agregarObservador(this);
        
        return Commands.create(MostrarEstadisticaBatalla(), HabilitarJuego());
    }


    /*
    2. Selecciona (haciendo click) una 
    coordenada en el océano de “Aguas 
    enemigas” 
    3. La celda es marcada como “agua” o “impacto” según corresponda. 
    Si un barco es hundido, se agrega a la lista de “Bajas enemigas”. 
    Si la flota enemiga es hundida, fin de la batalla. 
    4. Siguiendo la táctica de juego configurada, el oponente realiza su 
    correspondiente disparo, que es reflejado en el océano de “Aguas 
    propias”

    Si la flota propia es hundida, fin de la batalla. 
    Esta secuencia se repite hasta que una flota es completamente hundida. 

    Flujos alternativos 
    2. Si el jugador intenta seleccionar una celda de su océano de aguas amigas, se indica error y la acción se ignora. 
    2. Si el jugador selecciona una celda que ya disparo anteriormente, se indica error y la acción se ignora.
 
    */
   
    @PostMapping("/jugarRonda")
    public Commands jugarRonda(@RequestParam int CoorX, @RequestParam int CoorY) throws BattleException {
        batallaActual.jugarRonda(CoorX, CoorY);
        if (batallaActual.esFinalizada()) {
            return Commands.create(juegoFinalizado(), actualizarTablero());
        }
        return Commands.create(actualizarTablero());
    }
     
    /*
    5. Opcionalmente selecciona “Volver a la 
    sala de operaciones” 
    6. La batalla se pausa en la situación actual y se ejecuta el CU01. 
    Postcondiciones 
    */


    @PostMapping("/volverASala")
    public Commands volverASala() {
        // validar que una batalla este en juego?
        return Commands.create(CommonCommands.Redirection("SalaOperaciones.html"));
    }
    

    /*
    CU03: LIBRAR BATALLA 
    Las batallas que se encuentran en combate se desarrollan en una serie de rondas hasta que una de las flotas es hundida. 
    En cada ronda, el jugador humano realiza un disparo. Si ese disparo no provoca que la batalla termine con la victoria del 
    jugador, la máquina realiza inmediatamente su propio disparo como respuesta. 
    
    Desde el punto de vista del jugador, ambos disparos y sus respectivos efectos forman una única operación atómica: la 
    ronda se completa íntegramente y no puede ser dividida ni interrumpida entre ambos disparos. Por ejemplo, al hacer clic 
    sobre una celda de las aguas enemigas, el jugador verá inmediatamente los dos disparos correspondientes a esa ronda: 
    primero el suyo y, a continuación, el de la máquina. 
    
    Nota: este caso de uso no solo se ejecuta para batallas en combate, en donde ocurren rondas de disparos, también se 
    puede acceder a esta vista para ver batallas ya ganadas o perdidas. 
    
  
Flujo normal 
 
Usuario Aplicación 
 1. Muestra: 
    - El nombre del lugar de la batalla 
    - El numero de ronda (comenzando con 1 e incrementándose cada 
    vez que toca el turno al humano) 
    - El estado de la batalla. 
    - Los océanos con el siguiente contenido: 
     - La disposición de barcos en aguas propias. 
     - Los disparos efectuados (si hubiera) indicando si fueron agua o 
impacto tanto en aguas propias como enemigas. 
     - La lista de bajas enemigas en formato “{#} {#} {#}...” donde cada 
elemento entre llaves representa un barco hundido y el valor # 
corresponde al tamaño de este. La lista se debe mostrar en la misma 
secuencia en que fueron hundidos (cronológicamente) 
- Un botón “Volver a la sala de operaciones” 


2. Selecciona (haciendo click) una 
coordenada en el océano de “Aguas 
enemigas” 
3. La celda es marcada como “agua” o “impacto” según corresponda. 
Si un barco es hundido, se agrega a la lista de “Bajas enemigas”. 
Si la flota enemiga es hundida, fin de la batalla. 
 4. Siguiendo la táctica de juego configurada, el oponente realiza su 
correspondiente disparo, que es reflejado en el océano de “Aguas 
propias”
Si la flota propia es hundida, fin de la batalla. 
Esta secuencia se repite hasta que una flota es completamente hundida. 

5. Opcionalmente selecciona “Volver a la 
sala de operaciones” 
6. La batalla se pausa en la situación actual y se ejecuta el CU01. 
 Postcondiciones 
 
- En el campo de “Estado de la batalla” se muestra la leyenda “Ganada” o “Perdida” según corresponda. 
- Idénticamente, en la sala de operaciones la batalla pasa a estado “Ganada” o “Perdida”. 
- Si la batalla fue perdida, se revelan las celdas de los barcos o fragmentos de barcos que no fueron descubiertos en las 
aguas enemigas.  
 
Flujos alternativos 
 2. Si el jugador intenta seleccionar una celda de su océano de aguas amigas, se indica error y la acción se ignora. 
2. Si el jugador selecciona una celda que ya disparo anteriormente, se indica error y la acción se ignora.
    
    
    */

// COMANDS

    private Command HabilitarJuego() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'HabilitarJuego'");
    }

    private Command MostrarEstadisticaBatalla() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'MostrarEstadisticaBatalla'");
    }

    private Command revelarBarcos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'revelarBarcos'");
    }

    private Command actualizarTablero() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarTablero'");
    }

    private Command juegoFinalizado() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'juegoFinalizado'");
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

}
