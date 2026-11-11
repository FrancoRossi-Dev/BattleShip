package ort.da.BattleShip.model;

import java.util.List;

import ort.da.BattleShip.observador.Observable;
import ort.da.BattleShip.observador.Observador;

public class Facade extends Observable implements Observador {

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

    // Sistema Batalla
    public Batalla getBatalla(int idBatalla) throws BattleException {
        return sistemaBatalla.getBatalla(idBatalla);
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

    // Sistema Barcos
    public void agregar(TipoBarco tipo) {
        sistemaBarcos.agregar(tipo);
    }

    public List<TipoBarco> getTiposBarco() {
        return sistemaBarcos.getTiposBarco();
    }

    // Sistema tácticas
    public void agregar(TacticaTipo tactica) {
        sistemaTacticas.agregar(tactica);
    }

    public List<TacticaTipo> getTacticas() {
        return sistemaTacticas.getTiposDeTacticas();
    }

    public Celda GetCelda(int coorX, int coorY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetCelda'");
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        // Esto es un rele, toma un evento y si aplica, lo transforma a un eveto propio identico y avisa
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }


}
