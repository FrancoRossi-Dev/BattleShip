package ort.da.BattleShip.presenter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ort.da.BattleShip.model.BattleException;

@ControllerAdvice 
public class GlobalExceptionHandler {
   
         private final int errorCodeStatus= 299;

  @ExceptionHandler(BattleException.class)
  public ResponseEntity<String> manejarException(BattleException ex) {  
        return ResponseEntity.status(errorCodeStatus).body(ex.getMessage());
  }

}
