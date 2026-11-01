package ort.da.BattleShip.model;

import lombok.Getter;

public class Ronda {

    @Getter
    private Disparo disparoHumano;
    @Getter
    private Disparo disparoMaquina;

    public Disparo turnoHumano(Jugador rival, Celda objetivo) throws BattleException {
        disparoHumano = rival.recibirDisparo(objetivo);
        return disparoHumano;
    }

    public Disparo turnoMaquina(JugadorMaquina maquina, Jugador rival) throws BattleException {
        disparoMaquina = rival.recibirDisparo(maquina.elegirObjetivo());
        maquina.agregarAlHistorial(disparoMaquina);
        return disparoMaquina;
    }

}
