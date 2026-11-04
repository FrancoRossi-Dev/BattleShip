package ort.da.BattleShip.model;

public class EstadoPreparacion extends EstadoBatalla {

    public EstadoPreparacion(Batalla b) {
        super(b);
    }

    @Override
    public String getResultado() {
        return "En curso";
    }

    @Override
    public EstadoBatalla cambiarEstado() throws BattleException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cambiarEstado'");
    }

    @Override
    protected void derrota() throws BattleException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'derrota'");
    }

    @Override
    protected void victoria() throws BattleException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'victoria'");
    }

    @Override
    protected void verificarGanador() throws BattleException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verificarGanador'");
    }
    
}
