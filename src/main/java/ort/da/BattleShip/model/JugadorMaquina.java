package ort.da.BattleShip.model;

import lombok.Getter;

public class JugadorMaquina extends Jugador {

    @Getter
    private TacticaInterfaz tactica;

    public JugadorMaquina(int ancho, int alto, TacticaTipo tactica) {
        super(ancho, alto);
        this.tactica = TacticaFactory.crearTactica(tactica);
    }

    public Celda elegirObjetivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'elegirObjetivo'");
    }


}
