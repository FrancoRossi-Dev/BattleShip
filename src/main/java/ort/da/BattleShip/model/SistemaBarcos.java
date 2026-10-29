package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SistemaBarcos {

    @Getter
    private List<TipoBarco> tiposBarco = new ArrayList<>();

    public void agregar(TipoBarco tipo) {
        tiposBarco.add(tipo);
    }
}
