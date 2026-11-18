package ort.da.BattleShip.model;

public class JugadorHumano extends Jugador {

    public JugadorHumano(int ancho, int alto) {
        super(ancho, alto);
    }

    // dispara a la celda que eligió el usuario
    @Override
    public Disparo disparar(Jugador rival, Celda indicada) throws BattleException {
        if (indicada == null) throw new BattleException("Debe indicar una celda para disparar");
        return rival.recibirDisparo(indicada);
    }

}
