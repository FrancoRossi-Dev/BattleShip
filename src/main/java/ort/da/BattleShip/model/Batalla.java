package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Batalla {

    @Getter
    private String lugar;
    @Getter
    private EstadoBatalla estado;

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

    private static final int MIN_LADO = 6;
    private static final int MAX_LADO = 12;

    public Batalla(String lugar, TacticaTipo tactica, int ancho, int alto, List<TipoBarco> tipos, int[] cantidades)
            throws BattleException {
        validarLado(ancho, "horizontales");
        validarLado(alto, "verticales");
        this.flota = new Flota(tipos, cantidades);
        validarDensidad(ancho, alto);

        this.lugar = lugarOAleatorio(lugar);
        this.estado = EstadoFactory.crearEstadoBatalla(EstadoTipo.PREPARACION, this);
        this.pendienteAsignar = new Flota(tipos, cantidades);
        this.jugadorHumano = new JugadorHumano(ancho, alto);
        this.jugadorMaquina = new JugadorMaquina(ancho, alto, tactica);
        jugadorMaquina.desplegarAleatorio(flota);
    }

    private void validarLado(int celdas, String sentido) throws BattleException {
        if (celdas < MIN_LADO || celdas > MAX_LADO)
            throw new BattleException("El océano debe tener entre " + MIN_LADO + " y " + MAX_LADO + " celdas " + sentido);
    }

    // celdas de la flota / celdas del océano < 0.25, sin decimales
    private void validarDensidad(int ancho, int alto) throws BattleException {
        if (flota.tamanoTotal() * 4 >= ancho * alto)
            throw new BattleException("La flota debe ocupar menos del 25% del océano");
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
        return estado.getResultado().equals("Ganada");
    }

    public boolean isPerdida() {
        return estado.getResultado().equals("Perdida");
    }

    public void jugarRonda(int coorX, int coorY) throws BattleException {
        Ronda r = new Ronda();

        verificarGanador(r.turnoHumano(jugadorMaquina, new Celda(coorY, coorX)));
        if (!isGanada()) verificarGanador(r.turnoMaquina(jugadorMaquina, jugadorHumano));

        rondas.add(r);
    }

    private void verificarGanador(Disparo d) {
        if (!d.hundioBarco()) return;
        this.estado.verificarGanador();
    }

    public void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        if (flotaDesplegada()) throw new BattleException("Todos los barcos ya fueron posicionados");
        TipoBarco tb = pendienteAsignar.getBarcos().get(0).getTipo();
        jugadorHumano.desplegarBarco(tb, coorX, coorY, orientacion);
        pendienteAsignar.remover(tb);
    }

    public void librarBatalla() throws BattleException {
        if (!estado.getNombre().equals("En preparación")) throw new BattleException("La batalla ya comenzó");
        if (!flotaDesplegada()) throw new BattleException("Debe posicionar todos sus barcos primero");
        this.estado = new Estado("En combate");
    }

    public boolean flotaDesplegada() {
        return pendienteAsignar.getBarcos().isEmpty();
    }

    public boolean derrota() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'derrota'");
    }

    public boolean victoria() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'victoria'");
    }

}
