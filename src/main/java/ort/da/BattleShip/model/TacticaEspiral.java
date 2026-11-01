package ort.da.BattleShip.model;

public class TacticaEspiral implements TacticaInterfaz {

    // derecha, abajo, izquierda, arriba (la fila 0 es la de abajo)
    private static final int[][] DIRECCIONES = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

    @Override
    public String GetNombre() {
       return "Táctica Espiral";
    }

    // recorre la espiral desde el centro como si el oceano fuese cuadrado,
    // salteando lo que cae fuera del tablero y lo ya disparado.
    // termina siempre porque JugadorMaquina garantiza que queda una celda libre
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        int fila = historial.getAlto() / 2;
        int columna = (historial.getAncho() - 1) / 2;
        if (historial.estaLibre(fila, columna)) return new Celda(fila, columna);

        for (int tramo = 0; ; tramo++) {
            int[] direccion = DIRECCIONES[tramo % 4];
            int largoTramo = tramo / 2 + 1;
            for (int paso = 0; paso < largoTramo; paso++) {
                fila += direccion[0];
                columna += direccion[1];
                if (historial.estaLibre(fila, columna)) return new Celda(fila, columna);
            }
        }
    }

}
