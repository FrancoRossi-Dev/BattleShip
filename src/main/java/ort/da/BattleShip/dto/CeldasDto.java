package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Celda;

// celdas de un océano (completo o solo las de un barco), en el formato que dibuja BattleShipBoard
// mostrarBarcos=false oculta los barcos no impactados (aguas enemigas)
public class CeldasDto {

    @Getter
    private List<CeldaDto> celdas = new ArrayList<>();

    public CeldasDto(List<Celda> celdasOceano, boolean mostrarBarcos) {
        for (Celda c : celdasOceano) {
            celdas.add(new CeldaDto(c, mostrarBarcos));
        }
    }
}
