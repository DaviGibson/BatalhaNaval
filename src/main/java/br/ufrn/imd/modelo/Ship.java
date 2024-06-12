package br.ufrn.imd.modelo;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private int size;
    private List<CellButton> position;
    private boolean isSunk;

    public Ship(int size) {
        this.size = size;
        this.position = new ArrayList<>();
        this.isSunk = false;
    }

    public void place(List<CellButton> position) {
        this.position = position;
        for (CellButton cell : position) {
            cell.setState(CellButton.State.SHIP);
        }
    }
// talvez mudar pro tabuleiro
    public boolean isAlive() {
        int cellsHit = 0;
        for (CellButton cell : position) {
            if (cell.getState() == CellButton.State.SHIP && cell.isHit()) {
                cellsHit += 1;
            }
        }
        if (cellsHit == position.size()){
            isSunk = true;
            return false;
        }
        return true;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public int getSize() {
        return size;
    }

    public List<CellButton> getPosition() {
        return position;
    }
}

