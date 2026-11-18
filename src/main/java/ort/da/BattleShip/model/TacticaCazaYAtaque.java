package ort.da.BattleShip.model;


public class TacticaCazaYAtaque implements TacticaInterfaz {

    private ModoCazaYAtaque modo;

    public TacticaCazaYAtaque() {
        this.modo = new ModoCaza(this);
    }

    @Override
    public String getNombre() {
        return TacticaTipo.CAZA_Y_ATAQUE;
    }

    @Override
    public Celda elegirCelda(HistorialDisparos historial) {
        return modo.elegirCelda(historial);
    }

    @Override
    public void registrarDisparo(Disparo disparo) {
        modo.registrarDisparo(disparo);
    }

    protected void cambiarModo(ModoCazaYAtaque nuevoModo) {
        this.modo = nuevoModo;
    }

}
