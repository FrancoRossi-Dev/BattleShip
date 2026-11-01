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
        return tactica.ElegirCelda(historial);
    }

    public void agregarAlHistorial(Disparo d) throws BattleException {
        if (d == null) throw new BattleException("Disparo invalido");
        historial.agregar(d);
    }

}
