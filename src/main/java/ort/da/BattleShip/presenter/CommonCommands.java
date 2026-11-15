package ort.da.BattleShip.presenter;

public class CommonCommands {

    // la vista navega a la página indicada (handler "redirigir" en cada html)
    public static Command redirigir(String pagina) {
        return new Command("redirigir", pagina);
    }

}
