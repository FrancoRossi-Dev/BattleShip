package ort.da.BattleShip.model;

public class TacticaSecuencial implements TacticaInterfaz {

    @Override
    public String GetNombre() {
        return "Táctica Secuencial";
    }

    // como no saltea celdas, la cantidad de disparos alcanza para saber la posicion
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        int disparos = historial.cantidad();
        int ancho = historial.getAncho();
        return new Celda(disparos / ancho, disparos % ancho);
    }

}
