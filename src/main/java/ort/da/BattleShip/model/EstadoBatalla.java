package ort.da.BattleShip.model;

import lombok.Getter;

public abstract class EstadoBatalla {
    @Getter 
    private Batalla batalla;

    public EstadoBatalla(Batalla b) {
        this.batalla = b;
    }

    public abstract EstadoBatalla cambiarEstado() throws BattleException;

    public abstract String getResultado();

    protected abstract void derrota() throws BattleException;

    protected abstract void victoria() throws BattleException;

    protected abstract void verificarGanador() throws BattleException;

}
