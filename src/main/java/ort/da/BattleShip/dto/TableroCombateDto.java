package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Celda;

// CU03: un océano completo durante el combate (o al ver una batalla finalizada).
// Aguas propias: mostrarBarcos=true. Aguas enemigas: mostrarBarcos=false, salvo que la batalla
// se haya perdido (se revelan los barcos no descubiertos).
public class TableroCombateDto {

    @Getter
    private List<CeldaDto> celdas = new ArrayList<>();

    public TableroCombateDto(List<Celda> celdasOceano, boolean mostrarBarcos) {
        for (Celda c : celdasOceano) {
            celdas.add(new CeldaDto(c, mostrarBarcos));
        }
    }
}
