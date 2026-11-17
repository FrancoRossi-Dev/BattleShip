package ort.da.BattleShip.presenter;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ort.da.BattleShip.dto.CeldaDto;
import ort.da.BattleShip.dto.CeldasDto;
import ort.da.BattleShip.dto.InicializarTableroDto;
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

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
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
    public Commands vistaConectada(@RequestParam(required = false) Integer idBatalla) throws BattleException {
        // como Agenda con el usuario no logueado: sin batalla indicada se vuelve a la sala
        if (idBatalla == null) return Commands.create(CommonCommands.redirigir("index.html"));
        batallaActual = f.getBatalla(idBatalla);
        batallaActual.agregarObservador(this);

        // también se entra a ver batallas ganadas o perdidas: el tablero queda habilitado igual y
        // un disparo en una batalla finalizada lo rechaza el modelo ("La batalla ya está finalizada")
        return Commands.create(lugarBatalla(), estadoBatalla(), numeroRonda(), inicializarTablero(),
                tableroPropio(), tableroEnemigo(), bajasEnemigas());
    }

    @PostMapping("/vistaDesconectada")
    public void vistaDesconectada() {
        if (batallaActual != null) {
            batallaActual.quitarObservador(this);
        }
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
   
    // los nombres de parámetros son los que envía BattleShipBoard al hacer clic.
    // vertical = fila (Y), horizontal = columna (X)
    @PostMapping("/jugarRonda")
    public Commands jugarRonda(@RequestParam int coordenadaVertical, @RequestParam int coordenadaHorizontal) throws BattleException {
        // los disparos de la ronda y el fin de la batalla llegan a la vista por SSE, desde actualizar()
        // (mismo criterio que CU02: no se devuelven también acá para no dibujar dos veces)
        batallaActual.jugarRonda(coordenadaHorizontal, coordenadaVertical);
        return Commands.create();
    }

    // flujo alternativo 2: clic en aguas propias
    @PostMapping("/aguasPropias")
    public Commands aguasPropias() throws BattleException {
        throw new BattleException("No puede disparar a sus propias aguas");
    }
     
    /*
    5. Opcionalmente selecciona “Volver a la 
    sala de operaciones” 
    6. La batalla se pausa en la situación actual y se ejecuta el CU01. 
    Postcondiciones 
    */


    @PostMapping("/volverASala")
    public Commands volverASala() {
        // la batalla queda pausada tal como está: no hay nada que guardar
        return Commands.create(CommonCommands.redirigir("index.html"));
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

    // los dos disparos de la ronda van en el mismo Commands: la vista los ve juntos (ronda atómica)
    private Commands rondaJugada() {
        Ronda r = batallaActual.ultimaRonda();
        Commands comandos = Commands.create(
                new Command("mostrarAguasEnemigas", new CeldaDto(r.getDisparoHumano().getCelda(), false)));
        // si el humano hundió la flota enemiga, la máquina no dispara
        if (r.getDisparoMaquina() != null) {
            comandos.add(new Command("mostrarAguasPropias", new CeldaDto(r.getDisparoMaquina().getCelda(), true)));
        }
        return comandos.add(bajasEnemigas(), numeroRonda());
    }

    // CAMBIO_ESTADO llega después de RONDA_FINALIZADA (ver EstadoEnCombate.jugarRonda)
    private Commands batallaFinalizada() {
        Commands comandos = Commands.create(estadoBatalla(), numeroRonda());
        if (batallaActual.revelaBarcosEnemigos()) comandos.add(tableroEnemigo());
        return comandos;
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        if (evento.equals(Batalla.Eventos.RONDA_FINALIZADA)) {
            conexionNavegador.enviarJSON(rondaJugada());
        }
        if (evento.equals(Batalla.Eventos.CAMBIO_ESTADO)) {
            conexionNavegador.enviarJSON(batallaFinalizada());
        }
    }

}
