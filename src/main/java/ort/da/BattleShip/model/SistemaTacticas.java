package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SistemaTacticas {

    @Getter
    private List<Tactica> tacticas = new ArrayList<>();

    public void agregar(Tactica tactica) {
        tacticas.add(tactica);
    }
}
