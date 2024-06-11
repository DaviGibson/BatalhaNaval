package br.ufrn.imd.modelo;

public class Game {
    private Player player1;
    private Player player2;
    private Player currentTurn;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        currentTurn = player1;
    }

    public void startGame() {
        // Inicia o jogo, posiciona navios, etc
        // toDo();
    }

    public void endGame() {
        // Lógica para terminar o jogo
        // toDo();
    }

    public void switchTurn() {
        currentTurn = (currentTurn == player1) ? player2 : player1;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
