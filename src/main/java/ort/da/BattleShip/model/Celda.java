package ort.da.BattleShip.model;

import lombok.Getter;

public class Celda {

    @Getter
    private int fila;
    @Getter
    private int columna;
    @Getter
    private Barco barco;
    @Getter
    private Disparo disparo;

    public Celda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public boolean isDisparada() {
        return disparo != null;
    }
}
