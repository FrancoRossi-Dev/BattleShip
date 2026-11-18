package ort.da.BattleShip.dto;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;

// formato del comando "inicializar" de BattleShipBoard
// un escuchador null deja ese océano en solo lectura
public class InicializarTableroDto {

    @Getter
    private int numeroCeldasVerticales;
    @Getter
    private int numeroCeldasHorizontales;
    @Getter
    private String escuchadorOceanoPropio;
    @Getter
    private String escuchadorOceanoEnemigo;

    public InicializarTableroDto(Batalla b, String escuchadorOceanoPropio, String escuchadorOceanoEnemigo) {
        this.numeroCeldasVerticales = b.getAlto();
        this.numeroCeldasHorizontales = b.getAncho();
        this.escuchadorOceanoPropio = escuchadorOceanoPropio;
        this.escuchadorOceanoEnemigo = escuchadorOceanoEnemigo;
    }
}
