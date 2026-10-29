package ort.da.BattleShip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ort.da.BattleShip.model.DatosPrueba;

@SpringBootApplication
public class BattleShipApplication {

	public static void main(String[] args) {
		DatosPrueba.cargarDatos();
		SpringApplication.run(BattleShipApplication.class, args);
	}

}
