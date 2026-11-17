package ort.da.BattleShip.model;

import java.util.List;

public class DatosPrueba {

    private static final String H = "HORIZONTAL";
    private static final String V = "VERTICAL";

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


            TacticaTipo secuencial = new TacticaTipo(TacticaTipo.SECUENCIAL);
            TacticaTipo espiral = new TacticaTipo(TacticaTipo.ESPIRAL);
            TacticaTipo cazaYAtaque = new TacticaTipo(TacticaTipo.CAZA_Y_ATAQUE);

            facade.agregar(secuencial);
            facade.agregar(espiral);
            facade.agregar(cazaYAtaque);

            // Las batallas se arman con las mismas operaciones que usa la aplicación
            // (desplegar -> librar -> jugar rondas), así cada estado queda consistente.
            // Al desplegar siempre se ubica el primer barco pendiente (el más grande):
            // las posiciones van en ese orden, como {x (columna), y (fila), orientación}.

            // 1. En preparación, sin barcos ubicados
            List<TipoBarco> tiposMalvinas = List.of(crucero, submarino, destructor);
            int[] cantidadesMalvinas = {1, 1, 1};
            facade.agregar(new Batalla("Malvinas", espiral, 8, 8, tiposMalvinas, cantidadesMalvinas));

            // 2. En preparación, con la flota a medio ubicar (falta submarino y destructor)
            Batalla trafalgar = new Batalla("Trafalgar", secuencial, 10, 10,
                    List.of(acorazado, submarino, destructor), new int[] {1, 1, 1});
            facade.agregar(trafalgar);
            trafalgar.desplegarBarco(2, 3, V);

            // 3. En combate, con tres rondas jugadas (un impacto y dos aguas del humano)
            Batalla midway = new Batalla("Midway", cazaYAtaque, 10, 10,
                    List.of(acorazado, crucero, destructor), new int[] {1, 1, 1});
            facade.agregar(midway);
            desplegar(midway, new Object[][] { {0, 0, H}, {0, 2, H}, {6, 5, H} });
            midway.librarBatalla();
            dispararAUnBarco(midway, 1);
            dispararAlAgua(midway, 2);

            // 4. Ganada: el humano dispara solo a los barcos enemigos.
            //    Gana en tantas rondas como celdas tiene la flota; la máquina tiene una ronda menos,
            //    así que no le alcanza para hundir la flota propia.
            Batalla jutlandia = new Batalla("Jutlandia", espiral, 8, 8,
                    List.of(crucero, submarino), new int[] {1, 1});
            facade.agregar(jutlandia);
            desplegar(jutlandia, new Object[][] { {0, 0, H}, {5, 5, V} });
            jutlandia.librarBatalla();
            dispararAUnBarco(jutlandia, Integer.MAX_VALUE);

            // 5. Perdida: la flota propia está en la fila 0 y la táctica secuencial recorre
            //    esa fila primero; el humano dispara solo al agua hasta que la máquina gana.
            Batalla leyte = new Batalla("Leyte", secuencial, 8, 8,
                    List.of(submarino, destructor), new int[] {1, 2});
            facade.agregar(leyte);
            desplegar(leyte, new Object[][] { {0, 0, H}, {3, 0, H}, {5, 0, H} });
            leyte.librarBatalla();
            dispararAlAgua(leyte, Integer.MAX_VALUE);

        } catch (BattleException e) {
            System.out.println("Error al cargar datos de prueba " + e.getMessage());
        }
    }

    private static void desplegar(Batalla b, Object[][] posiciones) throws BattleException {
        for (Object[] p : posiciones) {
            b.desplegarBarco((int) p[0], (int) p[1], (String) p[2]);
        }
    }

    // juega rondas disparando a celdas enemigas con barco que todavía no fueron disparadas
    private static void dispararAUnBarco(Batalla b, int rondas) throws BattleException {
        jugar(b, rondas, true);
    }

    // juega rondas disparando a celdas enemigas de agua que todavía no fueron disparadas
    private static void dispararAlAgua(Batalla b, int rondas) throws BattleException {
        jugar(b, rondas, false);
    }

    private static void jugar(Batalla b, int rondas, boolean aBarcos) throws BattleException {
        int jugadas = 0;
        for (Celda c : b.celdasEnemigas()) {
            if (jugadas >= rondas || b.esFinalizada()) return;
            boolean tieneBarco = c.getBarco() != null;
            if (tieneBarco == aBarcos && !c.isDisparada()) {
                b.jugarRonda(c.getColumna(), c.getFila());
                jugadas++;
            }
        }
    }
}
