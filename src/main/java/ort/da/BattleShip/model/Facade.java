package ort.da.BattleShip.model;

import java.util.List;

public class Facade {

    private static Facade instancia;

    private final SistemaBatalla sistemaBatalla = new SistemaBatalla();
    private final SistemaBarcos sistemaBarcos = new SistemaBarcos();
    private final SistemaTacticas sistemaTacticas = new SistemaTacticas();

    private Facade() {
    }

    public static synchronized Facade getInstancia() {
        if (instancia == null) {
            instancia = new Facade();
        }
        return instancia;
    }

    public void agregar(Batalla batalla) throws BattleException {
        sistemaBatalla.agregar(batalla);
    }

    public List<Batalla> getBatallas() {
        return sistemaBatalla.getBatallas();
    }

    public int cantidadGanadas() {
        return sistemaBatalla.cantidadGanadas();
    }

    public int cantidadPerdidas() {
        return sistemaBatalla.cantidadPerdidas();
    }

    public int porcentajeExito() {
        return sistemaBatalla.porcentajeExito();
    }

    public void agregar(TipoBarco tipo) {
        sistemaBarcos.agregar(tipo);
    }

    public List<TipoBarco> getTiposBarco() {
        return sistemaBarcos.getTiposBarco();
    }

    public void agregar(Tactica tactica) {
        sistemaTacticas.agregar(tactica);
    }

    public List<Tactica> getTacticas() {
        return sistemaTacticas.getTacticas();
    }
}
