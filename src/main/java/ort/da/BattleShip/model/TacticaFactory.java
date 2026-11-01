package ort.da.BattleShip.model;

public class TacticaFactory {
 
     public static TacticaInterfaz crearTactica(TacticaTipo tactica) {

        String tipo = tactica.getNombre();

        switch (tipo) {
            case "Espiral":
                return new TacticaEspiral();
            case "Caza y ataque":
                return new TacticaCazaYAtaque();
            case "Secuencial":
                return new TacticaSecuencial();
            default: 
                return null;
        }

    }
}
