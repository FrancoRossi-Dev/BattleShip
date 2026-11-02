package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SistemaBatalla {

    @Getter
    private List<Batalla> batallas = new ArrayList<>();

    public void agregar(Batalla batalla) throws BattleException {
        for (Batalla b : batallas) {
            if (b.getLugar().equals(batalla.getLugar())) {
                throw new BattleException("No se puede librar más de una batalla en el mismo lugar, cambie de nombre");
            }
        }
        batallas.add(batalla);
    }

    public int cantidadGanadas() {
        int ganadas = 0;
        for (Batalla b : batallas) {
            if (b.isGanada()) {
                ganadas++;
            }
        }
        return ganadas;
    }

    public int cantidadPerdidas() {
        int perdidas = 0;
        for (Batalla b : batallas) {
            if (b.isPerdida()) {
                perdidas++;
            }
        }
        return perdidas;
    }

    public int porcentajeExito() {
        if (batallas.isEmpty()) {
            return 0;
        }
        return cantidadGanadas() * 100 / batallas.size();
    }

    public Batalla getBatalla(int pos) throws BattleException {
        if (pos < 0 || pos >= batallas.size()) 
            throw new BattleException("Batalla desconocida");
        return batallas.get(pos);
    }
}
