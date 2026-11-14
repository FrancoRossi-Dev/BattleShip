package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

public class TacticaCazaYAtaque implements TacticaInterfaz {
    // implementar strategy aca - cambio dinanmico

    private static final int[][] HORIZONTAL = {{0, 1}, {0, -1}};
    private static final int[][] VERTICAL = {{1, 0}, {-1, 0}};
    private static final int[][] TODAS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    @Override
    public String GetNombre() {
      return "Táctica de Caza y Ataque";
    }

    // el modo se deduce del historial: hay ataque mientras quede un barco tocado sin hundir
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        List<Celda> candidatas = celdasDeAtaque(historial);
        if (candidatas.isEmpty()) candidatas = celdasDeCaza(historial);
        return candidatas.get((int) (Math.random() * candidatas.size()));
    }

    private List<Celda> celdasDeAtaque(HistorialDisparos historial) {
        List<Celda> impactos = impactosSinHundir(historial);
        int[][] direcciones = direccionesDeAtaque(impactos);
        List<Celda> candidatas = new ArrayList<>();
        for (Celda impacto : impactos) {
            for (int[] direccion : direcciones) {
                int fila = impacto.getFila() + direccion[0];
                int columna = impacto.getColumna() + direccion[1];
                if (historial.estaLibre(fila, columna)) candidatas.add(new Celda(fila, columna));
            }
        }
        return candidatas;
    }

    // hundioBarco mira el estado actual del barco, asi que los impactos
    // viejos dejan de contar cuando el barco termina de hundirse
    private List<Celda> impactosSinHundir(HistorialDisparos historial) {
        List<Celda> impactos = new ArrayList<>();
        for (Disparo d : historial.getDisparos()) {
            if (d.isImpacto() && !d.hundioBarco()) impactos.add(d.getCelda());
        }
        return impactos;
    }

    // con dos impactos ya se conoce la orientacion del barco
    private int[][] direccionesDeAtaque(List<Celda> impactos) {
        if (impactos.size() < 2) return TODAS;
        boolean horizontal = impactos.get(0).getFila() == impactos.get(1).getFila();
        return horizontal ? HORIZONTAL : VERTICAL;
    }

    private List<Celda> celdasDeCaza(HistorialDisparos historial) {
        List<Celda> candidatas = new ArrayList<>();
        for (int fila = 0; fila < historial.getAlto(); fila++) {
            for (int columna = 0; columna < historial.getAncho(); columna++) {
                if (historial.estaLibre(fila, columna)) candidatas.add(new Celda(fila, columna));
            }
        }
        return candidatas;
    }

}
