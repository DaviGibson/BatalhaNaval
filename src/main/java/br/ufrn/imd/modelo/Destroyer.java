package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public class Destroyer extends Ship{
    public Destroyer(){
        super();
        this.size = 5;
    }

    public Destroyer(List<CellButton> posicoes) throws CelulaInvalidaException {
        super();
        for (CellButton cell : posicoes) {
            if (cell.getState() == CellButton.State.SHIP){
                throw new CelulaInvalidaException("Você tentou posicionar um navio numa célula onde outro navio já ocupa");
            } else {
                cell.setState(CellButton.State.SHIP);
            }
        }
        this.size = 5;
        position = posicoes;
    }

    //Destroyer ataca 3 células: a clicada, a célula à sua esquerda e a célula à sua direita
    @Override
    public List<CellButton> attack(int row, int col) {
        List<CellButton> list = new ArrayList<>();

        CellButton cell1 = new CellButton(row, col);
        CellButton cell2 = new CellButton(row, col + 1);
        CellButton cell3 = new CellButton(row, col - 1);
        list.add(cell1); list.add(cell2); list.add(cell3);
        return list;
    }
}
