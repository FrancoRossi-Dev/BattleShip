package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Oceano {

    @Getter
    private int ancho;
    @Getter
    private int alto;
    @Getter
    private List<Celda> celdas = new ArrayList<>();

    public Oceano(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                celdas.add(new Celda(fila, columna));
            }
        }
    }

    public int partesImpactadas() {
        int impactadas = 0;
        for (Celda c : celdas) {
            if (c.getBarco() != null && c.isDisparada()) {
                impactadas++;
            }
        }
        return impactadas;
    }
}
