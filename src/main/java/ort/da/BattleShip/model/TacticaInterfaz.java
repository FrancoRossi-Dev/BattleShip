package ort.da.BattleShip.model;

// Strategy: táctica de juego de la máquina. JugadorMaquina tiene una sola durante toda la batalla
public interface TacticaInterfaz {

    public String getNombre();

    public Celda elegirCelda(HistorialDisparos historial);

    // la máquina avisa el resultado de cada disparo; las tácticas que no dependen
    // del resultado (secuencial, espiral) no hacen nada
    public void registrarDisparo(Disparo disparo);

}
