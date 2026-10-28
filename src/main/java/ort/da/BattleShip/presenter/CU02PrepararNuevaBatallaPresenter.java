package ort.da.BattleShip.presenter;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ort.da.BattleShip.model.Facade;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/nuevaBatalla")

public class CU02PrepararNuevaBatallaPresenter {
    private Facade f;

    public CU02PrepararNuevaBatallaPresenter (Facade facade) {
        this.f = facade;
    }

    @PostMapping("conectarVista")
    public Commands conectarVista() {

        return null;
    }
    

}
