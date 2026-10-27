package ort.da.BattleShip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ort.da.BattleShip.model.Facade;

@Configuration 
public class AppConfigBattleship {

    @Bean 
    public Facade facade() {
        return Facade.getInstancia();
    }
}
