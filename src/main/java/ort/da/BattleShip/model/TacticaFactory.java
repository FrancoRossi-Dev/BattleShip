package ort.da.BattleShip.model;

public class TacticaFactory {
 
     public static TacticaInterfaz crearTactica(TacticaTipo tactica) {

        String tipo = tactica.getNombre();

        switch (tipo) {
            case TacticaTipo.ESPIRAL:
                return new TacticaEspiral();
            case TacticaTipo.CAZA_Y_ATAQUE:
                // empieza cazando; pasa a ataque y vuelve sola via siguienteTactica
                return new TacticaCaza();
            case TacticaTipo.SECUENCIAL:
                return new TacticaSecuencial();
            default: 
                return null;
        }

    }
}
