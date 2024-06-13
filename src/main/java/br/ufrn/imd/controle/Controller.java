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

    private List<CellButton> alvosTempCorveta;
    private List<CellButton> alvosTempSubmarino;
    private List<CellButton> alvosTempFragata;
    private List<CellButton> alvosTempDestroyer;



    public Controller() {
        board = new Board();
        game = new Game();
        estado = "clique";
        deitado = true;
        alvosTempCorveta = new ArrayList<>();
        alvosTempSubmarino = new ArrayList<>();
        alvosTempFragata = new ArrayList<>();
        alvosTempDestroyer = new ArrayList<>();
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
        if (game.getPlayer1().getShips().size() == 4){
            //posicionar navios do computador
            estado = "selecionarAlvos";
            label.setText("É o seu turno, faça seu(s) ataque(s)");

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

                cell.setOnMouseClicked(event -> {
                    try {
                        handleCellClick(event, gridType);
                    } catch (CelulaInvalidaException e) {
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                });
            }
        }
    }

    // Método para lidar com o clique em uma célula
    private void handleCellClick(MouseEvent event, String gridType) throws CelulaInvalidaException {
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
                    estado = "clique";
                    break;

                case "posicionarSubmarino":
                    updateLabel("Posicione seu Submarino");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 3); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship submarino = new Submarine(posicoesNavio);
                            game.getPlayer1().placeShip(submarino, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    estado = "clique";
                    break;

                case "posicionarFragata":
                    updateLabel("Posicione sua Fragata");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 4); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship fragata = new Frigate(posicoesNavio);
                            game.getPlayer1().placeShip(fragata, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    estado = "clique";
                    break;

                case "posicionarDestroyer":
                    updateLabel("Posicione seu Destroyer");
                    posicoesNavio.add(celIni);
                    sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 5); // Corveta tem tamanho 2
                    try {
                        if (sucessoPosicao){
                            Ship destroyer = new Destroyer(posicoesNavio);
                            game.getPlayer1().placeShip(destroyer, celIni);
                            //fazerPintura(posicoesNavio);
                            updateBoard(board1);
                        }
                    } catch (CelulaInvalidaException e){
                        updateLabel(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    estado = "clique";
                    break;

                case "selecionarAlvosCorveta":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Corveta", alvosTempCorveta, fileira, coluna);
                    }
                    break;
                case "selecionarAlvosSubmarino":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Submarino", alvosTempCorveta, fileira, coluna);
                    }
                    break;
                case "selecionarAlvosFragata":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Fragata", alvosTempCorveta, fileira, coluna);
                    }
                    break;
                case "selecionarAlvosDestroyer":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Destroyer", alvosTempCorveta, fileira, coluna);
                    }
                    break;
            }


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

    public void handleCorveta(){
        if (estado.equals("clique")){
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getShips()){
                if (c instanceof Corvette){
                    posicionado = true;
                }
            }

            if (!posicionado){
                updateLabel("Posicione sua Corveta");
                estado = "posicionarCorveta";
            } else {
                updateLabel("Você já posicionou sua Corveta");
            }
        } else if (estado.equals("selecionarAlvos")) {
            estado = "selecionarAlvosCorveta";
            updateLabel("Selecione os alvos da sua Corveta");
        }
    }
    public void handleSubmarino(){
        if (estado.equals("clique")){
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getShips()){
                if (c instanceof Submarine){
                    posicionado = true;
                }
            }

            if (!posicionado){
                updateLabel("Posicione seu Submarino");
                estado = "posicionarSubmarino";
            } else {
                updateLabel("Você já posicionou seu Submarino");
            }
        } else if (estado.equals("selecionarAlvos")) {
            estado = "selecionarAlvosSubmarino";
            updateLabel("Selecione os alvos do seu Submarino");
        }
    }
    public void handleFragata(){
        if (estado.equals("clique")){
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getShips()){
                if (c instanceof Frigate){
                    posicionado = true;
                }
            }

            if (!posicionado){
                updateLabel("Posicione sua Fragata");
                estado = "posicionarFragata";
            } else {
                updateLabel("Você já posicionou sua Fragata");
            }
        } else if (estado.equals("selecionarAlvos")) {
            estado = "selecionarAlvosFragata";
            updateLabel("Selecione os alvos da sua Fragata");
        }
    }
    public void handleDestroyer(){
        if (estado.equals("clique")){
            updateLabel("Posicione seu Destroyer");
            estado = "posicionarDestroyer";
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getShips()){
                if (c instanceof Destroyer){
                    posicionado = true;
                }
            }

            if (!posicionado){
                updateLabel("Posicione seu Destroyer");
                estado = "posicionarDestroyer";
            } else {
                updateLabel("Você já posicionou seu Destroyer");
            }
        } else if (estado.equals("selecionarAlvos")) {
            estado = "selecionarAlvosDestroyer";
            updateLabel("Selecione os alvos do seu Destroyer");
        }
    }

    private void selecionarAlvos(String tipoNavio, List<CellButton> alvosTemp, int fileira, int coluna) throws CelulaInvalidaException {
        Board boardComputer = game.getPlayer2().getBoard();
        CellButton cell = game.getPlayer2().getBoard().getCell(fileira, coluna);
        Ship navio;
        List<CellButton> listAlvos;

        // Instancia o navio baseado no tipo
        switch (tipoNavio) {
            case "Corveta":
                navio = new Corvette();
                break;
            case "Submarino":
                navio = new Submarine();
                break;
            case "Fragata":
                navio = new Frigate();
                break;
            case "Destroyer":
                navio = new Destroyer();
                break;
            default:
                throw new IllegalArgumentException("Tipo de navio inválido: " + tipoNavio);
        }

        // Realiza o ataque e obtém a lista de alvos
        listAlvos = navio.attack(fileira, coluna);

        // Verifica se há sobreposição com outras células miradas ou já atingidas
        if (alvosTempCorveta.contains(cell) || alvosTempSubmarino.contains(cell) ||
                alvosTempFragata.contains(cell) || alvosTempDestroyer.contains(cell) ||
                boardComputer.getCell(coluna, fileira).isHit()) {

            throw new CelulaInvalidaException("Você está mirando numa célula inválida");
        } else {
            // Limpa a lista de alvos temporários para o navio específico
            if (!alvosTemp.isEmpty()) {
                alvosTemp.clear();
            }
            alvosTemp.addAll(listAlvos);

            estado = "selecionarAlvos";
            updateBoard(boardComputer);
        }
    }


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
                } else if(cell.getState() == CellButton.State.HIT){
                    Node cellNode = cell.getNode();
                    if (cellNode != null) {
                        cellNode.getStyleClass().add("cell-hit");
                    }
                } else {
                    Node cellNode = cell.getNode();
                    if (cellNode != null) {
                        cellNode.getStyleClass().remove("cell-ship");
                        cellNode.getStyleClass().remove("cell-hit");
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
