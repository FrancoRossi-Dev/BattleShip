package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoFinalizada extends EstadoBatalla {

    public static final String VICTORIA = "Victoria";
    public static final String DERROTA = "Derrota";

    @Getter
    private String resultado;

    public EstadoFinalizada(Batalla b, String r) throws BattleException {
        super(b);
        if (!VICTORIA.equals(r) && !DERROTA.equals(r))
            throw new BattleException("Resultado no valido");
        this.resultado = r;
    }

    @Override
    public String getNombre() {
        return "Finalizada";
    }

    @Override
    protected void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        throw new BattleException("La batalla ya está finalizada");
    }

    @Override
    protected void librarBatalla() throws BattleException {
        throw new BattleException("La batalla ya está finalizada");
    }

    @Override
    protected void jugarRonda(int coorX, int coorY) throws BattleException {
        throw new BattleException("La batalla ya está finalizada");
    }

}
