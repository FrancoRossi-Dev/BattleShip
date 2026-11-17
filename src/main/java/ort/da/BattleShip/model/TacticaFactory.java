package ort.da.BattleShip.model;

public class TacticaFactory {
 
     public static TacticaInterfaz crearTactica(TacticaTipo tactica) {

        String tipo = tactica.getNombre();

        switch (tipo) {
            case TacticaTipo.ESPIRAL:
                return new TacticaEspiral();
            case TacticaTipo.CAZA_Y_ATAQUE:
                return new TacticaCazaYAtaque();
            case TacticaTipo.SECUENCIAL:
            default:
                return new TacticaSecuencial();
        }

    }
}
