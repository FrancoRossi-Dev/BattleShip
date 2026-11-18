package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoFinalizada extends EstadoBatalla {

    public static final String GANADA = "Ganada";
    public static final String PERDIDA = "Perdida";

    @Getter
    private String resultado;

    public EstadoFinalizada(Batalla b, String resultado) throws BattleException {
        super(b);

        if(resultado.equals(GANADA)) {
            this.resultado = GANADA;
            return; 
        }

        if(resultado.equals(PERDIDA)) {
            this.resultado = PERDIDA;
            return; 
        } 

        throw new BattleException("Resultado no valido");
    }

    @Override
    public String getNombre() {
        return resultado;
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
    protected Ronda jugarRonda(int coorX, int coorY) throws BattleException {
        throw new BattleException("La batalla ya está finalizada");
    }

    @Override
    protected boolean flotaDesplegada() {
        return true;
    }

    @Override
    protected boolean esFinalizada() {
        return true;
    }

    @Override
    protected boolean revelaBarcosEnemigos() {
        return resultado.equals(PERDIDA);
    }

}
