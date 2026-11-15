package ort.da.BattleShip.dto;

import lombok.Getter;

// formato del comando "inicializar" de BattleShipBoard (igual al InicializarDto del demo del docente).
// Un escuchador null deja ese océano en solo lectura.
public class InicializarTableroDto {

    @Getter
    private int numeroCeldasVerticales;
    @Getter
    private int numeroCeldasHorizontales;
    @Getter
    private String escuchadorOceanoPropio;
    @Getter
    private String escuchadorOceanoEnemigo;

    public InicializarTableroDto(int numeroCeldasVerticales, int numeroCeldasHorizontales,
            String escuchadorOceanoPropio, String escuchadorOceanoEnemigo) {
        this.numeroCeldasVerticales = numeroCeldasVerticales;
        this.numeroCeldasHorizontales = numeroCeldasHorizontales;
        this.escuchadorOceanoPropio = escuchadorOceanoPropio;
        this.escuchadorOceanoEnemigo = escuchadorOceanoEnemigo;
    }
}
