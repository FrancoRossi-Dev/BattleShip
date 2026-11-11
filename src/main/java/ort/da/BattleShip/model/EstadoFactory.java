package ort.da.BattleShip.model;

public class EstadoFactory {

    public static EstadoBatalla crearEstadoBatalla(EstadoTipo tipo, Batalla batalla) throws BattleException {

        switch (tipo.getNombre()) {
            case "PREPARACION":
                return new EstadoPreparacion(batalla);
            case "EN_COMBATE":
                return new EstadoEnCombate(batalla);
            case "FINALIZADA_VICTORIA":
                return new EstadoFinalizada(batalla, EstadoFinalizada.GANADA);
            case "FINALIZADA_DERROTA":
                return new EstadoFinalizada(batalla, EstadoFinalizada.PERDIDA);
            default:
                return new EstadoPreparacion(batalla);
        }
    }
}
