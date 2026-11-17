package ort.da.BattleShip.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Oceano {

    @Getter
    private int ancho;
    @Getter
    private int alto;

    private Celda[][] celdas;

    // evaluar si es necesario, es lo que permite que varias 
    // celdas apunten a una misma entidad y se pueda recorrer luego
    private List<Barco> barcosVivos = new ArrayList<Barco>();
    // se crea esta lista para llevar un listado de los barcos hundidos y su orden
    private List<Barco> barcosHundidos = new ArrayList<>();

    private int impactos = 0;

    public Oceano(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new Celda[alto][ancho];
        for(int fila = 0; fila < alto; fila++) {
            for(int columna = 0; columna < ancho; columna++){
                celdas[fila][columna] = new Celda(fila, columna);
            }
        }
    }

    public int partesImpactadas() {
        return impactos;
    }

    public boolean flotaHundida() {
        return barcosVivos.isEmpty();
    }

    // el océano es el experto en sus celdas: las expone recorridas fila por fila
    public List<Celda> getCeldas() {
        List<Celda> lista = new ArrayList<>();
        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                lista.add(celdas[fila][columna]);
            }
        }
        return lista;
    }

    public Celda getCelda(int coorX, int coorY) throws BattleException {
        if(coorX >= ancho || coorX < 0 || coorY >= alto || coorY < 0) 
            throw new BattleException("Disparo fuera de tablero");
        
        return celdas[coorY][coorX];
    }

    public Disparo recibirDisparo(Celda o) throws BattleException {
        Celda objetivo = getCelda(o.getColumna(), o.getFila());
        Disparo d = objetivo.recibirDisparo();
        if(d.isImpacto()) impactos++;
        if(d.hundioBarco()) hundir(objetivo.getBarco());
        return d;
    }

    // al hundirse pasa a la lista de hundidos: queda en orden cronológico (bajas enemigas)
    // y flotaHundida() se cumple cuando no quedan vivos
    private void hundir(Barco b) {
        barcosVivos.remove(b);
        barcosHundidos.add(b);
    }

    public List<Barco> getBarcosHundidos() {
        return new ArrayList<>(barcosHundidos);
    }

    public void desplegarBarco(
        TipoBarco tb,
        int coorX, 
        int coorY, 
        String orientacion
    ) throws BattleException {
        boolean horizontal = orientacion.equals("HORIZONTAL");
        if (!horizontal && !orientacion.equals("VERTICAL"))
            throw new BattleException("Orientación inválida");


        int nroCeldas = tb.getTamano();

        // limites del barco
        int ultimaX = coorX + nroCeldas - 1;
        int ultimaY = coorY;
        if (!horizontal) {
            ultimaX = coorX;
            ultimaY = coorY + nroCeldas -1;
        }

        // validar que esta dentro del tablero
        if (coorX < 0 || coorY < 0 || ultimaX >= ancho || ultimaY >= alto)
            throw new BattleException("Barco fuera de tablero");

        List<Celda> celdasBarco = new ArrayList<>();
        for (int i = 0; i < nroCeldas; i++) {
            Celda c;
            if (horizontal) c = celdas[coorY][coorX + i];
            else c = celdas[coorY + i][coorX];
            validarAdjacentes(c);
            celdasBarco.add(c);
        }

        Barco b = BarcoFactory.crear(tb);
        for (Celda c : celdasBarco) {
            c.setBarco(b);
            b.addCelda(c);
        }
        this.barcosVivos.add(b);
    }

    private void validarAdjacentes(Celda celda) throws BattleException {
        // calcula el rango a evaluar, dejando afuera las celdas fuera del tablero
        int desdeFila = Math.max(0, celda.getFila() - 1);
        int hastaFila = Math.min(alto - 1, celda.getFila() + 1);
        int desdeCol = Math.max(0, celda.getColumna() - 1);
        int hastaCol = Math.min(ancho - 1, celda.getColumna() + 1);

        // se valida previo a colocar el barco, por lo que no es necesario chequear contra uno mismo
        for (int fila = desdeFila; fila <= hastaFila; fila++) {
            for (int col = desdeCol; col <= hastaCol; col++) {
                if (celdas[fila][col].getBarco() != null)
                    throw new BattleException("No puede estar adyacente a otro barco");
            }
        }
    }

    // en preparación todavía no se hundió ningún barco, así que el último vivo es el último ubicado
    public Barco getUltimoBarcoDesplegado() {
        return barcosVivos.getLast();
    }
}
