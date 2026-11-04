package ort.da.BattleShip.model;

public class EstadoEnCombate extends EstadoBatalla {

    public EstadoEnCombate(Batalla b) {
        super(b);
    }

    @Override
    public String getNombre() {
        return "En combate";
    }

    @Override
    public String getResultado() {
        return "En curso";
    }

    @Override
    protected void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        throw new BattleException("La batalla ya comenzó");
    }

    @Override
    protected void librarBatalla() throws BattleException {
        throw new BattleException("La batalla ya comenzó");
    }

    @Override
    protected void jugarRonda(int coorX, int coorY) throws BattleException {
        Batalla b = getBatalla();
        Ronda r = new Ronda();

        b.hacerTurnoHumano(r, coorX, coorY);
        b.agregarRonda(r);
        if (b.flotaMaquinaHundida()) {
            b.cambiarEstado(EstadoTipo.FINALIZADA_VICTORIA);
            return;
        }

        b.hacerTurnoMaquina(r);
        if (b.flotaHumanaHundida()) {
            b.cambiarEstado(EstadoTipo.FINALIZADA_DERROTA);
        }
    }

}
