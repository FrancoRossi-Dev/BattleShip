package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;

public class BatallaDto {

    @Getter
    private String lugar;
    @Getter
    private int ancho;
    @Getter
    private int alto;
    @Getter
    private String estado;
    @Getter
    private int flotaPropiaSanas;
    @Getter
    private int flotaPropiaTotal;
    @Getter
    private int flotaEnemigaSanas;
    @Getter
    private int flotaEnemigaTotal;

    public BatallaDto(Batalla b) {
        this.lugar = b.getLugar();
        this.ancho = b.getAncho();
        this.alto = b.getAlto();
        this.estado = b.getEstado().getNombre();
        this.flotaPropiaSanas = b.getJugadorHumano().partesSanas(b.getFlota());
        this.flotaPropiaTotal = b.getFlota().tamanoTotal();
        this.flotaEnemigaSanas = b.getJugadorMaquina().partesSanas(b.getFlota());
        this.flotaEnemigaTotal = b.getFlota().tamanoTotal();
    }

    public static List<BatallaDto> fromList(List<Batalla> batallas) {
        List<BatallaDto> dtoList = new ArrayList<>();
        for (Batalla b : batallas) {
            dtoList.add(new BatallaDto(b));
        }
        return dtoList;
    }

}
