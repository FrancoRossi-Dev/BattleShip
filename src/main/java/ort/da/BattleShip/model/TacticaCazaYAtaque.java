package ort.da.BattleShip.model;

// Contexto de los dos modos de "Caza y ataque" (cambio dinámico de estrategia, como EstadoAgenda en Agenda):
// - Caza: dispara al azar hasta conseguir un impacto, y pasa a ataque.
// - Ataque: dispara alrededor de los impactos hasta hundir el barco, y vuelve a caza.
// El contexto solo delega en el modo actual; cada modo decide cuándo cambiar llamando a cambiarModo.
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
