package ort.da.BattleShip.model;

public class BarcoFactory {

    public static Barco crear(TipoBarco tb) {
        return new Barco(tb);
    }


    
}
