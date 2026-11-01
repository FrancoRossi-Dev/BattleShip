package ort.da.BattleShip.model;

import lombok.Getter;

public abstract class Jugador {

    @Getter
    private Oceano oceano;

    public Jugador(int ancho, int alto) {
        this.oceano = new Oceano(ancho, alto);
    }

    public int partesSanas(Flota flota) {
        return flota.tamanoTotal() - oceano.partesImpactadas();
    }


    public Disparo recibirDisparo(Celda o) throws BattleException {
        return oceano.recibirDisparo(o);
    };
}
