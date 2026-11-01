package ort.da.BattleShip.model;

public class TacticaFactory {
 
     public static TacticaInterfaz crearTactica(TacticaTipo tactica) {

        String tipo = tactica.getNombre();

        switch (tipo) {
            case "Espiral":
                return new TacticaSecuencial();
            case "Caza y Ataque":
                return new TacticaCazaYAtaque();
            case "Secuencial":
                return new TacticaSecuencial();
            default: 
                return null;
        }

    }
}
