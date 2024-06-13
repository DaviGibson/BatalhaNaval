package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public class Corvette extends Ship{

    public Corvette(){
        super();
        this.size = 2;
    }

    public Corvette(List<CellButton> posicoes) throws CelulaInvalidaException {
        super();
        boolean controle;
        for (CellButton cell : posicoes) {
            if (cell.getState() == CellButton.State.SHIP){
                throw new CelulaInvalidaException("Você tentou posicionar um navio numa célula onde outro navio já ocupa");
            }
        }
        for (CellButton cell : posicoes){cell.setState(CellButton.State.SHIP);}


        this.size = 2;
        position = posicoes;
    }

    @Override
    public List<CellButton> attack(int row, int col) {
        List<CellButton> list = new ArrayList<>();

        CellButton cell = new CellButton(row, col);

        list.add(cell);
        return list;
    }




}
