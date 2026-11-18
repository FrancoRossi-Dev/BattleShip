package ort.da.BattleShip.model;

import java.util.List;

import lombok.Getter;

public abstract class Jugador {

    @Getter
    private Oceano oceano;

    public Jugador(int ancho, int alto) {
        this.oceano = new Oceano(ancho, alto);
    }

    // polimórfico: el humano dispara a la celda que indicó el usuario;
    // la máquina no recibe celda (llega null) y la elige con su táctica
    public abstract Disparo disparar(Jugador rival, Celda indicada) throws BattleException;

    public int partesImpactadas() {
        return oceano.partesImpactadas();
    }


    public List<Celda> getCeldas() {
        return oceano.getCeldas();
    }

    public List<Barco> getBarcosHundidos() {
        return oceano.getBarcosHundidos();
    }

    public boolean flotaHundida() {
        return oceano.flotaHundida();
    }

    public Disparo recibirDisparo(Celda o) throws BattleException {
        return oceano.recibirDisparo(o);
    }

    public void desplegarBarco(TipoBarco b, int coorX, int coorY, String orientacion) throws BattleException {
        oceano.desplegarBarco(b, coorX, coorY, orientacion);
    }

    public Barco getUltimoBarcoDesplegado() {
        return oceano.getUltimoBarcoDesplegado();
    }


}
