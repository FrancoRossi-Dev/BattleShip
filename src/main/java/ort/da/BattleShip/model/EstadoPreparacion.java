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
    protected void desplegarBarco(int posBarco, int coorX, int coorY, String orientacion) throws BattleException {
        Batalla b = getBatalla();
        if (b.flotaDesplegada()) throw new BattleException("Todos los barcos ya fueron posicionados");
        
        Barco seleccionado = b.getPendienteAsignar().barcoEn(posBarco);
        b.getJugadorHumano().desplegarBarco(seleccionado.getTipo(), coorX, coorY, orientacion);
        b.getPendienteAsignar().remover(seleccionado);
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
