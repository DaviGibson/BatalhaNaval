package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;
import java.util.List;

public interface IShip {
    void place();
    boolean isAlive();
    CellButton buscaCell(int row, int col);
    boolean isSunk();
    int getSize();
    List<CellButton> getPosition();
    void setPosition(List<CellButton> position) throws CelulaInvalidaException;
    List<CellButton> attack(int row, int col);
}
