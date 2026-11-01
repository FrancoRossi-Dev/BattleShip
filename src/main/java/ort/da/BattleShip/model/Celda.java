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

    public boolean estaEn(int fila, int columna) {
        return this.fila == fila && this.columna == columna;
    }

    public Disparo recibirDisparo() throws BattleException {
        if(isDisparada()) throw new BattleException("La celda objetivo ya fue disparada");
        this.disparo = new Disparo(this);
        return disparo;
    }

    public void setBarco(Barco b) throws BattleException {
        if(this.barco != null) throw new BattleException("La celda ya está ocupada");
        this.barco = b;
    }
}
