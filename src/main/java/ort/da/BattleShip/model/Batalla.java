package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Batalla {

    @Getter
    private String lugar;
    @Getter
    private Estado estado;

    // mover al estado
    @Getter
    private Flota pendienteAsignar;

    @Getter
    private Flota flota;
    @Getter
    private JugadorHumano jugadorHumano;
    @Getter
    private JugadorMaquina jugadorMaquina;
    @Getter
    private List<Ronda> rondas = new ArrayList<>();

    public Batalla(String lugar, TacticaTipo tactica, int ancho, int alto, List<TipoBarco> composicion) {
        this.lugar = lugarOAleatorio(lugar);
        this.estado = new Estado("En preparación");
        this.flota = new Flota(composicion);
        this.pendienteAsignar = new Flota(composicion);
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

    public void jugarRonda(int coorX, int coorY) throws BattleException {
        Ronda r = new Ronda();

        verificarGanador(r.turnoHumano(jugadorMaquina, new Celda(coorY, coorX)));
        if (!isGanada()) verificarGanador(r.turnoMaquina(jugadorMaquina, jugadorHumano));

        rondas.add(r);
    }

    private void verificarGanador(Disparo d) {
        if (!d.hundioBarco()) return;
        // cambiar a patron state
        if (jugadorHumano.partesSanas(flota) == 0) this.estado = new Estado("Perdida");
        if (jugadorMaquina.partesSanas(flota) == 0) this.estado = new Estado("Ganada");
    }

    public void desplegarBarco(Jugador j, TipoBarco tb, int coorX, int coorY, String orientacion) throws BattleException {
        if (!pendienteAsignar.contiene(tb)) throw new BattleException("El barco no esta disponible");
        j.desplegarBarco(tb, coorX, coorY, orientacion);
        pendienteAsignar.remover(tb);
    }

    public boolean flotaDesplegada() {
        return pendienteAsignar.getBarcos().isEmpty();
    }

}
