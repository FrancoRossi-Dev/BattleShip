package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Barco {

    @Getter
    private TipoBarco tipo;
    @Getter
    private List<Celda> celdas = new ArrayList<>();

    public Barco(TipoBarco tipo) {
        this.tipo = tipo;
    }

    public int partesSanas() {
        int sanas = tipo.getTamano();
        for (Celda c : celdas) {
            if (c.isDisparada()) {
                sanas--;
            }
        }
        return sanas;
    }

    public boolean isVivo() {
        return partesSanas() > 0;
    }

    public int getTamano() {
        return tipo.getTamano();
    }

    public void addCelda(Celda c) {
        if (c != null) celdas.add(c);
    }
}
