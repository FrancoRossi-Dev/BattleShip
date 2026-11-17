package ort.da.BattleShip.model;

import lombok.Getter;

// un modo de la táctica "Caza y ataque"; conoce a su contexto para pedirle el cambio de modo
public abstract class ModoCazaYAtaque {

    @Getter
    private TacticaCazaYAtaque tactica;

    public ModoCazaYAtaque(TacticaCazaYAtaque tactica) {
        this.tactica = tactica;
    }

    public abstract String getNombre();

    public abstract Celda elegirCelda(HistorialDisparos historial);

    // según el resultado del último disparo, el modo decide si sigue o cambia
    public abstract void registrarDisparo(Disparo disparo);

}
