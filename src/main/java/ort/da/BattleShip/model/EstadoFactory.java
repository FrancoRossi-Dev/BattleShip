package ort.da.BattleShip.model;

public class EstadoFactory {
    
    public static EstadoBatalla crearEstadoBatalla(EstadoTipo tipo, Batalla batalla) throws BattleException {

        switch (tipo.getNombre().toUpperCase()) {
            case "PREPARACION":
                return new EstadoPreparacion(batalla);    
            case "LISTA":
                return new EstadoListaParaIniciar(batalla);    
            case "FINALIZADA_VICTORIA":
                return new EstadoFinalizada(batalla, "VICTORIA");    
            case "FINALIZADA_DERROTA":
                return new EstadoFinalizada(batalla, "DERROTA");    
            default:
                return new EstadoPreparacion(batalla);    
        }
    }
}
