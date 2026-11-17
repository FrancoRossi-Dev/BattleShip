package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Barco;
import ort.da.BattleShip.model.TipoBarco;

// CU02 listaTiposBarco y barcosPendientes: la vista usa nombre y tamaño en los dos casos
public class TipoBarcoDto {

    @Getter
    private String nombre;
    @Getter
    private int tamano;

    public TipoBarcoDto(TipoBarco t) {
        this.nombre = t.getNombre();
        this.tamano = t.getTamano();
    }

    public TipoBarcoDto(Barco b) {
        this.nombre = b.getNombre();
        this.tamano = b.getTamano();
    }

    public static List<TipoBarcoDto> fromTipos(List<TipoBarco> tipos) {
        List<TipoBarcoDto> dtoList = new ArrayList<>();
        for (TipoBarco t : tipos) {
            dtoList.add(new TipoBarcoDto(t));
        }
        return dtoList;
    }

    public static List<TipoBarcoDto> fromBarcos(List<Barco> barcos) {
        List<TipoBarcoDto> dtoList = new ArrayList<>();
        for (Barco b : barcos) {
            dtoList.add(new TipoBarcoDto(b));
        }
        return dtoList;
    }
}
