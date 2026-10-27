package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

public class Barco {

    private boolean vivo;
    private TipoBarco tipo;
    private final List<Celda> celdas = new ArrayList<>();

    public boolean isVivo() {
        return vivo;
    }

    public TipoBarco getTipo() {
        return tipo;
    }

    public List<Celda> getCeldas() {
        return celdas;
    }
}
