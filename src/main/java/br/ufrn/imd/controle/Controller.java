package br.ufrn.imd.controle;

import br.ufrn.imd.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import br.ufrn.imd.visao.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private AnchorPane gamePane;
    @FXML
    private GridPane playerGrid;

    @FXML
    private GridPane computerGrid;

    @FXML
    private Button startGameButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label label;

    @FXML
    private Button corveta;

    private Board board;
    private Game game;

    private String estado;
    private boolean deitado;

    public Controller() {
        board = new Board();
        game = new Game();
        estado = "clique";
        deitado = true;
    }

    @FXML
    public void initialize() {
        gamePane.setStyle("-fx-background-color: #B9D9EB;");
        createGrid(playerGrid,"jogador");
        createGrid(computerGrid, "computador");
        startGameButton.setOnAction(event -> handleStartGame());
        // Adiciona evento de clique com o botão direito ao gamePane
        gamePane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                alternarOrientacaoNavio();
            }
        });
    }

    private void handleStartGame(){
        if (board.getNumShips() == 4){
            label.setText("Iniciando jogo...");
        }
        else label.setText("Voce ainda nao posicionou todos os navios!!!");
    }

    private void createGrid(GridPane grid, String gridType) {
        Board board1;
        if (gridType.equals("jogador")){
            board1 = game.getPlayer1().getBoard();
        } else {
            board1 = game.getPlayer2().getBoard();
        }

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Rectangle cell = new Rectangle (30,30);
                cell.getStyleClass().add("cell");
                grid.add(cell, col, row);

                CellButton cellButton = board1.getCell(row, col);
                cellButton.setNode(cell);

                // Adiciona classes CSS específicas
                if (gridType.equals("jogador")) {
                    //cell.getStyleClass().add("cell-player");
                } else if (gridType.equals("computador")) {
                    cell.getStyleClass().add("cell-computer");
                }

                cell.setOnMouseClicked(event -> handleCellClick(event, gridType));
            }
        }
    }

    // Método para lidar com o clique em uma célula
    private void handleCellClick(MouseEvent event, String gridType) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != null) {
            int coluna = GridPane.getColumnIndex(clickedNode);
            int fileira = GridPane.getRowIndex(clickedNode);
            boolean sucessoPosicao;
            //obtendo celula inicial e inicializando a lista de posições
            CellButton celIni; Board board1;
            if (gridType.equals("jogador")){
                celIni = game.getPlayer1().getBoard().getCell(fileira, coluna);
                board1 = game.getPlayer1().getBoard();
            } else {
                celIni = game.getPlayer2().getBoard().getCell(fileira, coluna);
                board1 = game.getPlayer2().getBoard();
            }
            List<CellButton> posicoesNavio = new ArrayList();

            switch (estado) {
                case "clique":
                    System.out.println("Célula clicada em: [" + fileira + ", " + coluna + "] no grid: " + gridType);
                    updateLabel("Célula clicada em: [" + fileira + ", " + coluna + "] no grid: " + gridType);
                    break;
                case "posicionarCorveta":
                    updateLabel("Posicione sua Corveta");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 2); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship corveta = new Corvette(posicoesNavio);
                            game.getPlayer1().placeShip(corveta, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;

                case "posicionarSubmarino":
                    updateLabel("Posicione seu Submarino");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 3); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship corveta = new Corvette(posicoesNavio);
                            game.getPlayer1().placeShip(corveta, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;

                case "posicionarFragata":
                    updateLabel("Posicione sua Fragata");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 4); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship corveta = new Corvette(posicoesNavio);
                            game.getPlayer1().placeShip(corveta, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;

                case "posicionarDestroyer":
                    updateLabel("Posicione seu Destroyer");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 5); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship corveta = new Corvette(posicoesNavio);
                            game.getPlayer1().placeShip(corveta, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
            }


            /*// Aqui pode adicionar lógica específica para cada grid
            if (gridType.equals("player")) {
                // Ação específica para o GridPane do jogador
            } else if (gridType.equals("computer")) {
                // Ação específica para o GridPane do computador
            }*/
        }
    }

    public void updateLabel(String s){
        label.setText(s);
    }
    private void alternarOrientacaoNavio() {
        deitado = !deitado;
        if (!estado.equals("clique") && !estado.equals("atacar")) {
            if (deitado) {
                updateLabel("Posicionar navio horizontalmente.");
            } else {
                updateLabel("Posicionar navio verticalmente.");
            }
        }
    }

    public void posicionarCorveta(){
        updateLabel("Posicione sua Corveta");
        estado = "posicionarCorveta";
    }
    public void posicionarSubmarino(){
        updateLabel("Posicione seu Submarino");
        estado = "posicionarSubmarino";
    }
    public void posicionarFragata(){
        updateLabel("Posicione sua Fragata");
        estado = "posicionarFragata";
    }
    public void posicionarDestroyer(){
        updateLabel("Posicione seu Destroyer");
        estado = "posicionarDestroyer";
    }

    /*public void playerTurn(){
        updateLabel("É sua vez, ataque uma vez para cada navio");

        for (Ship navio : game.getPlayer1().getShips()){
            if (navio instanceof Corvette corv){
                updateLabel("Ataque com sua Corveta");
                corv.attack();

            } else if (navio instanceof Submarine sub){
                updateLabel("Ataque com seu Submarino");
                sub.attack();

            } else if (navio instanceof Frigate frag){
                updateLabel("Ataque com sua Fragata");
                frag.attack();

            } else if (navio instanceof Destroyer dest){
                updateLabel("Ataque com seu Destroyer");
                dest.attack();
            } else {}
        }
    }*/

    // Adiciona posições à lista de células que formam o navio
    private boolean adicionarPosicoesNavio(CellButton celIni, List<CellButton> posicoesNavio, int tamanho) {
        int fileira = celIni.getRow();
        int coluna = celIni.getCol();

        try {
            if (deitado) {
                for (int i = 1; i < tamanho; i++) {
                    if ((coluna + i) > 10) {
                        throw new NavioForaDoMapaException("O navio ficou em parte fora do mapa, posicione-o de novo");
                    }
                    CellButton celulaAdjacente = game.getPlayer1().getBoard().getCell(fileira, coluna + i);
                    posicoesNavio.add(celulaAdjacente);
                }
            } else {
                for (int i = 1; i < tamanho; i++) {
                    if ((fileira + i) > 10) {
                        throw new NavioForaDoMapaException("O navio ficou em parte fora do mapa, posicione-o de novo");
                    }
                    CellButton celulaAdjacente = game.getPlayer1().getBoard().getCell(fileira + i, coluna);
                    posicoesNavio.add(celulaAdjacente);
                }
            }
        } catch (NavioForaDoMapaException e) {
            desfazerNavio(posicoesNavio);
            desfazerPintura(posicoesNavio);
            updateLabel(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        } catch (ArrayIndexOutOfBoundsException e){
            desfazerNavio(posicoesNavio);
            desfazerPintura(posicoesNavio);
            updateLabel(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // Atualiza o estilo de todas as células do navio
    private void fazerPintura(List<CellButton> posicoesNavio) {
        for (CellButton cell : posicoesNavio) {
            Node cellNode = cell.getNode();
            if (cellNode != null) {
                cellNode.getStyleClass().add("cell-ship");
            }
        }
    }

    private void updateBoard(Board b) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                CellButton cell = b.getCell(row, col);
                if (cell.getState() == CellButton.State.SHIP){
                    Node cellNode = cell.getNode();
                    if (cellNode != null) {
                        cellNode.getStyleClass().add("cell-ship");
                    }
                } else {
                    Node cellNode = cell.getNode();
                    if (cellNode != null) {
                        cellNode.getStyleClass().remove("cell-ship");
                    }
                    cell.reset();
                }
            }
        }
    }

    private void desfazerPintura(List<CellButton> posicoesNavio) {
        System.out.println("Desfez?");
        for (CellButton cell : posicoesNavio) {
            Node cellNode = cell.getNode();
            cellNode.getStyleClass().remove("cell-ship");
        }
    }

    private void desfazerNavio(List<CellButton> posicoesNavio) {
        for (CellButton cell : posicoesNavio) {
            cell.reset();
        }
    }



    @FXML
    public void startGame() {

        // Lógica para iniciar o jogo
        //verifica se navios estão posicionados
        //posiciona os navios do computador
        //começa o jogo com o primeiro movimento sendo do jogador
    }

    @FXML
    public void resetGame() {
        // Lógica para resetar o tabuleiro
    }
}
