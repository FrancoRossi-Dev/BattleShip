package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Batalla {

    @Getter
    private String lugar;
    @Getter
    private EstadoBatalla estado;

    @Getter
    private Flota flota;

    @Getter
    private JugadorHumano jugadorHumano;
    @Getter
    private JugadorMaquina jugadorMaquina;
    @Getter
    private List<Ronda> rondas = new ArrayList<>();
    @Getter 
    private int ancho, alto;

    private static final int MIN_LADO = 6;
    private static final int MAX_LADO = 12;

    public Batalla(
        String lugar, 
        TacticaTipo tactica, 
        int ancho, 
        int alto, 
        List<TipoBarco> tipos, 
        int[] cantidades) throws BattleException {

        this.flota = new Flota(tipos, cantidades);
        this.ancho = ancho;
        this.alto = alto;
        this.lugar = lugarOAleatorio(lugar);
        this.jugadorHumano = new JugadorHumano(ancho, alto);
        this.jugadorMaquina = new JugadorMaquina(ancho, alto, tactica);
        
        this.estado = EstadoFactory.crearEstadoBatalla(EstadoTipo.PREPARACION, this);

        validar();

        jugadorMaquina.desplegarAleatorio(flota);
    }

    protected void cambiarEstado(EstadoTipo tipo) throws BattleException {
        this.estado = EstadoFactory.crearEstadoBatalla(tipo, this);
    }


    // Preparación
    public void desplegarBarco(int posBarco, int coorX, int coorY, String orientacion) throws BattleException {
        estado.desplegarBarco(posBarco, coorX, coorY, orientacion);
    }


    public void librarBatalla() throws BattleException {
        estado.librarBatalla();
    }

    // En combate
    public void jugarRonda(int coorX, int coorY) throws BattleException {
        estado.jugarRonda(coorX, coorY);
    }

    // la batalla ejecuta los turnos por experto, el estado los llama.
    protected Disparo turnoHumano(Ronda r, int coorX, int coorY) throws BattleException {
        return r.turnoHumano(jugadorMaquina, new Celda(coorY, coorX));
    }

    protected Disparo turnoMaquina(Ronda r) throws BattleException {
        return r.turnoMaquina(jugadorMaquina, jugadorHumano);
    }
    
    public boolean flotaMaquinaHundida() {
        return jugadorMaquina.flotaHundida();
    }

    public boolean flotaHumanaHundida() {
        return jugadorHumano.flotaHundida();
    }

    protected void agregarRonda(Ronda r) {
        rondas.add(r);
    }

    // finalizada
    public boolean isGanada() {
        return estado.getNombre().equals(EstadoFinalizada.GANADA);
    }

    public boolean isPerdida() {
        return estado.getNombre().equals(EstadoFinalizada.PERDIDA);
    }

    // utilidades
    private void validar() throws BattleException {
        validarLado(this.ancho, "horizontales");
        validarLado(this.alto, "verticales");
        validarDensidad();
    }

    private void validarLado(int celdas, String sentido) throws BattleException {
        if (celdas < MIN_LADO || celdas > MAX_LADO)
            throw new BattleException("El océano debe tener entre " + MIN_LADO + " y " + MAX_LADO + " celdas " + sentido);
    }

    // celdas de la flota / celdas del océano < 0.25, sin decimales
    private void validarDensidad() throws BattleException {
        if (flota.tamanoTotal() * 4 >= this.ancho * this.alto)
            throw new BattleException("La flota debe ocupar menos del 25% del océano");
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

    public String getNombreEstado() {
        return estado.getClass().getSimpleName();
    }
}