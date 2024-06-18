package br.ufrn.imd.modelo;

import java.util.ArrayList;
import java.util.Iterator;
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
        ship.place();
        // ver se opera com o casting em cada if ou aq msm
        ships.add(ship);
        numShips++;
    }

    //usando pro pc
    public void hitCells(int row, int col) {
        CellButton cell = cells[row][col];
        cell.hit();
        for (Ship ship : ships) {
            if (!ship.isAlive()) {
                numShips--;
            }
        }
    }

    public void buscarCellNavio(int coluna, int altura) {
        for (Ship ship : ships) {
            for (CellButton cell : ship.getPosition()) {
                if (cell.getCol() == coluna && cell.getRow() == altura) {
                    cell.hit();
                }
            }
        }
    }

    public void attListaNavios(){
        Iterator<Ship> iterator = ships.iterator();
        while (iterator.hasNext()) {
            Ship ship = iterator.next();
            if (!ship.isAlive()) {
                iterator.remove();
            }
        }
    }

    public CellButton getCell(int row, int col) {

        if (row > 10 || col > 10 || row < 0 || col < 0){
            throw new ArrayIndexOutOfBoundsException("Você mirou numa célula fora do alcance do tabuleiro");
        } else {
            return cells[row][col];
        }
    }

    public void setNumShips(int numShips) {
        this.numShips = numShips;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getNumShips() {
        return numShips;
    }
}

