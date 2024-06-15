package br.ufrn.imd.modelo;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private CellButton[][] cells;
    private List<Ship> ships;
    private int numShips;

    public Board() {
        cells = new CellButton[10][10];
        ships = new ArrayList<>();
        numShips = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col] = new CellButton(row, col);
            }
        }
    }
    // ideia: receber uma celula com o click do mouse, e passar a posição dela e das "size" celulas adiante
    public void placeShip(Ship ship, CellButton cellIni) {

        /*if (ship instanceof Corvette corvette) {
            //agora posso usar coisas especificas do tipo corvette
            for(int i = 0; i < corvette.getSize(); i++){
                corvette.position.add(getCell(cellIni.getRow() +i, cellIni.getCol() + i));
                //pode acessar e modificar os metodos sem gets e sets, a forma de acessar position n parece certa
            }
        }
        if (ship instanceof Submarine submarine) {
            for (int i = 0; i < submarine.getSize(); i++) {
                submarine.position.add(getCell(cellIni.getRow() + i, cellIni.getCol() + i));
            }
        }
        if (ship instanceof Frigate frigate) {
            for(int i = 0; i < frigate.getSize(); i++){
                frigate.position.add(getCell(cellIni.getRow() +i, cellIni.getCol() + i));
            }
        }
        if (ship instanceof Destroyer destroyer) {
            for(int i = 0; i < destroyer.getSize(); i++){
                destroyer.position.add(getCell(cellIni.getRow() +i, cellIni.getCol() + i));
            }
        }*/
        ship.place();
        // ver se opera com o casting em cada if ou aq msm
        ships.add(ship);
        numShips++;
    }

    //Inútil por agora
    public void hitCell(int row, int col) {
        CellButton cell = cells[row][col];
        cell.hit();
        for (Ship ship : ships) {
            if (!ship.isAlive()) {
                numShips--;
            }
        }
    }

    public CellButton getCell(int row, int col) {
        return cells[row][col];
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getNumShips() {
        return numShips;
    }
}

