package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

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


    // el estado da el permiso (quedan barcos por ubicar) y la batalla hace el despliegue;
    // si la posición no es válida el barco sigue pendiente
    @Override
    protected void desplegarBarco(int coorX, int coorY, String orientacion) throws BattleException {
        if (flotaDesplegada()) throw new BattleException("Todos los barcos ya fueron posicionados");

        Barco seleccionado = proximoBarco();
        getBatalla().hacerDesplegarBarco(seleccionado.getTipo(), coorX, coorY, orientacion);
        pendienteAsignar.remover(seleccionado);
    }

    @Override
    protected boolean flotaDesplegada() {
        return pendienteAsignar.getBarcos().isEmpty();
    }

    // la lista está ordenada de mayor a menor, el seleccionado es siempre el primero
    @Override
    protected Barco proximoBarco() {
        if (flotaDesplegada()) return null;
        return pendienteAsignar.getBarcos().get(0);
    }

    @Override
    protected List<Barco> barcosPendientes() {
        return new ArrayList<>(pendienteAsignar.getBarcos());
    }

    @Override
    protected boolean enPreparacion() {
        return true;
    }

    @Override
    protected boolean esFinalizada() {
        return false;
    }

    @Override
    protected void librarBatalla() throws BattleException {
        if (!flotaDesplegada())
            throw new BattleException("Debe posicionar todos sus barcos primero");
        getBatalla().cambiarEstado(EstadoTipo.EN_COMBATE);
    }

    @Override
    protected Ronda jugarRonda(int coorX, int coorY) throws BattleException {
        throw new BattleException("La batalla todavía no comenzó");
    }

}
