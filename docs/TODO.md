# TODO — Obligatorio BattleShip

Pendientes detectados al comparar el código con la letra (`Obligatorio DADG SEPTIEMBRE 2026.pdf`).
Entrega: **19/11/2026, 21:00**.

---

## 1. Vistas (no existe ninguna)

- [ ] `src/main/resources/static/index.html` — CU01 Sala de Operaciones (debe estar en `/`)
- [ ] `nuevaBatalla.html` — CU02
- [ ] `librarBatalla.html` — CU03
- [ ] Cada vista:
  - [ ] `urlInicio` → `.../vistaConectada`, `urlCierre` → `.../desconectarVista`
  - [ ] Abrir conexión SSE
  - [ ] Manejar el comando `"redirect to"` (CommandDispatcher no lo maneja solo)
  - [ ] Mostrar errores (status 299 de `GlobalExceptionHandler`)
- [ ] Mover `SSECommandDispatcher.js` de `java/.../observador/` a `resources/static/`
- [ ] Borrar o reemplazar `batalla-naval.html` (apunta a `/inicializarVista`, que no existe)

## 2. Métodos de presenters sin implementar

- [ ] **CU01** `reanudarBatalla` (botón "Entrar") — hoy devuelve `null`
- [ ] **CU02** `habilitarFormulario`, `mostrarBatallaEnPreparacion`, `actualizarTablero`, `batallaPreparada`, `actualizar`
- [ ] **CU03** `HabilitarJuego`, `MostrarEstadisticaBatalla`, `revelarBarcos`, `actualizarTablero`, `juegoFinalizado`, `actualizar`

## 3. Contrato con `BattleShipBoard.js`

El tablero, al hacer clic, envía `{coordenadaVertical, coordenadaHorizontal}` al escuchador.

- [ ] `CU03.disparar` espera `CoorX`/`CoorY` → renombrar parámetros
- [ ] `CU02.desplegarFlota` espera `coorX`/`coorY`/`orientacion`/`indiceBarco` → renombrar coords
  - [ ] La orientación no viene en el clic: guardarla en el presenter con un endpoint aparte (default Horizontal)
  - [ ] `indiceBarco` sobra: siempre se coloca el primero de la lista
- [ ] En `inicializar` pasar `escuchadorOceanoPropio` y `escuchadorOceanoEnemigo`
  - CU02: propio → `desplegarFlota`, enemigo → `aguasEnemigas`
  - CU03: enemigo → `disparar`, propio → **nuevo endpoint** que tire error ("no puede disparar a sus aguas")
- Valores de celda que entiende el tablero: `"barco"`, `"agua"`, `"impacto"`, `"nada"`
- Comandos que escucha: `inicializar`, `mostrarAguasPropias`, `mostrarAguasEnemigas`, `bajasEnemigas`

## 4. Modelo

- [ ] **Bajas enemigas** (lista cronológica, formato `{4} {1} {2}`)
  - [ ] Bug: `Disparo.hundioBarco()` mira el estado *actual* del barco → da `true` para todos los impactos de un barco ya hundido
  - [ ] Que `Disparo` guarde en su construcción si fue el que hundió el barco
  - [ ] `Batalla.bajasEnemigas()` derivada de `rondas` (disparos humanos que hundieron) → orden cronológico gratis
  - [ ] Ajustar `TacticaCazaYAtaque.impactosSinHundir` para que use `celda.getBarco().isVivo()`
- [ ] **Exponer celdas para la vista**: `Celda.valor(boolean mostrarBarcos)` (Experto) y que `Oceano` las recorra
  - aguas propias, aguas enemigas (ocultas), revelar barcos no descubiertos si la batalla se perdió
  - [ ] CU03 necesita **su propio dto de tablero**, distinto de `TableroDespliegueDto` (que es solo para el despliegue de CU02). Reusar `CeldaDto` con `mostrarBarcos=false` para aguas enemigas
- [ ] **Número de ronda** (`rondas.size() + 1` en combate)
- [ ] **Barcos pendientes en preparación**: exponer desde `Batalla` (método polimórfico en el estado; lista vacía fuera de preparación) para la lista y la leyenda "Posicione su X en forma Y" / "Batalla preparada"
- [ ] **Concurrencia**: `jugarRonda` `synchronized` (gana el primer disparo); revisar listas de `Observable`
- [ ] Quitar `FLOTA_UNDIDA` del enum de eventos (lo cubre `CAMBIO_ESTADO`)

## 5. Observer y eventos (4 pts)

- [ ] `Facade.actualizar`: reemitir `ACTUALIZACION_DE_LISTA` ante `CAMBIO_ESTADO` y `RONDA_FINALIZADA`
- [ ] `Facade.agregar(batalla)`: avisar `ACTUALIZACION_DE_LISTA` (batalla nueva visible en otras sesiones)
- [ ] CU02 y CU03: suscribirse a la `Batalla` en `vistaConectada`, desuscribirse en `volverASala` y al cerrar la vista
- [ ] CU02 y CU03: endpoint `registrarSSE` (hoy solo existe en CU01)

## 6. Polimorfismo (9 pts)

- [ ] `Batalla.isGanada()/isPerdida()` comparan strings → `estado.isGanada()` polimórfico (o clases `EstadoGanada`/`EstadoPerdida`)
- [ ] `EstadoFactory` hace `switch` sobre strings
- [ ] `TacticaFactory` devuelve `null` en el default
- [ ] `Batalla.getNombreEstado()` usa `getClass().getSimpleName()`
- [ ] `EstadoBatalla.resultado` duplicado/ocultado por `EstadoFinalizada.resultado`
- [ ] "Entrar" (CU01 → CU02 o CU03): usar un booleano polimórfico del estado (`enPreparacion()`), no comparar nombres
- [ ] `BatallaDto`: agregar id (índice) para que "Entrar" sepa a qué batalla ir
- [ ] **Dividir `TacticaCazaYAtaque` en 2 estrategias (patrón Strategy, cambio dinámico)**
  - [ ] Una estrategia de **caza** (buscar barcos) y otra de **ataque** (rematar alrededor de los impactos sin hundir)
  - [ ] `TacticaCazaYAtaque` queda como contexto: tiene la estrategia actual y la cambia en tiempo de ejecución (impacto → ataque; barco hundido/sin impactos pendientes → caza)
  - [ ] Sin `if`/flags para elegir el modo: cada estrategia decide a cuál pasar

## 7. Precarga (`DatosPrueba`)

- [x] Una batalla por estado (5 en total):
  - [x] En preparación sin barcos ubicados (Malvinas)
  - [x] En preparación con la flota a medio ubicar (Trafalgar)
  - [x] En combate, 3 rondas jugadas (Midway)
  - [x] Ganada (Jutlandia)
  - [x] Perdida (Leyte)
- [x] Para armarlas: desplegar barcos humanos → `librarBatalla()` → jugar rondas por código
- [x] Al menos dos barcos de distinto tamaño por batalla

## 8. Entregables no-código

- [ ] Diagrama de clases conceptual (`.asta`)
- [ ] Diagrama de clases de diseño (`.asta`) con modelo, vistas y presentadores
- [ ] PDF: portada, autoevaluación de errores, **cita de uso de IA** (herramientas y contexto)
- [ ] Repo privado `DA-2026Agosto-[Grupo]-[NroEst1]-[NroEst2]` con acceso de lectura al docente

---

## Ya resuelto

- Validaciones CU02: lugar vacío → aleatorio, lugar duplicado, lados 6–12, máx. 6 por tipo, flota vacía, densidad < 25%
- Colocación de barcos: fuera de tablero, superposición, adyacencia
- Despliegue aleatorio de la flota enemiga
- Tácticas Secuencial, Espiral, Caza y ataque
- No se puede disparar dos veces a la misma celda
- La máquina no dispara si el humano ya ganó
- Patrón State para las transiciones de la batalla
