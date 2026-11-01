package ort.da.BattleShip.model;

import lombok.Getter;

public class TacticaTipo {
    @Getter 
    private String nombre;

    public TacticaTipo(String s) {
        this.nombre = s;
    }
}
