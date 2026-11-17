# BattleShip — Batalla Naval

Obligatorio de Diseño de Aplicaciones (ORT, septiembre 2026). Spring Boot + MVP (vista pasiva) + SSE.

Ejecutar: `./mvnw spring-boot:run` y abrir `http://localhost:8080/`.

Los diagramas de abajo son el punto de partida para armar los `.asta` en Astah. Reflejan el código actual.

---

## 1. Diagrama conceptual del dominio

Solo conceptos del problema y sus atributos, sin métodos ni clases técnicas.

```mermaid
classDiagram
    direction LR

    class Batalla {
        lugar
        celdasHorizontales
        celdasVerticales
        estado
    }
    class EstadoBatalla {
        <<enumeración conceptual>>
        En preparación
        En combate
        Ganada
        Perdida
    }
    class TacticaDeJuego {
        nombre
    }
    class TipoBarco {
        nombre
        tamaño
    }
    class Flota
    class Barco
    class Jugador
    class JugadorHumano
    class JugadorMaquina
    class Oceano {
        ancho
        alto
    }
    class Celda {
        fila
        columna
    }
    class Ronda {
        numero
    }
    class Disparo {
        resultado (agua / impacto / hundido)
    }

    Batalla "1" -- "1" EstadoBatalla : está en
    Batalla "1" -- "1" Flota : se configura con
    Batalla "1" -- "1" JugadorHumano
    Batalla "1" -- "1" JugadorMaquina
    Batalla "1" -- "0..*" Ronda : se juega en
    JugadorMaquina "*" -- "1" TacticaDeJuego : juega con
    Flota "1" -- "1..*" Barco
    Barco "*" -- "1" TipoBarco : es de
    Jugador <|-- JugadorHumano
    Jugador <|-- JugadorMaquina
    Jugador "1" -- "1" Oceano : aguas propias
    Oceano "1" -- "*" Celda
    Barco "0..1" -- "1..*" Celda : ocupa
    Ronda "1" -- "1..2" Disparo : humano, luego máquina
    Disparo "0..1" -- "1" Celda : apunta a
    Batalla "1" -- "0..*" Barco : bajas enemigas (orden cronológico)
```

---

## 2. Diagrama de diseño — Modelo

### 2.1 Fachada, sistemas y Observer

```mermaid
classDiagram
    direction TB

    class Observable {
        -List~Observador~ observadores
        +agregarObservador(Observador)
        +quitarObservador(Observador)
        +avisar(Object evento)
    }
    class Observador {
        <<interface>>
        +actualizar(Observable origen, Object evento)
    }

    class Facade {
        <<singleton>>
        -Facade instancia$
        +getInstancia()$ Facade
        +agregar(Batalla)
        +getBatalla(int id) Batalla
        +idDe(Batalla) int
        +getBatallas() List~Batalla~
        +cantidadGanadas() int
        +cantidadPerdidas() int
        +porcentajeExito() int
        +agregar(TipoBarco)
        +getTiposBarco() List~TipoBarco~
        +agregar(TacticaTipo)
        +getTacticas() List~TacticaTipo~
        +actualizar(Observable, Object)
    }
    class Facade_Eventos {
        <<enumeration>>
        ACTUALIZACION_DE_LISTA
    }
    class SistemaBatalla {
        -List~Batalla~ batallas
        +agregar(Batalla)
        +getBatalla(int) Batalla
        +idDe(Batalla) int
        +cantidadGanadas() int
        +cantidadPerdidas() int
        +porcentajeExito() int
    }
    class SistemaBarcos {
        -List~TipoBarco~ tiposBarco
        +agregar(TipoBarco)
    }
    class SistemaTacticas {
        -List~TacticaTipo~ tiposDeTacticas
        +agregar(TacticaTipo)
    }
    class DatosPrueba {
        +cargarDatos()$
    }
    class BattleException {
        +BattleException(String mensaje)
    }

    Observable <|-- Facade
    Observador <|.. Facade
    Observable <|-- Batalla
    Facade *-- SistemaBatalla
    Facade *-- SistemaBarcos
    Facade *-- SistemaTacticas
    Facade ..> Facade_Eventos : avisa
    Facade ..> Batalla : observa
    SistemaBatalla o-- "*" Batalla
    SistemaBarcos o-- "*" TipoBarco
    SistemaTacticas o-- "*" TacticaTipo
    DatosPrueba ..> Facade : precarga
    Exception <|-- BattleException
```

### 2.2 Batalla y patrón State

```mermaid
classDiagram
    direction TB

    class Batalla {
        -String lugar
        -int ancho
        -int alto
        -EstadoBatalla estado
        -Flota flota
        -JugadorHumano jugadorHumano
        -JugadorMaquina jugadorMaquina
        -List~Ronda~ rondas
        +desplegarBarco(int x, int y, String orientacion)
        +librarBatalla()
        +jugarRonda(int x, int y) synchronized
        ~cambiarEstado(EstadoTipo)
        ~hacerJugarRonda(int x, int y)
        +barcosPendientes() List~Barco~
        +flotaDesplegada() boolean
        +enPreparacion() boolean
        +esFinalizada() boolean
        +isGanada() boolean
        +isPerdida() boolean
        +revelaBarcosEnemigos() boolean
        +nombreEstado() String
        +numeroRonda() int
        +tamanoFlota() int
        +partesSanasPropias() int
        +partesSanasEnemigas() int
        +celdasPropias() List~Celda~
        +celdasEnemigas() List~Celda~
        +bajasEnemigas() List~Barco~
        +ultimaRonda() Ronda
        +getUltimoBarcoColocado() Barco
    }
    class Batalla_Eventos {
        <<enumeration>>
        CAMBIO_ESTADO
        BARCO_DESPLEGADO
        RONDA_FINALIZADA
    }

    class EstadoBatalla {
        <<abstract>>
        -Batalla batalla
        +getNombre()* String
        ~desplegarBarco(int x, int y, String orientacion)*
        ~librarBatalla()*
        ~jugarRonda(int x, int y)*
        ~flotaDesplegada()* boolean
        ~esFinalizada()* boolean
        ~proximoBarco() Barco
        ~barcosPendientes() List~Barco~
        ~enPreparacion() boolean
        ~revelaBarcosEnemigos() boolean
    }
    class EstadoPreparacion {
        -Flota pendienteAsignar
    }
    class EstadoEnCombate
    class EstadoFinalizada {
        +String GANADA$
        +String PERDIDA$
        -String resultado
    }
    class EstadoTipo {
        +EstadoTipo PREPARACION$
        +EstadoTipo EN_COMBATE$
        +EstadoTipo FINALIZADA_VICTORIA$
        +EstadoTipo FINALIZADA_DERROTA$
        -String nombre
    }
    class EstadoFactory {
        +crearEstadoBatalla(EstadoTipo, Batalla)$ EstadoBatalla
    }

    Batalla ..> Batalla_Eventos : avisa
    Batalla "1" *-- "1" EstadoBatalla : estado
    EstadoBatalla --> Batalla : contexto
    EstadoBatalla <|-- EstadoPreparacion
    EstadoBatalla <|-- EstadoEnCombate
    EstadoBatalla <|-- EstadoFinalizada
    Batalla ..> EstadoFactory : cambiarEstado
    EstadoFactory ..> EstadoTipo
    EstadoFactory ..> EstadoBatalla : crea
    EstadoPreparacion --> Flota : pendientes de ubicar
```

### 2.3 Jugadores, océano, flota y disparos

```mermaid
classDiagram
    direction TB

    class Batalla
    class Jugador {
        <<abstract>>
        -Oceano oceano
        +desplegarBarco(TipoBarco, int x, int y, String orientacion)
        +recibirDisparo(Celda) Disparo
        +partesImpactadas() int
        +flotaHundida() boolean
        +getCeldas() List~Celda~
        +getBarcosHundidos() List~Barco~
        +getUltimoBarcoDesplegado() Barco
        ~vaciarOceano()
    }
    class JugadorHumano
    class JugadorMaquina {
        -TacticaInterfaz tactica
        -HistorialDisparos historial
        +elegirObjetivo() Celda
        +desplegarAleatorio(Flota)
        +agregarAlHistorial(Disparo)
    }
    class Oceano {
        -int ancho
        -int alto
        -Celda celdas (matriz alto x ancho)
        -List~Barco~ barcosVivos
        -List~Barco~ barcosHundidos
        -int impactos
        +desplegarBarco(TipoBarco, int x, int y, String orientacion)
        +recibirDisparo(Celda) Disparo
        +getCelda(int x, int y) Celda
        +getCeldas() List~Celda~
        +partesImpactadas() int
        +flotaHundida() boolean
        +getBarcosHundidos() List~Barco~
        +getUltimoBarcoDesplegado() Barco
    }
    class Celda {
        -int fila
        -int columna
        -Barco barco
        -Disparo disparo
        +recibirDisparo() Disparo
        +isDisparada() boolean
        +estaEn(int fila, int columna) boolean
        +valor(boolean mostrarBarcos) String
        +setBarco(Barco)
    }
    class Barco {
        -TipoBarco tipo
        -List~Celda~ celdas
        +partesSanas() int
        +isVivo() boolean
        +getNombre() String
        +getTamano() int
        +addCelda(Celda)
    }
    class BarcoFactory {
        +crear(TipoBarco)$ Barco
    }
    class TipoBarco {
        -String nombre
        -int tamano
    }
    class Flota {
        -List~Barco~ barcos
        +Flota(List~TipoBarco~, int[] cantidades)
        +Flota(Flota copia)
        +tamanoTotal() int
        +remover(Barco)
    }
    class Ronda {
        -Disparo disparoHumano
        -Disparo disparoMaquina
        +turnoHumano(Jugador rival, Celda objetivo) Disparo
        +turnoMaquina(JugadorMaquina, Jugador rival) Disparo
    }
    class Disparo {
        -Celda celda
        -boolean hundio
        ~registrarResultado()
        +isImpacto() boolean
        +hundioBarco() boolean
    }
    class HistorialDisparos {
        -int ancho
        -int alto
        -List~Disparo~ disparos
        +agregar(Disparo)
        +cantidad() int
        +estaCompleto() boolean
        +estaLibre(int fila, int columna) boolean
        +celdasLibres() List~Celda~
    }

    Batalla "1" *-- "1" Flota : configuración
    Batalla "1" *-- "1" JugadorHumano
    Batalla "1" *-- "1" JugadorMaquina
    Batalla "1" *-- "0..*" Ronda
    Jugador <|-- JugadorHumano
    Jugador <|-- JugadorMaquina
    Jugador "1" *-- "1" Oceano
    Oceano "1" *-- "*" Celda
    Oceano "1" o-- "*" Barco
    Oceano ..> BarcoFactory
    Celda "*" --> "0..1" Barco
    Celda "1" --> "0..1" Disparo
    Barco "1" --> "1..*" Celda
    Barco "*" --> "1" TipoBarco
    Flota "1" o-- "1..*" Barco
    Ronda "1" --> "1..2" Disparo
    Disparo --> Celda
    JugadorMaquina "1" *-- "1" HistorialDisparos
    HistorialDisparos o-- "*" Disparo
```

### 2.4 Tácticas: Strategy + cambio dinámico de modo (Caza y ataque)

```mermaid
classDiagram
    direction TB

    class JugadorMaquina {
        -TacticaInterfaz tactica
        +elegirObjetivo() Celda
        +agregarAlHistorial(Disparo)
    }
    class TacticaInterfaz {
        <<interface>>
        +getNombre() String
        +elegirCelda(HistorialDisparos) Celda
        +registrarDisparo(Disparo)
    }
    class TacticaSecuencial
    class TacticaEspiral {
        -int[][] DIRECCIONES$
    }
    class TacticaCazaYAtaque {
        -ModoCazaYAtaque modo
        ~cambiarModo(ModoCazaYAtaque)
    }
    class ModoCazaYAtaque {
        <<abstract>>
        -TacticaCazaYAtaque tactica
        +getNombre()* String
        +elegirCelda(HistorialDisparos)* Celda
        +registrarDisparo(Disparo)*
    }
    class ModoCaza
    class ModoAtaque
    note for ModoCaza "impacto sin hundir: cambia a ModoAtaque"
    note for ModoAtaque "barco hundido: vuelve a ModoCaza"
    class TacticaTipo {
        +String SECUENCIAL$
        +String ESPIRAL$
        +String CAZA_Y_ATAQUE$
        -String nombre
    }
    class TacticaFactory {
        +crearTactica(TacticaTipo)$ TacticaInterfaz
    }
    class HistorialDisparos

    JugadorMaquina "1" --> "1" TacticaInterfaz : strategy
    TacticaInterfaz <|.. TacticaSecuencial
    TacticaInterfaz <|.. TacticaEspiral
    TacticaInterfaz <|.. TacticaCazaYAtaque
    TacticaCazaYAtaque "1" *-- "1" ModoCazaYAtaque : modo actual
    ModoCazaYAtaque --> TacticaCazaYAtaque : contexto
    ModoCazaYAtaque <|-- ModoCaza
    ModoCazaYAtaque <|-- ModoAtaque
    JugadorMaquina ..> TacticaFactory
    TacticaFactory ..> TacticaTipo
    TacticaFactory ..> TacticaInterfaz : crea
    TacticaInterfaz ..> HistorialDisparos
```

---

## 3. Diagrama de diseño — Presentación (MVP, vista pasiva + SSE)

```mermaid
classDiagram
    direction LR

    class index_html {
        <<vista>>
        CU01 Sala de operaciones
    }
    class nuevaBatalla_html {
        <<vista>>
        CU02 Preparar nueva batalla
    }
    class librarBatalla_html {
        <<vista>>
        CU03 Librar batalla
    }
    class CommandDispatcher_js {
        <<biblioteca JS>>
    }
    class SSECommandDispatcher_js {
        <<biblioteca JS>>
    }
    class BattleShipBoard_js {
        <<componente JS>>
    }

    class CU01SalaOperacionesPresenter {
        <<RestController, session>>
        -Facade f
        -ConexionNavegador conexionNavegador
        +vistaConectada() Commands
        +vistaDesconectada()
        +registrarSSE() SseEmitter
        +nuevaBatalla() Commands
        +reanudarBatalla(int idBatalla) Commands
        +actualizar(Observable, Object)
    }
    class CU02PrepararNuevaBatallaPresenter {
        <<RestController, session>>
        -Facade f
        -Batalla batalla
        -List~TacticaTipo~ listaDeTacticas
        -List~TipoBarco~ listaDeTiposDeBarco
        -ConexionNavegador conexionNavegador
        +vistaConectada(Integer idBatalla) Commands
        +vistaDesconectada()
        +registrarSSE() SseEmitter
        +crearBatalla(lugar, posTactica, ancho, alto, cantidades) Commands
        +desplegarFlota(coordenadaVertical, coordenadaHorizontal, orientacion) Commands
        +aguasEnemigas() Commands
        +librarBatalla() Commands
        +volverASala() Commands
        +actualizar(Observable, Object)
    }
    class CU03LibrarBatallaPresenter {
        <<RestController, session>>
        -Facade f
        -Batalla batallaActual
        -ConexionNavegador conexionNavegador
        +vistaConectada(Integer idBatalla) Commands
        +vistaDesconectada()
        +registrarSSE() SseEmitter
        +jugarRonda(coordenadaVertical, coordenadaHorizontal) Commands
        +aguasPropias() Commands
        +volverASala() Commands
        +actualizar(Observable, Object)
    }

    class Command {
        -String id
        -Object parametro
    }
    class Commands {
        -List~Command~ commands
        +create(Command...)$ Commands
        +add(Command...) Commands
    }
    class CommonCommands {
        +redirigir(String pagina)$ Command
    }
    class GlobalExceptionHandler {
        <<ControllerAdvice>>
        +manejarException(BattleException) ResponseEntity
    }
    class ConexionNavegador {
        <<Component, session>>
        -SseEmitter conexionSSE
        +conectarSSE()
        +cerrarConexion()
        +enviarJSON(Object)
    }
    class Observador {
        <<interface>>
    }
    class Facade
    class Batalla

    index_html ..> CommandDispatcher_js
    nuevaBatalla_html ..> CommandDispatcher_js
    librarBatalla_html ..> CommandDispatcher_js
    index_html ..> SSECommandDispatcher_js
    nuevaBatalla_html ..> SSECommandDispatcher_js
    librarBatalla_html ..> SSECommandDispatcher_js
    nuevaBatalla_html ..> BattleShipBoard_js
    librarBatalla_html ..> BattleShipBoard_js

    index_html ..> CU01SalaOperacionesPresenter : /salaOperaciones
    nuevaBatalla_html ..> CU02PrepararNuevaBatallaPresenter : /nuevaBatalla
    librarBatalla_html ..> CU03LibrarBatallaPresenter : /librarBatalla

    Observador <|.. CU01SalaOperacionesPresenter
    Observador <|.. CU02PrepararNuevaBatallaPresenter
    Observador <|.. CU03LibrarBatallaPresenter
    CU01SalaOperacionesPresenter --> Facade : observa
    CU02PrepararNuevaBatallaPresenter --> Facade
    CU02PrepararNuevaBatallaPresenter --> Batalla : observa
    CU03LibrarBatallaPresenter --> Facade
    CU03LibrarBatallaPresenter --> Batalla : observa
    CU01SalaOperacionesPresenter --> ConexionNavegador
    CU02PrepararNuevaBatallaPresenter --> ConexionNavegador
    CU03LibrarBatallaPresenter --> ConexionNavegador
    Commands o-- "*" Command
    CU01SalaOperacionesPresenter ..> Commands
    CU02PrepararNuevaBatallaPresenter ..> Commands
    CU03LibrarBatallaPresenter ..> Commands
    CU01SalaOperacionesPresenter ..> CommonCommands
    CU02PrepararNuevaBatallaPresenter ..> CommonCommands
    CU03LibrarBatallaPresenter ..> CommonCommands
```

### 3.1 DTOs (un DTO por comando, datos justos)

```mermaid
classDiagram
    direction LR

    class TotalBatallasDto {
        -List~BatallaDto~ batallas
        -int ganadas
        -int perdidas
        -int porcentajeExito
    }
    class BatallaDto {
        -int id
        -String lugar
        -int ancho
        -int alto
        -String estado
        -int flotaPropiaSanas
        -int tamanoFlotas
        -int flotaEnemigaSanas
        +fromList(List~Batalla~)$ List~BatallaDto~
    }
    class TacticaDto {
        -String nombre
        +fromList(List~TacticaTipo~)$ List~TacticaDto~
    }
    class TipoBarcoDto {
        -String nombre
        -int tamano
        +TipoBarcoDto(TipoBarco)
        +TipoBarcoDto(Barco)
        +fromTipos(List~TipoBarco~)$ List~TipoBarcoDto~
        +fromBarcos(List~Barco~)$ List~TipoBarcoDto~
    }
    class InicializarTableroDto {
        -int numeroCeldasVerticales
        -int numeroCeldasHorizontales
        -String escuchadorOceanoPropio
        -String escuchadorOceanoEnemigo
    }
    class CeldasDto {
        -List~CeldaDto~ celdas
        +CeldasDto(List~Celda~, boolean mostrarBarcos)
    }
    class CeldaDto {
        -int coordenadaVertical
        -int coordenadaHorizontal
        -String valor
        +CeldaDto(Celda, boolean mostrarBarcos)
    }

    class CU01SalaOperacionesPresenter
    class CU02PrepararNuevaBatallaPresenter
    class CU03LibrarBatallaPresenter

    TotalBatallasDto *-- "*" BatallaDto
    CeldasDto *-- "*" CeldaDto

    CU01SalaOperacionesPresenter ..> TotalBatallasDto : listaBatallas
    CU02PrepararNuevaBatallaPresenter ..> TacticaDto : listaTacticas
    CU02PrepararNuevaBatallaPresenter ..> TipoBarcoDto : listaTiposBarco / barcosPendientes
    CU02PrepararNuevaBatallaPresenter ..> InicializarTableroDto : inicializar
    CU02PrepararNuevaBatallaPresenter ..> CeldasDto : tableroPropio / barcoUbicado
    CU03LibrarBatallaPresenter ..> InicializarTableroDto : inicializar
    CU03LibrarBatallaPresenter ..> CeldasDto : tableroPropio / tableroEnemigo
    CU03LibrarBatallaPresenter ..> CeldaDto : mostrarAguasPropias / mostrarAguasEnemigas

    BatallaDto ..> Batalla
    TacticaDto ..> TacticaTipo
    TipoBarcoDto ..> TipoBarco
    TipoBarcoDto ..> Barco
    InicializarTableroDto ..> Batalla
    CeldaDto ..> Celda
```

### Comandos por presentador

| Presentador | Comando | Parámetro |
|---|---|---|
| CU01 | `listaBatallas` | `TotalBatallasDto` |
| CU02 | `listaTacticas` | `List<TacticaDto>` |
| CU02 | `listaTiposBarco`, `barcosPendientes` | `List<TipoBarcoDto>` |
| CU02 | `habilitarFormulario` | `boolean` |
| CU02 / CU03 | `lugarBatalla` | `String` |
| CU02 / CU03 | `inicializar` | `InicializarTableroDto` |
| CU02 / CU03 | `tableroPropio` | `CeldasDto` |
| CU02 | `barcoUbicado` | `CeldasDto` |
| CU02 | `confirmarBatallaLista` | `String` (lugar) |
| CU03 | `estadoBatalla` | `String` |
| CU03 | `numeroRonda` | `int` |
| CU03 | `tableroEnemigo` | `CeldasDto` |
| CU03 | `mostrarAguasPropias`, `mostrarAguasEnemigas` | `CeldaDto` |
| CU03 | `bajasEnemigas` | `String` (`{4} {1} {2}`) |
| todos | `redirigir` | `String` (URL) |
