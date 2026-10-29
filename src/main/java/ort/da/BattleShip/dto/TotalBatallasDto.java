package ort.da.BattleShip.dto;

import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;

public class TotalBatallasDto {

    @Getter
    private List<BatallaDto> batallas;
    @Getter
    private int ganadas;
    @Getter
    private int perdidas;
    @Getter
    private int porcentajeExito;

    public TotalBatallasDto(List<Batalla> batallas, int ganadas, int perdidas, int porcentajeExito) {
        this.batallas = BatallaDto.fromList(batallas);
        this.ganadas = ganadas;
        this.perdidas = perdidas;
        this.porcentajeExito = porcentajeExito;
    }
}
