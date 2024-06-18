package br.ufrn.imd.modelo;

public class Game {
    private Player player1;
    private Player player2;

    public Game() {
        player1 = new Player();
        player2 = new Player();
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
