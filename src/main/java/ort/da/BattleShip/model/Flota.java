package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Flota {

    @Getter
    private List<Barco> barcos = new ArrayList<>();

    private static final int MAX_POR_TIPO = 6;

    public Flota(List<TipoBarco> tipos, int[] cantidades) throws BattleException {
        if (cantidades.length != tipos.size()) throw new BattleException("Configuración de flota inválida");
        for (int i = 0; i < tipos.size(); i++) {
            agregar(tipos.get(i), cantidades[i]);
        }
        if (barcos.isEmpty()) throw new BattleException("La flota debe tener al menos un barco");
    }

    private void agregar(TipoBarco tipo, int cantidad) throws BattleException {
        if (cantidad < 0 || cantidad > MAX_POR_TIPO)
            throw new BattleException("La cantidad de " + tipo.getNombre() + " debe estar entre 0 y " + MAX_POR_TIPO);
        for (int i = 0; i < cantidad; i++) {
            agregarOrdenado(new Barco(tipo));
        }
    }

    private void agregarOrdenado(Barco nuevo) {
        int pos = 0;
        while (pos < barcos.size() && barcos.get(pos).getTamano() >= nuevo.getTamano()) {
            pos++;
        }
        barcos.add(pos, nuevo);
    }

    public int tamanoTotal() {
        int total = 0;
        for (Barco b : barcos) {
            total += b.getTipo().getTamano();
        }
        return total;
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
