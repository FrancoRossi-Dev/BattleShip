package ort.da.BattleShip.model;

import lombok.Getter;

public class Tactica {

    @Getter
    private String nombre;

    public Tactica(String nombre) {
        this.nombre = nombre;
    }
}
