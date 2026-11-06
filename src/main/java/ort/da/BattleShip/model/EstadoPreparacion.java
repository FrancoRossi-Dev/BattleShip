package ort.da.BattleShip.model;

import lombok.Getter;

public class EstadoPreparacion extends EstadoBatalla {

    @Getter
    private Flota pendienteAsignar;

    public EstadoPreparacion(Batalla b) {
        super(b);
        // generar copia
        this.pendienteAsignar = new Flota(b.getFlota());
    }

    @Override
    public String getNombre() {
        return "En preparación";
    }


    @Override
    protected void desplegarBarco(int posBarco, int coorX, int coorY, String orientacion) throws BattleException {
        Batalla b = getBatalla();
        if (pendienteAsignar.getBarcos().isEmpty()) throw new BattleException("Todos los barcos ya fueron posicionados");
        
        Barco seleccionado = pendienteAsignar.barcoEn(posBarco);
        b.getJugadorHumano().desplegarBarco(seleccionado.getTipo(), coorX, coorY, orientacion);
        pendienteAsignar.remover(seleccionado);
    }

    @Override
    protected void librarBatalla() throws BattleException {
        if (!pendienteAsignar.getBarcos().isEmpty())
            throw new BattleException("Debe posicionar todos sus barcos primero");
        getBatalla().cambiarEstado(EstadoTipo.EN_COMBATE);
    }

    @Override
    protected void jugarRonda(int coorX, int coorY) throws BattleException {
        throw new BattleException("La batalla todavía no comenzó");
    }

}
