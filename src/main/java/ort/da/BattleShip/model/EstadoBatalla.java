package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public abstract class EstadoBatalla {
    @Getter
    private Batalla batalla;

    @Getter 
    private String resultado = null;

    public EstadoBatalla(Batalla b) {
        this.batalla = b;
    }

    public abstract String getNombre();

    protected abstract void desplegarBarco(int posBarco, int coorX, int coorY, String orientacion) throws BattleException;

    protected abstract void librarBatalla() throws BattleException;

    protected abstract void jugarRonda(int coorX, int coorY) throws BattleException;

    protected abstract boolean flotaDesplegada();

    // solo tiene sentido en preparación; el resto de los estados no tiene barcos pendientes
    protected Barco proximoBarco() {
        return null;
    }

    protected List<Barco> barcosPendientes() {
        return new ArrayList<>();
    }

    // CU01 "Entrar": en preparación se continúa el despliegue (CU02), en el resto se libra o se ve (CU03)
    protected boolean enPreparacion() {
        return false;
    }

    protected abstract boolean esFinalizada();

}
