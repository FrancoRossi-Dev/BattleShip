package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoTipo {
    public static final EstadoTipo PREPARACION = new EstadoTipo("PREPARACION");
    public static final EstadoTipo EN_COMBATE = new EstadoTipo("EN_COMBATE");
    public static final EstadoTipo FINALIZADA_VICTORIA = new EstadoTipo("FINALIZADA_VICTORIA");
    public static final EstadoTipo FINALIZADA_DERROTA = new EstadoTipo("FINALIZADA_DERROTA");

    @Getter
    private String nombre;

    public EstadoTipo(String nombre) {
        this.nombre = nombre;
    }

}
