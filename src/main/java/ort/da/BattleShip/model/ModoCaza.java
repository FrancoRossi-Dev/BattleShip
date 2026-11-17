package ort.da.BattleShip.model;

import java.util.List;

public class ModoCaza extends ModoCazaYAtaque {
    // Esta tactica selecciona casillas al azar, sin repetir las ya disparadas.
    // pasos:
    // 1. se piden al historial las celdas libres (oceano completo menos lo disparado)
    // 2. se elige una de esas celdas al azar, todas con la misma probabilidad
    // el paso a ataque se decide en registrarDisparo

    public ModoCaza(TacticaCazaYAtaque tactica) {
        super(tactica);
    }

    @Override
    public String getNombre() {
        return "Caza";
    }

    @Override
    public Celda elegirCelda(HistorialDisparos historial) {
        // Math.random() da un numero en [0, 1): multiplicado por el tamaño
        // y truncado da un indice valido entre 0 y size - 1
        List<Celda> libres = historial.celdasLibres();
        return libres.get((int) (Math.random() * libres.size()));
    }

    // si el disparo toco un barco sin hundirlo, la maquina pasa a atacarlo.
    // si fue agua o lo hundio de un tiro (barco de tamaño 1), se sigue cazando
    @Override
    public void registrarDisparo(Disparo disparo) {
        if (disparo.isImpacto() && !disparo.hundioBarco()) {
            getTactica().cambiarModo(new ModoAtaque(getTactica()));
        }
    }

}
