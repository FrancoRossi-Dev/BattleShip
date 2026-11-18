package ort.da.BattleShip.model;

import lombok.Getter;


public abstract class ModoCazaYAtaque {

    @Getter
    private TacticaCazaYAtaque tactica;

    public ModoCazaYAtaque(TacticaCazaYAtaque tactica) {
        this.tactica = tactica;
    }

    public abstract String getNombre();

    public abstract Celda elegirCelda(HistorialDisparos historial);

    public abstract void registrarDisparo(Disparo disparo);

}
