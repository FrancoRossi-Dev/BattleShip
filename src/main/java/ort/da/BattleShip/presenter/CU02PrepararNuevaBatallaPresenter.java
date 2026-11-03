package ort.da.BattleShip.presenter;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Batalla;
import ort.da.BattleShip.model.BattleException;
import ort.da.BattleShip.model.Facade;
import ort.da.BattleShip.model.TacticaTipo;
import ort.da.BattleShip.model.TipoBarco;

import java.util.ArrayList;
import java.util.List;

// CU02: PREPARAR UNA NUEVA BATALLA
@RestController
@RequestMapping("/nuevaBatalla")
@Scope("session")
public class CU02PrepararNuevaBatallaPresenter {

    private Facade f;
    private List<TacticaTipo> listaDeTacticas = new ArrayList<>();
    private List<TipoBarco> listaDeTiposDeBarco = new ArrayList<>();
    private Batalla batalla;

    public CU02PrepararNuevaBatallaPresenter(Facade facade) {
        this.f = facade;
    }

    /*
     * 1. Indica que desea iniciar una nueva batalla
     * 2. Solicita completar los siguientes datos:
     * - Lugar del combate:
     * - Nombre
     * - Táctica de juego (despliega las tácticas definidas)
     * - Océano:
     * - Cantidad de celdas horizontales
     * - Cantidad de celdas verticales
     * - Flota:
     * Se muestran los tipos de barcos definidos y se permite ingresar una cantidad
     * para cada uno de ellos. Mínimo 0, máximo 6.
     * - Muestra el botón “Continuar”
     * 
     */

    @PostMapping("/vistaConectada")
    public Commands vistaConectada(@RequestParam(required = false) Integer idBatalla) throws BattleException {
        listaDeTacticas = f.getTacticas();
        listaDeTiposDeBarco = f.getTiposBarco();

        if (idBatalla == null) {
            batalla = null;
            return Commands.create(habilitarFormulario());
        }
        batalla = f.getBatalla(idBatalla);
        return Commands.create(mostrarBatallaEnPreparacion());
    }
    /*
     * 3. Ingresa los datos requeridos e indica la opción Continuar.
     * 4. La batalla es incorporada a la lista en la sala de operaciones con el
     * estado
     * “En preparación”.
     */

    /*
     * Flujos alternativos
     * 3. Si el nombre del lugar de combate no es proporcionado, el sistema asignara
     * un texto aleatorio de 6 caracteres alfabéticos
     * en mayúsculas.
     * 3. Si el nombre del lugar de combate ya existe se muestra el mensaje “No se
     * puede librar más de una batalla en el mismo
     * lugar, cambie de nombre”.
     * 3. Si alguna cantidad de celdas del océano es menor a 6 o mayor a 12, se
     * indica un error específico.
     * 3. Si se ingresan más de 6 unidades de un tipo de embarcación, se indica un
     * error específico.
     * 3. Si todos los tipos de naves tienen 0 unidades, se indica un error
     * específico.
     * 3. Si el tamaño de la flota no entra en el océano, se indica un error
     * específico. Para eso, el sistema debe validar que la
     * densidad de ocupación de un océano sea menor al 25%, es decir, los barcos
     * colocados ocupan menos de la cuarta parte
     * de las celdas disponibles.
     * (cantidad de celdas de la flota / cantidad de celdas del océano) < 0.25
     */

    /*
     * Muestra:
     * - Los océanos con el título “Desplegando la Flota”, lo cual permite al
     * usuario
     * ubicar sus barcos en su océano de aguas propias.
     * - El lugar del combate.
     * - La lista de barcos disponibles ordenados por longitud, de mayor a menor. Si
     * en la configuración de la flota se ingresó una cantidad mayor a 1 en un tipo
     * de barco, se muestra el nombre del barco tantas veces como esa cantidad.
     * El primer barco de la lista debe estar siempre seleccionado.
     * - La opción de elegir horizontal o vertical. Horizontal debe estar
     * seleccionado
     * por defecto.
     * - Mientras haya un elemento en la lista de barcos disponibles, se muestra la
     * leyenda de acción “Posicione su [Tipo de barco] en forma [Orientación]”. Por
     * ejemplo “Posicione su destructor en forma vertical”.
     * - Un botón “Librar batalla”.
     * - Un botón “Volver a la sala de operaciones”.
     */

    @PostMapping("/crearBatalla")
    public Commands crearBatalla(
            @RequestParam(required = false) String lugarCombate,
            @RequestParam(required = false) Integer tacticaPos,
            @RequestParam(required = false) Integer nroCeldasX,
            @RequestParam(required = false) Integer nroCeldasY,
            @RequestParam(required = false) Integer[] flotaConfig) throws BattleException {

        Batalla nueva = new Batalla(lugarCombate, tacticaEn(requerido(tacticaPos, "la táctica")),
                requerido(nroCeldasX, "las celdas horizontales"), requerido(nroCeldasY, "las celdas verticales"),
                listaDeTiposDeBarco, cantidades(flotaConfig));
        f.agregar(nueva);
        batalla = nueva;
        return Commands.create(mostrarBatallaEnPreparacion());
    }

    /*
     * 5. Selecciona (haciendo click) una coordenada en el océano de aguas propias.
     * 6.
     * - El barco seleccionado es ubicado en el océano con la posición indicada. La
     * celda seleccionada corresponde a la proa del barco, es decir, el extremo
     * izquierdo (si el barco se coloca horizontalmente) o el extremo inferior (si el
     * barco se acomoda verticalmente).
     * - El barco es eliminado de la lista de barcos disponibles y el siguiente
     * elemento es seleccionado. La leyenda de acción se actualiza
     * adecuadamente. (Si no quedan barcos disponibles para ubicar, la leyenda de
     * acción muestra “Batalla preparada”
     * Los pasos 5 y 6 continúan hasta que todos los barcos están posicionados.
     *
     * Flujos alternativos
     * 5. Si el barco no puede ser ubicado en el océano, se indica un error y se ignora el intento. Un barco no puede ser ubicado
     * en el océano de aguas propias, si se da alguna de las siguientes condiciones:
     * - Parte del barco cae fuera de los límites del océano.
     * - Parte del barco colisiona (se superpone) con un barco ya ubicado.
     * - Parte del barco toca al menos una celda con un barco ya ubicado. (Siempre debe haber una celda con agua
     * entre dos barcos).
     * 5. Si el usuario interactúa con el océano de aguas enemigas durante este caso de uso, se muestra el mensaje “No puede
     * ubicar barcos en aguas enemigas”
     */

    @PostMapping("/desplegarFlota")
    public Commands desplegarFlota(
            @RequestParam int coorX,
            @RequestParam int coorY,
            @RequestParam String orientacion) throws BattleException {

        batallaEnPreparacion().desplegarBarco(coorX, coorY, orientacion);

        if (batalla.flotaDesplegada())
            return Commands.create(batallaPreparada());
        return Commands.create(actualizarTablero());
    }

    @PostMapping("/aguasEnemigas")
    public Commands aguasEnemigas() throws BattleException {
        throw new BattleException("No puede ubicar barcos en aguas enemigas");
    }

    /*
     * 7. Opcionalmente selecciona “Volver a la sala de operaciones”
     * 8. Ejecuta el CU01
     */

    @PostMapping("/volverASala")
    public Commands volverASala() {
        return Commands.create(CommonCommands.Redirection("SalaOperaciones.html"));
    }

    /*
     * 8. Opcionalmente selecciona “Librar batalla”
     * 9. Ejecuta el CU03
     *
     * Flujos alternativos
     * 8. Si el usuario intenta librar batalla antes de posicionar todos sus barcos, se muestra el mensaje “Debe posicionar todos
     * sus barcos primero”.
     */

    @PostMapping("/librarBatalla")
    public Commands librarBatalla() throws BattleException {
        batallaEnPreparacion().librarBatalla();
        return Commands.create(CommonCommands.Redirection("librarBatalla.html"));
    }

    private Batalla batallaEnPreparacion() throws BattleException {
        if (batalla == null)
            throw new BattleException("No hay una batalla en preparación");
        return batalla;
    }

    private int requerido(Integer valor, String campo) throws BattleException {
        if (valor == null) throw new BattleException("Debe ingresar " + campo);
        return valor;
    }

    private int[] cantidades(Integer[] flotaConfig) throws BattleException {
        if (flotaConfig == null) throw new BattleException("Debe ingresar la cantidad de cada tipo de barco");
        int[] cantidades = new int[flotaConfig.length];
        for (int i = 0; i < flotaConfig.length; i++) {
            cantidades[i] = requerido(flotaConfig[i], "la cantidad de cada tipo de barco");
        }
        return cantidades;
    }

    private TacticaTipo tacticaEn(int pos) throws BattleException {
        if (pos < 0 || pos >= listaDeTacticas.size())
            throw new BattleException("Táctica desconocida");
        return listaDeTacticas.get(pos);
    }

    /*
     * CU02: PREPARAR UNA NUEVA BATALLA
     * Este caso de uso cuenta con dos pasos. En el primero se introducen y validan datos básicos para “Preparar una nueva
     * batalla”. Si el usuario no logra superar las validaciones, si abandona la vista o si selecciona “Volver a la sala de
     * operaciones” la información se pierde.
     *
     * Una vez que se avanza al segundo paso (“Desplegando la flota”), se conserva la situación actual de la nueva batalla. De
     * este modo, si el usuario abandona la vista o selecciona “Volver a la sala de operaciones”, puede volver a “Entrar” y
     * continuar con la configuración desde el punto en que la dejó.
     *
     * Postcondiciones
     *
     * El océano propio es establecido para la batalla con la disposición de la flota designado por el usuario.
     * El océano del enemigo es establecido con una distribución aleatoria, pero con las mismas reglas.
     */

    private Command habilitarFormulario() {
        // crear dtos, enviar el front las listas de tipos
        throw new UnsupportedOperationException("Unimplemented method 'habilitarFormulario'");
    }

    private Command mostrarBatallaEnPreparacion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrarBatallaEnPreparacion'");
    }

    private Command actualizarTablero() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarTablero'");
    }

    private Command batallaPreparada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'batallaPreparada'");
    }
}
