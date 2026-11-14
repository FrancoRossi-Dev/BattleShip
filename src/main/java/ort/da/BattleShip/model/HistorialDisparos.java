package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class HistorialDisparos {

    @Getter
    private int ancho;
    @Getter
    private int alto;
    @Getter
    private List<Disparo> disparos = new ArrayList<>();

    public HistorialDisparos(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    public void agregar(Disparo d) {
        disparos.add(d);
    }

    public int cantidad() {
        return disparos.size();
    }

    public boolean estaCompleto() {
        return cantidad() >= ancho * alto;
    }

    public boolean estaLibre(int fila, int columna) {
        boolean dentroDelOceano = fila >= 0 && fila < alto && columna >= 0 && columna < ancho;
        return dentroDelOceano && !fueDisparada(fila, columna);
    }

    private boolean fueDisparada(int fila, int columna) {
        for (Disparo d : disparos) {
            if (d.getCelda().estaEn(fila, columna)) return true;
        }
        return false;
    }

    // resta el océano completo menos las celdas ya disparadas
    public List<Celda> celdasLibres() {
        // 1. matriz de marcas: true donde ya se disparó (una sola pasada por los disparos)
        boolean[][] disparadas = new boolean[alto][ancho];
        for (Disparo d : disparos) {
            Celda c = d.getCelda();
            disparadas[c.getFila()][c.getColumna()] = true;
        }

        // 2. se recorre el océano completo y se guardan las celdas sin marca
        // se crean celdas nuevas (sin barco) para no exponer el océano enemigo
        List<Celda> libres = new ArrayList<>();
        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                if (!disparadas[fila][columna]) libres.add(new Celda(fila, columna));
            }
        }
        return libres;
    }

}
