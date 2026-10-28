package ort.da.BattleShip.presenter;

public class CommonCommands {

    public static Command Redirection(String path) {
        return new Command("redirect to", path);
    }
    
}
