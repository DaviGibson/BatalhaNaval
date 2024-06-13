package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public class Submarine extends Ship{

    public Submarine(){
        super();
        this.size = 3;
    }

    public Submarine(List<CellButton> posicoes) throws CelulaInvalidaException {
        super();
        for (CellButton cell : posicoes) {
            if (cell.getState() == CellButton.State.SHIP){
                throw new CelulaInvalidaException("Você tentou posicionar um navio numa célula onde outro navio já ocupa");
            } else {
                cell.setState(CellButton.State.SHIP);
            }
        }
        this.size = 3;
        position = posicoes;
    }

    //Submarino ataca 2 células: a clicada e a célula a sua direita
    @Override
    public List<CellButton> attack(int row, int col) {
        List<CellButton> list = new ArrayList<>();

        CellButton cell1 = new CellButton(row, col);
        CellButton cell2 = new CellButton(row, col + 1);
        list.add(cell1); list.add(cell2);
        return list;
    }
}
