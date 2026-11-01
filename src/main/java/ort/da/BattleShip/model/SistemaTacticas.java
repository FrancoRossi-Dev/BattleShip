package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SistemaTacticas {

    @Getter
    private List<TacticaTipo> TiposDeTacticas = new ArrayList<>();

    public void agregar(TacticaTipo tactica) {
        TiposDeTacticas.add(tactica);
    }
}
