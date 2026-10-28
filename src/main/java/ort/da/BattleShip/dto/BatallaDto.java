package ort.da.BattleShip.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ort.da.BattleShip.model.Batalla;

public class BatallaDto {
    
    @Getter 
    public String lugarCombate;
    @Getter 
    public int ancho;
    @Getter 
    public int largo;
    @Getter 
    public String estado;
    @Getter 
    public int tamanoFlota;
    @Getter 
    public int barcosAliadosVivos;
    @Getter 
    public int barcosEnemigosVivos;
    
    

    public BatallaDto(Batalla b) {
        this.lugarCombate = b.GetLugarCombate();
        this.ancho = b.GetAncho();
        this.largo = b.GetLargo();
        this.estado = b.GetEstado();
        this.tamanoFlota = b.GetTamanoFlota();
        this.barcosAliadosVivos = b.GetBarcosAliadosVivos();
        this.barcosEnemigosVivos = b.GetBarcosEnemigosVivos();
    }

    public static List<BatallaDto> fromList(List<Batalla> batallas) {
        List<BatallaDto> dtoList = new ArrayList<>();
        for(Batalla b : batallas) {
            dtoList.add(new BatallaDto(b));
        }
        return dtoList;
    }
    
}
