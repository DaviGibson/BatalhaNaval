package br.ufrn.imd.modelo;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private Board board;
    private List<Ship> ships;

    public Player() {
        this.board = new Board();
        this.ships = new ArrayList<>();
    }

    public void placeShip(Ship ship, CellButton cellIni) {
        board.placeShip(ship, cellIni);
        ships.add(ship);
    }

    public void takeTurn(int row, int col) {
        board.hitCells(row, col);
    }

    public Board getBoard() {
        return board;
    }

    public List<Ship> getShips() {
        return ships;
    }
}
