package ort.da.BattleShip.dto;

import lombok.Getter;
import ort.da.BattleShip.model.Ronda;

// los dos disparos de la ronda juntos: la vista los dibuja en el mismo comando (ronda atómica)
public class RondaDto {

    @Getter
    private CeldaDto disparoHumano;
    // null si el humano hundió la flota enemiga (la máquina no llega a disparar)
    @Getter
    private CeldaDto disparoMaquina;

    public RondaDto(Ronda r) {
        this.disparoHumano = new CeldaDto(r.getDisparoHumano().getCelda(), false);
        if (r.getDisparoMaquina() != null) {
            this.disparoMaquina = new CeldaDto(r.getDisparoMaquina().getCelda(), true);
        }
    }
}
