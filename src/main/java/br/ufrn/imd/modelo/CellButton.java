package br.ufrn.imd.modelo;

import javafx.scene.Node;

public class CellButton {
    private int row;
    private int col;
    private State state;
    private boolean isHit;
    private Node node;

    public enum State {
        WATER, SHIP, HIT, AIMED
    }

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = State.WATER;
        this.isHit = false;
    }

    public void hit() {
        if (!isHit) {
            isHit = true;
            state = State.HIT;
        }
    }

    public void reset() {
        state = State.WATER;
        isHit = false;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public boolean isHit() {
        return isHit;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // Adicionando Node associado
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
