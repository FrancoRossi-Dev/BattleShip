package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

public class TacticaAtaque implements TacticaInterfaz {
    // esta tactica se usa mientras haya un barco tocado sin hundir
    // precondicion: el historial tiene al menos un impacto sin hundir

    @Override
    public String GetNombre() {
        return "Táctica de Ataque";
    }

    // pasos:
    // 1. se juntan los impactos sin hundir: son las partes conocidas del barco tocado
    // 2. con un impacto no se sabe la direccion: candidatas = los 4 vecinos libres
    //    con dos o mas se conoce el eje: candidatas = los 2 extremos libres de la linea
    // 3. se elige una candidata al azar
    @Override
    public Celda ElegirCelda(HistorialDisparos historial) {
        List<Celda> impactos = impactosSinHundir(historial);
        List<Celda> candidatas = impactos.size() == 1
                ? vecinosLibres(impactos.get(0), historial)
                : extremosLibres(impactos, historial);
        return candidatas.get((int) (Math.random() * candidatas.size()));
    }

    // aciertos cuyo barco sigue a flote: es el barco que se esta atacando
    // (no se usa solo el ultimo disparo: tras un agua el barco sigue tocado)
    private List<Celda> impactosSinHundir(HistorialDisparos historial) {
        List<Celda> impactos = new ArrayList<>();
        for (Disparo d : historial.getDisparos()) {
            if (d.isImpacto() && !d.hundioBarco()) impactos.add(d.getCelda());
        }
        return impactos;
    }

    // un solo impacto: no se conoce la direccion, se prueban los 4 lados
    private List<Celda> vecinosLibres(Celda impacto, HistorialDisparos historial) {
        int fila = impacto.getFila();
        int columna = impacto.getColumna();
        List<Celda> libres = new ArrayList<>();
        agregarSiLibre(libres, fila - 1, columna, historial);
        agregarSiLibre(libres, fila + 1, columna, historial);
        agregarSiLibre(libres, fila, columna - 1, historial);
        agregarSiLibre(libres, fila, columna + 1, historial);
        return libres;
    }

    // dos o mas impactos: el barco esta en linea, solo quedan sus dos extremos
    // (los barcos no se tocan, asi que todos los impactos sin hundir son del mismo barco)
    private List<Celda> extremosLibres(List<Celda> impactos, HistorialDisparos historial) {
        // si dos impactos comparten fila el barco es horizontal, si no es vertical
        boolean horizontal = impactos.get(0).getFila() == impactos.get(1).getFila();

        // posicion minima y maxima de los impactos sobre el eje del barco
        // (columna si es horizontal, fila si es vertical): son las puntas conocidas
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Celda impacto : impactos) {
            int pos = horizontal ? impacto.getColumna() : impacto.getFila();
            min = Math.min(min, pos);
            max = Math.max(max, pos);
        }

        // se prueba una celda antes de la primera punta y una despues de la ultima;
        // si una ya fue agua o cae fuera del oceano, queda solo la otra
        List<Celda> libres = new ArrayList<>();
        if (horizontal) {
            int fila = impactos.get(0).getFila();
            agregarSiLibre(libres, fila, min - 1, historial);
            agregarSiLibre(libres, fila, max + 1, historial);
        } else {
            int columna = impactos.get(0).getColumna();
            agregarSiLibre(libres, min - 1, columna, historial);
            agregarSiLibre(libres, max + 1, columna, historial);
        }
        return libres;
    }

    // cuando el barco se hunde, la maquina vuelve a cazar.
    // si fue agua o un impacto sin hundir, se sigue atacando el mismo barco
    // (los barcos no se tocan, asi que al hundirlo no queda otro tocado pendiente)
    @Override
    public TacticaInterfaz siguienteTactica(Disparo ultimoDisparo) {
        if (ultimoDisparo.hundioBarco()) return new TacticaCaza();
        return this;
    }

    // estaLibre descarta celdas fuera del oceano o ya disparadas
    private void agregarSiLibre(List<Celda> lista, int fila, int columna, HistorialDisparos historial) {
        if (historial.estaLibre(fila, columna)) lista.add(new Celda(fila, columna));
    }

}
