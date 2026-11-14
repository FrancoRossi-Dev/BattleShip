package ort.da.BattleShip.model;

// dispara desde el centro del oceano hacia afuera, girando en espiral
public class TacticaEspiral implements TacticaInterfaz {

    // derecha, abajo, izquierda, arriba (la fila 0 es la de abajo)
    // cada par {df, dc} es lo que se suma a fila y columna para dar un paso
    private static final int[][] DIRECCIONES = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

    @Override
    public String GetNombre() {
       return "Táctica Espiral";
    }

    // recorre la espiral desde el centro como si el oceano fuese cuadrado,
    // salteando lo que cae fuera del tablero y lo ya disparado.
    // no guarda en que punto quedo: cada vez rehace el recorrido desde el centro
    // y devuelve la primera celda libre que encuentra.
    // termina siempre porque JugadorMaquina garantiza que queda una celda libre
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        // 1. se arranca en la celda central
        int fila = historial.getAlto() / 2;
        int columna = (historial.getAncho() - 1) / 2;
        if (historial.estaLibre(fila, columna)) return new Celda(fila, columna);

        // 2. se avanza por tramos rectos, girando 90 grados al terminar cada uno.
        //    los largos de los tramos son 1, 1, 2, 2, 3, 3, ...: cada dos giros
        //    la espiral se agranda una celda para rodear la vuelta anterior
        for (int tramo = 0; ; tramo++) {
            int[] direccion = DIRECCIONES[tramo % 4]; // gira cada tramo
            int largoTramo = tramo / 2 + 1;           // crece cada dos tramos
            for (int paso = 0; paso < largoTramo; paso++) {
                fila += direccion[0];
                columna += direccion[1];
                // 3. la primera celda dentro del oceano y no disparada es la elegida
                if (historial.estaLibre(fila, columna)) return new Celda(fila, columna);
            }
        }
    }

    // nunca cambia: sigue la espiral toda la partida
    @Override
    public TacticaInterfaz siguienteTactica(Disparo ultimoDisparo) {
        return this;
    }

}
