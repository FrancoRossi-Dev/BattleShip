package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.TacticaTipo;

// CU02 listaTacticas: la vista solo muestra el nombre; la posición en la lista es el valor elegido
public class TacticaDto {

    @Getter
    private String nombre;

    public TacticaDto(TacticaTipo t) {
        this.nombre = t.getNombre();
    }

    public static List<TacticaDto> fromList(List<TacticaTipo> tacticas) {
        List<TacticaDto> dtoList = new ArrayList<>();
        for (TacticaTipo t : tacticas) {
            dtoList.add(new TacticaDto(t));
        }
        return dtoList;
    }
}
