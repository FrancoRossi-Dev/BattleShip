package ort.da.BattleShip.dto;

import java.util.List;

import ort.da.BattleShip.model.Batalla;

public class TotalBatallasDto {
    public List<BatallaDto> batallas;
    public int victorias;
    public int derrotas;
    public int porcentajeVictorias;


    public TotalBatallasDto(List<Batalla> batallas, int v, int d, int p) {
        this.batallas = BatallaDto.fromList(batallas);
        this.victorias = v;
        this.derrotas = d;
        this.porcentajeVictorias = p;
        }
    }

