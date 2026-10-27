package ort.da.BattleShip.presenter;

import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Facade;

@RestController
public class CU03LibrarBatallaPresenter {

    private final Facade facade = Facade.getInstancia();
}
