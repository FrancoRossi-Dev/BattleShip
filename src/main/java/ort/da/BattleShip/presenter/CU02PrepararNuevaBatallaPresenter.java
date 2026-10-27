package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Facade;

@RestController
public class CU02PrepararNuevaBatallaPresenter {

    private final Facade facade = Facade.getInstancia();
}
