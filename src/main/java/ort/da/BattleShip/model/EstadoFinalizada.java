package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoFinalizada extends EstadoBatalla {
    
    @Getter 
    private String resultado;
    
    public EstadoFinalizada(Batalla b, String r) throws BattleException {
        this.resultado = r;
        super(b);
        validate();
    }
    
    private void validate() throws BattleException {
        if (this.resultado != "Victoria" && this.resultado != "Derrota") 
            throw new BattleException("Resultado no valido");
    }

    @Override
    protected void derrota() throws BattleException {
        throw new BattleException("La batalla ya esta finalizada");
    }
    
    @Override
    protected void victoria() throws BattleException {
        throw new BattleException("La batalla ya esta finalizada");
    }

    @Override
    protected void verificarGanador() throws BattleException {
        if(this.getBatalla().derrota()) derrota();
        if(this.getBatalla().victoria()) victoria();
    }

    @Override
    public EstadoBatalla cambiarEstado() throws BattleException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cambiarEstado'");
    }
}
