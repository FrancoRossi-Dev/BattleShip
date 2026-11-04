package ort.da.BattleShip.model;

public class EstadoPreparacion extends EstadoBatalla {

    public EstadoPreparacion(Batalla b) {
        super(b);
    }

    @Override
    public String getNombre() {
        return "En preparación";
    }

    @Override
    public String getResultado() {
        return "En curso";
    }

    @Override
    protected void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        getBatalla().hacerDesplegarBarco(coorX, coorY, orientacion);
    }

    @Override
    protected void librarBatalla() throws BattleException {
        if (!getBatalla().flotaDesplegada())
            throw new BattleException("Debe posicionar todos sus barcos primero");
        getBatalla().cambiarEstado(EstadoTipo.EN_COMBATE);
    }

    @Override
    protected void jugarRonda(int coorX, int coorY) throws BattleException {
        throw new BattleException("La batalla todavía no comenzó");
    }

}
