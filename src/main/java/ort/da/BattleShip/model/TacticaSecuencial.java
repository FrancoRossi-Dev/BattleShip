package ort.da.BattleShip.model;

// recorre el oceano celda por celda, fila por fila, de izquierda a derecha
public class TacticaSecuencial implements TacticaInterfaz {

    @Override
    public String GetNombre() {
        return "Táctica Secuencial";
    }

    // como no saltea celdas, la cantidad de disparos alcanza para saber la posicion:
    // el disparo numero n cae en la celda n si se numeran las celdas fila por fila.
    // ej. con ancho 10: disparo 0 -> (0,0), 9 -> (0,9), 10 -> (1,0), 23 -> (2,3)
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        int disparos = historial.cantidad();
        int ancho = historial.getAncho();
        // cada fila completa son "ancho" disparos: el cociente da la fila
        // y el resto da la columna dentro de esa fila
        return new Celda(disparos / ancho, disparos % ancho);
    }

    // nunca cambia: sigue recorriendo en orden toda la partida
    @Override
    public TacticaInterfaz siguienteTactica(Disparo ultimoDisparo) {
        return this;
    }

}
