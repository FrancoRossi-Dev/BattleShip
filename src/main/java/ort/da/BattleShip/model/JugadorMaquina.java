package ort.da.BattleShip.model;

import lombok.Getter;

public class JugadorMaquina extends Jugador {

    @Getter
    private Tactica tactica;

    public JugadorMaquina(int ancho, int alto, Tactica tactica) {
        super(ancho, alto);
        this.tactica = tactica;
    }
}
