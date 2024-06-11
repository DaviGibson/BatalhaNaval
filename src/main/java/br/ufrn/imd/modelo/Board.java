package br.ufrn.imd.modelo;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private CellButton[][] cells;
    private List<Ship> ships;

    public Board() {
        cells = new CellButton[10][10];
        ships = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col] = new CellButton(row, col);
            }
        }
    }

    public void placeShip(Ship ship, List<CellButton> position) {
        ship.place(position);
        ships.add(ship);
    }

    public void hitCell(int row, int col) {
        CellButton cell = cells[row][col];
        cell.hit();
        for (Ship ship : ships) {
            ship.hit();
        }
    }

    public CellButton getCell(int row, int col) {
        return cells[row][col];
    }

    public List<Ship> getShips() {
        return ships;
    }
}

