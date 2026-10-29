package ort.da.BattleShip.model;

import lombok.Getter;

public class TipoBarco {

    @Getter
    private String nombre;
    @Getter
    private int tamano;

    public TipoBarco(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamano = tamano;
    }
}
