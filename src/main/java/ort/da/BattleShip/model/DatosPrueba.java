package ort.da.BattleShip.model;

import java.util.List;

public class DatosPrueba {

    public static void cargarDatos() {
        Facade facade = Facade.getInstancia();

        try {
            TipoBarco portaaviones = new TipoBarco("Portaaviones", 5);
            TipoBarco acorazado = new TipoBarco("Acorazado", 4);
            TipoBarco crucero = new TipoBarco("Crucero", 3);
            TipoBarco submarino = new TipoBarco("Submarino", 2);
            TipoBarco destructor = new TipoBarco("Destructor", 1);

            facade.agregar(portaaviones);
            facade.agregar(acorazado);
            facade.agregar(crucero);
            facade.agregar(submarino);
            facade.agregar(destructor);


            TacticaTipo secuencial = new TacticaTipo("Secuencial");
            TacticaTipo espiral = new TacticaTipo("Espiral");
            TacticaTipo cazaYAtaque = new TacticaTipo("Caza y ataque");

            facade.agregar(secuencial);
            facade.agregar(espiral);
            facade.agregar(cazaYAtaque);

            List<TipoBarco> flotaMalvinas = List.of(crucero, submarino, destructor);
            facade.agregar(new Batalla("Malvinas", espiral, 8, 8, flotaMalvinas));

        } catch (BattleException e) {
            System.out.println("Error al cargar datos de prueba " + e.getMessage());
        }
    }
}
