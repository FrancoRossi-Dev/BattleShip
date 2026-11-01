package ort.da.BattleShip.model;

import lombok.Getter;

public class Disparo {
    
    @Getter 
    private Celda celda;

    public Disparo(Celda celda) {
        this.celda = celda;
    }

    public boolean isImpacto() {
        return celda.getBarco() != null;
    }

    public boolean hundioBarco() {
        return isImpacto() && !celda.getBarco().isVivo();
    }

}
