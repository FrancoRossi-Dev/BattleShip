package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Batalla {

    private Estado estado;
    private Flota flota;
    private JugadorHumano jugadorHumano;
    private JugadorMaquina jugadorMaquina;
    private final List<Ronda> rondas = new ArrayList<>();

    @Getter 
    private String ganador;

    public Estado getEstado() {
        return estado;
    }

    public Flota getFlota() {
        return flota;
    }

    public JugadorHumano getJugadorHumano() {
        return jugadorHumano;
    }

    public JugadorMaquina getJugadorMaquina() {
        return jugadorMaquina;
    }

    public List<Ronda> getRondas() {
        return rondas;
    }

    public String GetLugarCombate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetLugarCombate'");
    }
}
