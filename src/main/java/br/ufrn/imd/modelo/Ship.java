package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {
    protected int size;
    protected List<CellButton> position;
    protected boolean isSunk;

    public Ship() {
        this.position = new ArrayList<>();
        this.isSunk = false;
    }


    // Possíveis exceções aqui: navio em cima de navio e navio fora do mapa
    public void place() {
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

    public void setPosition(List<CellButton> position) throws CelulaInvalidaException {
        int sucessos = 0;
        for (CellButton cell : position) {
            if (cell.getState() == CellButton.State.SHIP){
                //System.out.println("a célula fileira:" + cell.getRow() + " e coluna:" + cell.getCol() + " deu erro");
                throw new CelulaInvalidaException("Você tentou posicionar um navio numa célula onde outro navio já ocupa");
            } else {
                //System.out.println("a célula fileira:" + cell.getRow() + " e coluna:" + cell.getCol() + " deu bom");
                sucessos++;
            }
        }
        if (sucessos == size){
            this.position = position;
            for (CellButton c : position){
                c.setState(CellButton.State.SHIP);
            }
        }
    }

    abstract public List<CellButton> attack(int row, int col);
}

