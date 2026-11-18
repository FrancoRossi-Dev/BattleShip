package ort.da.BattleShip.model;

import lombok.Getter;

public class Disparo {

    @Getter
    private Celda celda;

    // se fija una sola vez, al registrar el disparo: solo el disparo que hunde el barco lo marca
    private boolean hundio = false;

    public Disparo(Celda celda) {
        this.celda = celda;
    }

    // la celda lo llama apenas queda marcada como disparada
    protected void registrarResultado() {
        this.hundio = isImpacto() && !celda.tieneBarcoVivo();
    }

    public boolean isImpacto() {
        return celda.getBarco() != null;
    }

    // impacto en un barco que sigue a flote
    public boolean isImpactoSinHundir() {
        return celda.tieneBarcoVivo();
    }

    public boolean hundioBarco() {
        return hundio;
    }

}
