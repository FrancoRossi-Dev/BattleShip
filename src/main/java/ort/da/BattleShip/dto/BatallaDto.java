package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;

// CU01: un renglón de la lista de batallas
public class BatallaDto {

    // posición en la lista de la fachada: es lo que "Entrar" envía como idBatalla
    @Getter
    private int id;
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
    private int tamanoFlotas;
    @Getter
    private int flotaEnemigaSanas;

    public BatallaDto(Batalla b, int id) {
        this.id = id;
        this.lugar = b.getLugar();
        this.ancho = b.getAncho();
        this.alto = b.getAlto();
        this.estado = b.getEstado().getNombre();
        this.flotaPropiaSanas = b.getJugadorHumano().partesSanas(b.getFlota());
        this.tamanoFlotas = b.getFlota().tamanoTotal();
        this.flotaEnemigaSanas = b.getJugadorMaquina().partesSanas(b.getFlota());
    }

    public static List<BatallaDto> fromList(List<Batalla> batallas) {
        List<BatallaDto> dtoList = new ArrayList<>();
        for (int i = 0; i < batallas.size(); i++) {
            dtoList.add(new BatallaDto(batallas.get(i), i));
        }
        return dtoList;
    }

}
