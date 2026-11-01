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

    public Batalla(String lugar, TacticaTipo tactica, int ancho, int alto, List<TipoBarco> composicion) {
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

    public void jugarRonda(int coorX, int coorY) throws BattleException {
        Ronda r = new Ronda();

        // resuelve el turno humano
        Celda objetivo = new Celda(coorY, coorX);
        Disparo d1 = resolverDisparo(jugadorMaquina, objetivo);
        if (d1.hundioBarco()) verificarGanador();
        r.setDisparoHumano(d1);
        
        if (!isGanada()) {   
            // resuelve el turno maquina
            Celda objetivoMaquina = jugadorMaquina.elegirObjetivo();
            Disparo d2 = resolverDisparo(jugadorHumano, objetivoMaquina);
            jugadorMaquina.agregarAlHistorial(d2);

            if (d2.hundioBarco()) verificarGanador();
            r.setDisparoMaquina(d2);
        }

        rondas.add(r);
    }

    private Disparo resolverDisparo(Jugador j, Celda o) throws BattleException {
        return j.recibirDisparo(o);
    }

    private void verificarGanador() {
        // cambiar a patron state
        if (jugadorHumano.partesSanas(flota) == 0) this.estado = new Estado("Perdida");
        if (jugadorMaquina.partesSanas(flota) == 0) this.estado = new Estado("Ganada");
    }

}
