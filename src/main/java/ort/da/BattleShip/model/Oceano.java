package ort.da.BattleShip.model;

import lombok.Getter;

public class Oceano {

    @Getter
    private int ancho;
    @Getter
    private int alto;

    private Celda[][] celdas;
    // cambio a matriz para busqueda directa;

    private int impactos = 0;

    public Oceano(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Celda[alto][ancho];
        for(int fila = 0; fila < alto; fila++) {
            for(int columna = 0; columna < ancho; columna++){
                celdas[fila][columna] = new Celda(fila, columna);
            }
        }
    }

    // esto puede ser un contador no? pero es eficiencia boluda
    // public int partesImpactadas() {
    //     int impactadas = 0;
    //     for (Celda[] fila : celdas) {
    //         for(Celda c : fila) {
    //             if (c.getBarco() != null && c.isDisparada()) {
    //                 impactadas++;
    //         }
    //         }
    //     }
    //     return impactadas;
    // }

    public int partesImpactadas() {
        return impactos;
    }

    public Celda getCelda(int coorX, int coorY) throws BattleException {
        if(coorX >= ancho || coorX < 0 || coorY >= alto || coorY < 0) 
            throw new BattleException("Disparo fuera de tablero");
        
        return celdas[coorY][coorX];
    }

    public Disparo recibirDisparo(Celda o) throws BattleException {
        Celda objetivo = getCelda(o.getColumna(), o.getFila());
        Disparo d = objetivo.recibirDisparo();
        if(d.isImpacto()) impactos++;
        return d;
    }
}
