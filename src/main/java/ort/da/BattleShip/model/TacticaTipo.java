package ort.da.BattleShip.model;

import lombok.Getter;

public class TacticaTipo {
    public static final String SECUENCIAL = "Secuencial";
    public static final String ESPIRAL = "Espiral";
    public static final String CAZA_Y_ATAQUE = "Caza y ataque";

    @Getter
    private String nombre;

    public TacticaTipo(String s) {
        this.nombre = s;
    }
}
