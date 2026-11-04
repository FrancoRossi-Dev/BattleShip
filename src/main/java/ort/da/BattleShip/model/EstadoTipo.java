package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoTipo {
    public static final EstadoTipo PREPARACION = new EstadoTipo("PREPARACION");
    public static final EstadoTipo LISTA = new EstadoTipo("LISTA");
    public static final EstadoTipo FINALIZADA = new EstadoTipo("FINALIZADA");
    
    @Getter 
    private String nombre;

    public EstadoTipo(String nombre) {
        this.nombre = nombre;
    }
    
}
