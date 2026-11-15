package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Barco;
import ort.da.BattleShip.model.Celda;

// CU02: solo las celdas del barco recién ubicado, para no redibujar todo el océano en cada clic.
// El redibujo completo (al retomar una batalla) lo hace TableroDespliegueDto.
public class BarcoUbicadoDto {

    @Getter
    private List<CeldaDto> celdas = new ArrayList<>();

    public BarcoUbicadoDto(Barco b) {
        for (Celda c : b.getCeldas()) {
            celdas.add(new CeldaDto(c, true));
        }
    }
}
