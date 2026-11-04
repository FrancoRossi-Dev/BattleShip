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
        this.pendienteAsignar = new Flota(tipos, cantidades);
        this.jugadorHumano = new JugadorHumano(ancho, alto);
        this.jugadorMaquina = new JugadorMaquina(ancho, alto, tactica);
        
        this.estado = EstadoFactory.crearEstadoBatalla(EstadoTipo.PREPARACION, this);

        jugadorMaquina.desplegarAleatorio(flota);

        validar();
    }

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

    public void cambiarEstado(EstadoTipo tipo) throws BattleException {
        this.estado = EstadoFactory.crearEstadoBatalla(tipo, this);
    }

    public boolean isGanada() {
        return estado.getResultado().equals(EstadoFinalizada.VICTORIA);
    }

    public boolean isPerdida() {
        return estado.getResultado().equals(EstadoFinalizada.DERROTA);
    }

    // operaciones dependientes del estado: se delegan
    public void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        estado.desplegarBarco(coorX, coorY, orientacion);
    }

    public void librarBatalla() throws BattleException {
        estado.librarBatalla();
    }

    public void jugarRonda(int coorX, int coorY) throws BattleException {
        estado.jugarRonda(coorX, coorY);
    }

    // trabajo real, invocado por los estados
    protected void hacerDesplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        if (flotaDesplegada()) throw new BattleException("Todos los barcos ya fueron posicionados");
        TipoBarco tb = pendienteAsignar.getBarcos().get(0).getTipo();
        jugadorHumano.desplegarBarco(tb, coorX, coorY, orientacion);
        pendienteAsignar.remover(tb);
    }

    protected Disparo hacerTurnoHumano(Ronda r, int coorX, int coorY) throws BattleException {
        return r.turnoHumano(jugadorMaquina, new Celda(coorY, coorX));
    }

    protected Disparo hacerTurnoMaquina(Ronda r) throws BattleException {
        return r.turnoMaquina(jugadorMaquina, jugadorHumano);
    }

    protected void agregarRonda(Ronda r) {
        rondas.add(r);
    }

    public boolean flotaDesplegada() {
        return pendienteAsignar.getBarcos().isEmpty();
    }

    public boolean flotaMaquinaHundida() {
        return jugadorMaquina.flotaHundida();
    }

    public boolean flotaHumanaHundida() {
        return jugadorHumano.flotaHundida();
    }

    public String getNombreEstado() {
        return estado.getClass().getSimpleName();
    }
}
