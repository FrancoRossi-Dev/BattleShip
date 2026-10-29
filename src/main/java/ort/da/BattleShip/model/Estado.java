package ort.da.BattleShip.model;

import lombok.Getter;

public class Estado {

    @Getter
    private String nombre;

    public Estado(String nombre) {
        this.nombre = nombre;
    }
}
