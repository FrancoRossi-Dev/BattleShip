package ort.da.BattleShip.model;

public interface TacticaInterfaz {

    public String GetNombre();
    public Celda ElegirCelda(HistorialDisparos historial);

    // cada tactica decide, segun el resultado de su ultimo disparo,
    // que tactica usa la maquina en el proximo turno (puede ser ella misma)
    public TacticaInterfaz siguienteTactica(Disparo ultimoDisparo);

}
