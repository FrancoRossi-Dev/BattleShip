package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Flota {

    @Getter
    private List<Barco> barcos = new ArrayList<>();

    public Flota(List<TipoBarco> composicion) {
        for (TipoBarco tipo : composicion) {
            barcos.add(new Barco(tipo));
        }
    }

    public int tamanoTotal() {
        int total = 0;
        for (Barco b : barcos) {
            total += b.getTipo().getTamano();
        }
        return total;
    }

    public boolean contiene(TipoBarco tb) {
        for (Barco b : barcos) {
            if (b.getTipo().equals(tb)) return true;
        }
        return false;
    }

    public void remover(TipoBarco tb) {
        for (Barco b : barcos) {
            if (b.getTipo().equals(tb)) {
                barcos.remove(b);
                return;
            }
        }
    }

}
