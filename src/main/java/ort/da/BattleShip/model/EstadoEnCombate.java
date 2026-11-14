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
    protected void jugarRonda(int coorX, int coorY) throws BattleException {
        Batalla b = getBatalla();
        b.hacerJugarRonda(coorX, coorY);

        if (b.flotaMaquinaHundida()) {
            b.cambiarEstado(EstadoTipo.FINALIZADA_VICTORIA);
        } else if (b.flotaHumanaHundida()) {
            b.cambiarEstado(EstadoTipo.FINALIZADA_DERROTA);
        }
    }

    
    @Override
    protected void desplegarBarco(int indiceBarco, int coorX, int coorY, String orientacion) throws BattleException {
        throw new BattleException("La batalla ya comenzó");
    }

    @Override
    protected void librarBatalla() throws BattleException {
        throw new BattleException("La batalla ya comenzó");
    }

    @Override
    protected boolean flotaDesplegada() {
        return true;
    }

    @Override
    protected boolean esFinalizada() {
        return false;
    }

}
