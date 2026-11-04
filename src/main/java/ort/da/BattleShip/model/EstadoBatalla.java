package ort.da.BattleShip.model;

import lombok.Getter;

public abstract class EstadoBatalla {
    @Getter
    private Batalla batalla;

    public EstadoBatalla(Batalla b) {
        this.batalla = b;
    }

    public abstract String getNombre();

    public abstract String getResultado();

    protected abstract void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException;

    protected abstract void librarBatalla() throws BattleException;

    protected abstract void jugarRonda(int coorX, int coorY) throws BattleException;

}
