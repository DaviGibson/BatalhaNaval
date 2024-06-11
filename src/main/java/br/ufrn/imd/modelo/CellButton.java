package br.ufrn.imd.modelo;

public class CellButton {
    private int row;
    private int col;
    private State state;
    private boolean isHit;

    public enum State {
        WATER, SHIP, HIT
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
            if (state == State.SHIP) {
                state = State.HIT;
            }
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

    // Getters for row and col
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
