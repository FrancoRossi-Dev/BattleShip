package ort.da.BattleShip.dto;

import lombok.Getter;
import ort.da.BattleShip.model.Celda;

// formato que espera BattleShipBoard en mostrarAguasPropias / mostrarAguasEnemigas
public class CeldaDto {

    @Getter
    private int coordenadaVertical;
    @Getter
    private int coordenadaHorizontal;
    @Getter
    private String valor;

    public CeldaDto(Celda c, boolean mostrarBarcos) {
        this.coordenadaVertical = c.getFila();
        this.coordenadaHorizontal = c.getColumna();
        this.valor = c.valor(mostrarBarcos);
    }
}
