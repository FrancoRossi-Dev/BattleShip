package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.observador.Observable;

public class Batalla extends Observable {

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

    public enum Eventos {
        CAMBIO_ESTADO,
        BARCO_DESPLEGADO, 
        RONDA_FINALIZADA, 
    }

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
        avisar(Batalla.Eventos.CAMBIO_ESTADO);
    }


    // Preparación
    // siempre se ubica el primer barco pendiente (el más grande)
    public void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        estado.desplegarBarco(coorX, coorY, orientacion);
        avisar(Batalla.Eventos.BARCO_DESPLEGADO);
    }


    public void librarBatalla() throws BattleException {
        estado.librarBatalla();
    }

    // synchronized: si dos sesiones disparan a la vez sobre la misma batalla, la ronda es atómica
    // y gana el primer disparo (el segundo encuentra la celda ya disparada o la batalla finalizada)
    public synchronized void jugarRonda(int coorX, int coorY) throws BattleException {
        estado.jugarRonda(coorX, coorY);
    }

    // llamado dsde el estado
    protected void hacerJugarRonda(int coorX, int coorY) throws BattleException {
        Ronda r = new Ronda();

        hacerTurnoHumano(r, coorX, coorY);
        if (!flotaMaquinaHundida()) {
            hacerTurnoMaquina(r);
        }
        agregarRonda(r);
        avisar(Batalla.Eventos.RONDA_FINALIZADA);
    }

    public boolean flotaDesplegada() {
        return estado.flotaDesplegada();
    }

    // barcos que faltan ubicar, de mayor a menor; vacía fuera de preparación
    public List<Barco> barcosPendientes() {
        return estado.barcosPendientes();
    }

    public boolean enPreparacion() {
        return estado.enPreparacion();
    }

    public Ronda ultimaRonda() {
        if (rondas.isEmpty()) return null;
        return rondas.getLast();
    }

    public List<Celda> celdasPropias() {
        return jugadorHumano.getCeldas();
    }

    // quien las muestre decide si oculta los barcos no descubiertos (Celda.valor(mostrarBarcos))
    public List<Celda> celdasEnemigas() {
        return jugadorMaquina.getCeldas();
    }

    // barcos enemigos hundidos, en el orden en que se hundieron
    public List<Barco> bajasEnemigas() {
        return jugadorMaquina.getBarcosHundidos();
    }

    // empieza en 1 y suma cada vez que le toca al humano; finalizada, queda en la última jugada
    public int numeroRonda() {
        if (esFinalizada()) return rondas.size();
        return rondas.size() + 1;
    }

    // la batalla ejecuta los turnos por experto, el estado los llama.
    protected Disparo hacerTurnoHumano(Ronda r, int coorX, int coorY) throws BattleException {
        return r.turnoHumano(jugadorMaquina, new Celda(coorY, coorX));
    }

    protected Disparo hacerTurnoMaquina(Ronda r) throws BattleException {
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

    public String nombreEstado() {
        return estado.getNombre();
    }

    public int tamanoFlota() {
        return flota.tamanoTotal();
    }

    // integridad: la flota se cuenta completa aunque todavía no esté desplegada (en preparación)
    public int partesSanasPropias() {
        return tamanoFlota() - jugadorHumano.partesImpactadas();
    }

    public int partesSanasEnemigas() {
        return tamanoFlota() - jugadorMaquina.partesImpactadas();
    }

    // solo una batalla perdida revela los barcos enemigos no descubiertos
    public boolean revelaBarcosEnemigos() {
        return estado.revelaBarcosEnemigos();
    }

    public boolean esFinalizada() {
        return this.estado.esFinalizada();
    }

    public Barco getUltimoBarcoColocado() {
        return this.jugadorHumano.getUltimoBarcoDesplegado();
    }
}