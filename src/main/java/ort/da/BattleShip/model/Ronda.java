package ort.da.BattleShip.model;

import lombok.Getter;

public class Ronda {

    @Getter
    private Disparo disparoHumano;
    @Getter
    private Disparo disparoMaquina;

    public Disparo turnoHumano(Jugador humano, Jugador rival, Celda indicada) throws BattleException {
        disparoHumano = humano.disparar(rival, indicada);
        return disparoHumano;
    }

    // en el turno de la máquina el usuario no indica celda: la elige ella
    public Disparo turnoMaquina(Jugador maquina, Jugador rival) throws BattleException {
        disparoMaquina = maquina.disparar(rival, null);
        return disparoMaquina;
    }

}
