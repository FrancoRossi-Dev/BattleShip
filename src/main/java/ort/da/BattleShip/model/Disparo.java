package ort.da.BattleShip.model;

import lombok.Getter;

public class Disparo {

    @Getter
    private Celda celda;

    // se fija una sola vez, al registrar el disparo: solo el disparo que hunde el barco lo marca.
    // (antes se calculaba con el estado actual del barco, y daba true para todos sus impactos)
    private boolean hundio = false;

    public Disparo(Celda celda) {
        this.celda = celda;
    }

    // la celda lo llama apenas queda marcada como disparada
    protected void registrarResultado() {
        this.hundio = isImpacto() && !celda.getBarco().isVivo();
    }

    public boolean isImpacto() {
        return celda.getBarco() != null;
    }

    public boolean hundioBarco() {
        return hundio;
    }

}
