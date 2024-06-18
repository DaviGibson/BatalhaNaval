package br.ufrn.imd.modelo;

import br.ufrn.imd.controle.CelulaInvalidaException;
import java.util.List;

public interface IShip {
    void place();
    boolean isAlive();
    boolean isSunk();
    int getSize();
    List<CellButton> getPosition();
    void setPosition(List<CellButton> position) throws CelulaInvalidaException;
    List<CellButton> attack(int row, int col);
}
