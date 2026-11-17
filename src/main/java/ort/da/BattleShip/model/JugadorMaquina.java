package ort.da.BattleShip.model;

import lombok.Getter;

public class JugadorMaquina extends Jugador {

    @Getter
    private TacticaInterfaz tactica;

    private HistorialDisparos historial;

    public JugadorMaquina(int ancho, int alto, TacticaTipo tactica) {
        super(ancho, alto);
        this.tactica = TacticaFactory.crearTactica(tactica);
        this.historial = new HistorialDisparos(ancho, alto);
    }

    public Celda elegirObjetivo() throws BattleException {
        if (historial.estaCompleto()) throw new BattleException("No quedan celdas libres para disparar");
        return tactica.elegirCelda(historial);
    }

    private static final int MAX_DESPLIEGUES = 100;
    private static final int MAX_INTENTOS_POR_BARCO = 200;

    // si un barco ya no entra, los anteriores lo bloquearon: se empieza de nuevo con el océano vacío
    public void desplegarAleatorio(Flota flota) throws BattleException {
        for (int despliegue = 0; despliegue < MAX_DESPLIEGUES; despliegue++) {
            if (ubicarTodos(flota)) return;
            vaciarOceano();
        }
        throw new BattleException("No se pudo desplegar la flota enemiga");
    }

    private boolean ubicarTodos(Flota flota) {
        for (Barco b : flota.getBarcos()) {
            if (!ubicarAleatorio(b.getTipo())) return false;
        }
        return true;
    }

    private boolean ubicarAleatorio(TipoBarco tb) {
        for (int intento = 0; intento < MAX_INTENTOS_POR_BARCO; intento++) {
            int coorX = (int) (Math.random() * getOceano().getAncho());
            int coorY = (int) (Math.random() * getOceano().getAlto());
            String orientacion = "HORIZONTAL";
            if (Math.random() < 0.5) orientacion = "VERTICAL";
            try {
                desplegarBarco(tb, coorX, coorY, orientacion);
                return true;
            } catch (BattleException e) {
                // posición inválida (fuera o tocando otro barco): se prueba otra
            }
        }
        return false;
    }

    public void agregarAlHistorial(Disparo d) throws BattleException {
        if (d == null) throw new BattleException("Disparo invalido");
        historial.agregar(d);
        // la táctica es la misma toda la batalla; si tiene modos (caza y ataque), ella decide si cambia
        tactica.registrarDisparo(d);
    }

}
