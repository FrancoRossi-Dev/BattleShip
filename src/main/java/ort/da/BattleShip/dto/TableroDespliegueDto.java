package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.Celda;

// CU02: aguas propias durante el despliegue de la flota, el jugador ve sus propios barcos.
// CU03 usa su propio dto (aguas propias + enemigas con barcos ocultos, impactos, etc.)
public class TableroDespliegueDto {

    @Getter
    private List<CeldaDto> celdas = new ArrayList<>();

    public TableroDespliegueDto(Batalla b) {
        for (Celda c : b.celdasPropias()) {
            celdas.add(new CeldaDto(c, true));
        }
    }
}
