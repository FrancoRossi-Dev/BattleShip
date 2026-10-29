package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Batalla {

    @Getter
    private String lugar;
    @Getter
    private Estado estado;
    @Getter
    private Flota flota;
    @Getter
    private JugadorHumano jugadorHumano;
    @Getter
    private JugadorMaquina jugadorMaquina;
    @Getter
    private List<Ronda> rondas = new ArrayList<>();

    public Batalla(String lugar, Tactica tactica, int ancho, int alto, List<TipoBarco> composicion) {
        this.lugar = lugarOAleatorio(lugar);
        this.estado = new Estado("En preparación");
        this.flota = new Flota(composicion);
        this.jugadorHumano = new JugadorHumano(ancho, alto);
        this.jugadorMaquina = new JugadorMaquina(ancho, alto, tactica);
    }

    public int getAncho() {
        return jugadorHumano.getOceano().getAncho();
    }

    public int getAlto() {
        return jugadorHumano.getOceano().getAlto();
    }

    private String lugarOAleatorio(String lugar) {
        if (lugar != null && !lugar.isBlank()) {
            return lugar;
        }
        String aleatorio = "";
        for (int i = 0; i < 6; i++) {
            aleatorio += (char) ('A' + (int) (Math.random() * 26));
        }
        return aleatorio;
    }

    public boolean isGanada() {
        return estado.getNombre().equals("Ganada");
    }

    public boolean isPerdida() {
        return estado.getNombre().equals("Perdida");
    }
}
