package br.ufrn.imd.controle;

import br.ufrn.imd.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.random.RandomDataGenerator;

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
    private Label label;


    private Game game;
    private String estado;
    private boolean deitado;

    private String vencedor;

    //Variáveis para o posicionamento dos alvos mirados pelo jogador
    private List<CellButton> alvosMiradosCorveta;

    private List<CellButton> alvosMiradosSubmarino;
    private List<CellButton> alvosMiradosFragata;
    private List<CellButton> alvosMiradosDestroyer;



    public Controller() {
        game = new Game();
        estado = "clique";
        deitado = true;
        alvosMiradosCorveta = new ArrayList<>();
        alvosMiradosSubmarino = new ArrayList<>();
        alvosMiradosFragata = new ArrayList<>();
        alvosMiradosDestroyer = new ArrayList<>();
        vencedor = "ninguem";
    }
    @FXML
    public void initialize() {
        gamePane.setStyle("-fx-background-color: #B9D9EB;");
        createGrid(playerGrid,"jogador");
        createGrid(computerGrid, "computador");


        startGameButton.setOnAction(event -> {
            try {
                handleStartGame();
            } catch (CelulaInvalidaException e) {
                throw new RuntimeException(e);
            }
        });

        //evento de clique com o botão direito no gamePane
        gamePane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                alternarOrientacaoNavio();
            }
        });
    }

    //método para criar um grid, inicializar seus Nodes (JavaFX) e suas células (CellButton) e definir seu comportamento

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
                    //cell.getStyleClass().add("cell-computer");
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

    // Método para lidar com o clique em qualquer célula durante variados estados do jogo

    private void handleCellClick(MouseEvent event, String gridType) throws CelulaInvalidaException {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != null) {
            //Variáveis importantes para o Switch-case abaixo
            int coluna = GridPane.getColumnIndex(clickedNode); //coluna do Node clicado
            int fileira = GridPane.getRowIndex(clickedNode); //fileira do Node clicado

            boolean sucessoPosicao; //usado nos estados de POSICIONAR

            CellButton celIni; //célula clicada pelo jogador (em seu grid ou no grid do computador)
            Board board1; //board do jogador ou do computador
            if (gridType.equals("jogador")){
                celIni = game.getPlayer1().getBoard().getCell(fileira, coluna);
                board1 = game.getPlayer1().getBoard();
            } else {
                celIni = game.getPlayer2().getBoard().getCell(fileira, coluna);
                board1 = game.getPlayer2().getBoard();
            }

            List<CellButton> posicoesNavio = new ArrayList(); //usado nos estados de POSICIONAR

            switch (estado) {
                case "clique":
                    System.out.println("Célula clicada em: [" + fileira + ", " + coluna + "] no grid: " + gridType);
                    updateLabel("Célula clicada em: [" + fileira + ", " + coluna + "] no grid: " + gridType);
                    break;
                case "posicionarCorveta":
                    if (gridType.equals("jogador")){
                        updateLabel("Posicione sua Corveta");
                        posicoesNavio.add(celIni); //inclui a célula clicada
                        sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 2); // inclui as outras células do navio em função de seu tamanho e se está deitado ou de pé
                        Ship corveta = new Corvette();
                        try {
                            if (sucessoPosicao){
                                corveta.setPosition(posicoesNavio); //posiciona um navio, alterando seu State de WATER para SHIP
                                game.getPlayer1().placeShip(corveta, celIni); // adiciona o navio ao array de navios do jogador
                                //game.getPlayer1().getBoard().placeShip(corveta, celIni);
                                updateBoard(board1); //atualiza e pinta o tabuleiro
                            }
                        } catch (CelulaInvalidaException e){ //provavelmente erro relacionado a posicionar um navio em cima de outro
                            desfazerNavio1(corveta);
                            updateBoard(board1);
                            updateLabel(e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }
                    estado = "clique";
                    break;

                case "posicionarSubmarino":
                    if (gridType.equals("jogador")){
                        updateLabel("Posicione seu Submarino");
                        posicoesNavio.add(celIni);
                        sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 3); // Corveta tem tamanho 2
                        Ship submarino = new Submarine();
                        try {
                            if (sucessoPosicao){
                                submarino.setPosition(posicoesNavio);
                                game.getPlayer1().placeShip(submarino, celIni);
                                //.getPlayer1().getBoard().placeShip(submarino, celIni);
                                updateBoard(board1);
                            }
                        } catch (CelulaInvalidaException e){
                            desfazerNavio1(submarino);
                            updateBoard(board1);
                            updateLabel(e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }
                    estado = "clique";
                    break;

                case "posicionarFragata":
                    if (gridType.equals("jogador")){
                        updateLabel("Posicione sua Fragata");
                        posicoesNavio.add(celIni);
                        sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 4); // Corveta tem tamanho 2
                        Ship fragata = new Frigate();
                        try {
                            if (sucessoPosicao){
                                fragata.setPosition(posicoesNavio);
                                game.getPlayer1().placeShip(fragata, celIni);
                                //game.getPlayer1().getBoard().placeShip(fragata, celIni);
                                updateBoard(board1);
                            }
                        } catch (CelulaInvalidaException e){
                            desfazerNavio1(fragata);
                            updateBoard(board1);
                            updateLabel(e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }
                    estado = "clique";
                    break;

                case "posicionarDestroyer":
                    if (gridType.equals("jogador")){
                        updateLabel("Posicione seu Destroyer");
                        posicoesNavio.add(celIni);
                        sucessoPosicao = adicionarPosicoesNavio(celIni, posicoesNavio, 5); // Corveta tem tamanho 2
                        Ship destroyer = new Destroyer();
                        try {
                            if (sucessoPosicao){
                                destroyer.setPosition(posicoesNavio);
                                game.getPlayer1().placeShip(destroyer, celIni);
                                //game.getPlayer1().getBoard().placeShip(destroyer, celIni);
                                updateBoard(board1);
                            }
                        } catch (CelulaInvalidaException e){
                            desfazerNavio1(destroyer);
                            updateBoard(board1);
                            updateLabel(e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }
                    estado = "clique";
                    break;

                case "selecionarAlvosCorveta":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Corveta", alvosMiradosCorveta, fileira, coluna);
                        estado = "selecionarAlvos";
                    }
                    break;
                case "selecionarAlvosSubmarino":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Submarino", alvosMiradosSubmarino, fileira, coluna);
                        estado = "selecionarAlvos";
                    }
                    break;
                case "selecionarAlvosFragata":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Fragata", alvosMiradosFragata, fileira, coluna);
                        estado = "selecionarAlvos";
                    }
                    break;
                case "selecionarAlvosDestroyer":
                    if (gridType.equals("computador")){
                        selecionarAlvos("Destroyer", alvosMiradosDestroyer, fileira, coluna);
                        estado = "selecionarAlvos";
                    }
                    break;
            }


        }
    }

    //gerencia os usos do botão Destroyer (posicioná-lo e atacar com ele)
    public void handleCorveta() throws InterruptedException {
        boolean isAlive = true;
        boolean excluido = true;
        List<Ship> ships = game.getPlayer1().getBoard().getShips();
        for (Ship ship : ships) {
            if (ship instanceof Corvette) {
                isAlive = ship.isAlive();
                excluido = false;
            }
        }
        if (excluido){isAlive = false;}
        System.out.println(isAlive);

        if (estado.equals("clique")){
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getBoard().getShips()){
                if (c instanceof Corvette){
                    posicionado = true;
                }
            }

            if (!posicionado){
                //Thread.sleep(3000);
                updateLabel("Posicione sua Corveta");
                estado = "posicionarCorveta";
            } else {
                updateLabel("Você já posicionou sua Corveta");
            }
        }

        if (estado.equals("selecionarAlvos")) {
            if (isAlive) {
                if (alvosMiradosCorveta.isEmpty()){
                    updateLabel("Selecione os alvos da sua Corveta");
                    estado = "selecionarAlvosCorveta";
                } else {
                    updateLabel("Você já mirou com o seu Corveta");
                }
            } else {
                updateLabel("Sua Corveta esta afundada!!!");
            }
        }


    }

    //gerencia os usos do botão Submarino (posicioná-lo e atacar com ele)
    public void handleSubmarino(){
        boolean isAlive = true;
        boolean excluido = true;
        List<Ship> ships = game.getPlayer1().getBoard().getShips();
        for (Ship ship : ships) {
            if (ship instanceof Submarine) {
                isAlive = ship.isAlive();
                excluido = false;
            }
        }
        if (excluido){isAlive = false;}
        System.out.println(isAlive);

        if (estado.equals("clique")) {
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getBoard().getShips()) {
                if (c instanceof Submarine) {
                    posicionado = true;
                }
            }

            if (!posicionado) {
                updateLabel("Posicione seu Submarino");
                estado = "posicionarSubmarino";
            } else {
                updateLabel("Você já posicionou seu Submarino");
            }
        }

        if (estado.equals("selecionarAlvos")) {
            if (isAlive) {
                if (alvosMiradosSubmarino.isEmpty()) {
                    updateLabel("Selecione os alvos do seu Submarino");
                    estado = "selecionarAlvosSubmarino";
                } else {
                    updateLabel("Você já mirou com o seu Submarino");
                }
            } else {
                updateLabel("Seu Submarino esta afundado!!!");
            }

        }


    }

    //gerencia os usos do botão Fragata (posicioná-lo e atacar com ele)
    public void handleFragata(){
        boolean isAlive = true;
        boolean excluido = true;
        List<Ship> ships = game.getPlayer1().getBoard().getShips();
        for (Ship ship : ships) {
            if (ship instanceof Frigate) {
                isAlive = ship.isAlive();
                excluido = false;
            }
        }
        if (excluido){isAlive = false;}
        System.out.println(isAlive);

        if (estado.equals("clique")) {
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getBoard().getShips()) {
                if (c instanceof Frigate) {
                    posicionado = true;
                }
            }

            if (!posicionado) {
                updateLabel("Posicione sua Fragata");
                estado = "posicionarFragata";
            } else {
                updateLabel("Você já posicionou sua Fragata");
            }
        }

        if (estado.equals("selecionarAlvos")) {
            if (isAlive) {
                if (alvosMiradosFragata.isEmpty()) {
                    updateLabel("Selecione os alvos da sua Fragata");
                    estado = "selecionarAlvosFragata";
                } else {
                    updateLabel("Você já mirou com o seu Fragata");
                }
            } else {
                updateLabel("Sua Fragata esta afundada!!!");
            }
        }


    }

    //gerencia os usos do botão Destroyer (posicioná-lo e atacar com ele)
    public void handleDestroyer(){
        boolean isAlive = true;
        boolean excluido = true;
        List<Ship> ships = game.getPlayer1().getBoard().getShips();
        for (Ship ship : ships) {
            if (ship instanceof Destroyer) {
                isAlive = ship.isAlive();
                excluido = false;
            }
        }
        if (excluido){isAlive = false;}
        System.out.println(isAlive);

        if (estado.equals("clique")) {
            updateLabel("Posicione seu Destroyer");
            boolean posicionado = false;
            for (Ship c : game.getPlayer1().getBoard().getShips()) {
                if (c instanceof Destroyer) {
                    posicionado = true;
                }
            }

            if (!posicionado) {
                updateLabel("Posicione seu Destroyer");
                estado = "posicionarDestroyer";
            } else {
                updateLabel("Você já posicionou seu Destroyer");
            }
        }

        if (estado.equals("selecionarAlvos") ) {
            if (isAlive) {
                if (alvosMiradosDestroyer.isEmpty()) {
                    updateLabel("Selecione os alvos do seu Destroyer");
                    estado = "selecionarAlvosDestroyer";
                } else {
                    updateLabel("Você já mirou com o seu Destroyer");
                }
            } else {
                updateLabel("Seu Destroyer esta afundado!!!");

            }

        }

    }




    //gerencia uso do botão Atirar
    public void handleAtirar(){
        if (estado.equals("selecionarAlvos")){
            int naviosVivos = game.getPlayer1().getBoard().getShips().size();
            int naviosMirados = 0;
            Board boardComputer = game.getPlayer2().getBoard();

            if (!alvosMiradosCorveta.isEmpty()){naviosMirados++;}
            if (!alvosMiradosSubmarino.isEmpty()){naviosMirados++;}
            if (!alvosMiradosFragata.isEmpty()){naviosMirados++;}
            if (!alvosMiradosDestroyer.isEmpty()){naviosMirados++;}


            if (naviosVivos == naviosMirados){ // Verifica se todos navios vivos miraram
                List<Ship> playerShips = game.getPlayer1().getBoard().getShips();
                System.out.println("mirou com todos navios e vc tem " + playerShips.size() + "navios");

                atiraMirados(boardComputer);
                updateBoard(boardComputer);

                // Lógica para dar a dica de posição seria por aqui


                updateLabel("Você atirou no campo inimigo");
                System.out.println("Você atirou no campo inimigo... o computador atacou");

                alvosMiradosCorveta.clear();
                alvosMiradosSubmarino.clear();
                alvosMiradosFragata.clear();
                alvosMiradosDestroyer.clear();

                // lógica para o COMPUTADOR ATIRAR ficaria aq se pa
                boardComputer.attListaNavios();
                ataquePc();

            } else {
                updateLabel("Você ainda não mirou com algum navio");
            }

            Board boardPlayer = game.getPlayer1().getBoard();
            Board boardPc = game.getPlayer2().getBoard();
            Player jogador = game.getPlayer1(); Player computador = game.getPlayer2();

            boardPlayer.attListaNavios();
            boardPc.attListaNavios();

            updateLabel ("SEUS navios vivos: " + boardPlayer.getShips().size() + "    Navios do PC vivos: " + boardPc.getShips().size());

            System.out.println("SEUS navios vivos: " + boardPlayer.getShips().size() + "    Navios do PC vivos: " + boardPc.getShips().size());
            System.out.println("SEUS navios vivos: " + jogador.getShips().size() + "    Navios do PC vivos: " + computador.getShips().size());

            if (boardPc.getShips().size() == 0){
                updateLabel("PC PERDEU");
                System.out.println("PC PERDEU");
                estado = "endGame";
            } else if (boardPlayer.getShips().size() == 0){
                updateLabel("PLAYER PERDEU");
                System.out.println("PLAYER PERDEU");
                estado = "endGame";
            }

        }
    }


    private void ataquePc(){
        Board board = game.getPlayer1().getBoard();
        int quantosNavios = game.getPlayer2().getBoard().getShips().size();

        List<CellButton> cellsAttk;

        for(int i = 0; i < quantosNavios; i++){
            //Random random = new Random();
            RandomDataGenerator randomData = new RandomDataGenerator();

            int fileira = randomData.nextInt(0, 9);
            int coluna = randomData.nextInt(0, 9);

            cellsAttk = game.getPlayer2().getBoard().getShips().get(i).attack(fileira,coluna);
            for (CellButton c : cellsAttk){
                if(c.getRow() < 10 && c.getCol() < 10 && c.getRow() >=0 && c.getCol() >=0){
                    board.hitCells(c.getRow(),c.getCol());
                    board.buscarCellNavio(c.getRow(),c.getCol());

                    //System.out.println("ATIRANDO " + c.getRow()+" "+c.getCol());
                }
            }
            board.attListaNavios();
            updateBoard(board);
            if (board.getShips().size() == 0){
                updateLabel("PLAYER PERDEU");
                System.out.println("PLAYER PERDEU");
                estado = "endGame";
            }
        }
    }
    //gerencia o uso do botão Começar Jogo!!
    private void handleStartGame() throws CelulaInvalidaException {
        System.out.println("tanto de navios: " + game.getPlayer1().getBoard().getShips().size());
        if (game.getPlayer1().getBoard().getShips().size() == 4 && !estado.equals("endGame")){
            //posicionar navios do computador
            if(game.getPlayer2().getBoard().getShips().size() < 4) {
                posicionaComputador();
            }

            estado = "selecionarAlvos";
            label.setText("É o seu turno, faça seu(s) ataque(s)");


        } else if (estado.equals("endGame")){

        } else label.setText("Voce ainda nao posicionou todos os navios!!!");

    }

    private void posicionaComputador() throws CelulaInvalidaException {
        for (int i = 2; i < 6; i++) {
            posicionaNaviosPc(i);
        }

    }

    private void posicionaNaviosPc(int tamanho) throws CelulaInvalidaException {
        RandomDataGenerator randomData = new RandomDataGenerator();
        int virado = randomData.nextInt(0, 1);


        boolean sucessoPosicao;
        List<CellButton> posicoesNavio = new ArrayList();
        System.out.println("aaaaa");
        sucessoPosicao = adicionarPosicoesNavioPc(posicoesNavio, tamanho, virado);
        try{
            if (sucessoPosicao){
                switch (tamanho) {
                    case 2:
                        Ship ship1 = new Corvette(posicoesNavio);
                        game.getPlayer2().getBoard().placeShip(ship1, posicoesNavio.get(0));
                        game.getPlayer2().getShips().add(ship1);
                        updateBoard(game.getPlayer2().getBoard());
                    case 3:
                        Ship ship2 = new Submarine(posicoesNavio);
                        game.getPlayer2().getBoard().placeShip(ship2, posicoesNavio.get(0));
                        game.getPlayer2().getShips().add(ship2);
                        updateBoard(game.getPlayer2().getBoard());
                    case 4:
                        Ship ship3 = new Frigate(posicoesNavio);
                        game.getPlayer2().getBoard().placeShip(ship3, posicoesNavio.get(0));
                        game.getPlayer2().getShips().add(ship3);
                        updateBoard(game.getPlayer2().getBoard());
                    case 5:
                        Ship ship4 = new Destroyer(posicoesNavio);
                        game.getPlayer2().getBoard().placeShip(ship4, posicoesNavio.get(0));
                        game.getPlayer2().getShips().add(ship4);
                        updateBoard(game.getPlayer2().getBoard());

                }

            }
        } catch (CelulaInvalidaException e){
            updateLabel(e.getMessage());
            System.out.println(e.getMessage());
        }
    }





    //Seleciona e demarca no mapa os alvos MIRADOS de um navio específico, procedimento necessário antes de atirar
    private void selecionarAlvos(String tipoNavio, List<CellButton> alvosTemp, int fileira, int coluna) throws CelulaInvalidaException, ArrayIndexOutOfBoundsException{
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
        if (alvosMiradosCorveta.contains(cell) || alvosMiradosSubmarino.contains(cell) ||
                alvosMiradosFragata.contains(cell) || alvosMiradosDestroyer.contains(cell) ||
                boardComputer.getCell(fileira, coluna).isHit()) {
            throw new CelulaInvalidaException("Você está mirando numa célula inválida");
        } else {
            // Limpa a lista de alvos temporários para o navio específico
            if (!alvosTemp.isEmpty()) {alvosTemp.clear();}
            alvosTemp.addAll(listAlvos);

            for (CellButton celula : listAlvos){
                if (celula.getCol() < 10 && celula.getCol() >= 0  && celula.getRow() < 10 && celula.getRow() >= 0){
                    boardComputer.getCell(celula.getRow(), celula.getCol()).setAimed(true);
                }
            }
            updateBoard(boardComputer);

        }
    }


    private boolean adicionarPosicoesNavioPc(List<CellButton> posicoesNavio, int tamanho, int virado) {
        Random random = new Random();

        while (true) {
            int fileira = random.nextInt(10);
            int coluna = random.nextInt(10);

            boolean posicaoValida = true;
            posicoesNavio.clear();

            if (virado == 0) {
                for (int i = 1; i <= tamanho; i++) {
                    if ((coluna + i) >= 10 || game.getPlayer2().getBoard().getCell(fileira, coluna + i).getState() == CellButton.State.SHIP) {
                        posicaoValida = false;
                        break;
                    } else {
                        CellButton celulaAdjacente = game.getPlayer2().getBoard().getCell(fileira, coluna + i);
                        posicoesNavio.add(celulaAdjacente);
                    }
                }
            } else {
                for (int i = 1; i <= tamanho; i++) {
                    if ((fileira + i) >= 10 || game.getPlayer2().getBoard().getCell(fileira + i, coluna).getState() == CellButton.State.SHIP) {
                        posicaoValida = false;
                        break;
                    } else {
                        CellButton celulaAdjacente = game.getPlayer2().getBoard().getCell(fileira + i, coluna);
                        posicoesNavio.add(celulaAdjacente);
                    }
                }
            }

            if (posicaoValida) {
                return true;
            }
        }
    }

    // Adiciona coordenadas de células necessárias para definir (inicializar) um navio específico
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


    //Criada para corrigir erros no posicionamento indevido de cêlulas num mapa
    private void desfazerPintura(List<CellButton> posicoesNavio) {
        System.out.println("Desfez?");
        for (CellButton cell : posicoesNavio) {
            Node cellNode = cell.getNode();
            cellNode.getStyleClass().remove("cell-ship");
        }
    }

    //Em navios indevidamente posicionados dentro mas com células foras do mapa, este método reseta as células que foram posicionadas corretamente
    private void desfazerNavio(List<CellButton> posicoesNavio) {
        for (CellButton cell : posicoesNavio) {
            cell.reset();
        }
    }
    private void desfazerNavio1(Ship navio) {
        for (CellButton cell : navio.getPosition()) {
            cell.reset();
        }
    }

    private void atiraMirados(Board b){
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                CellButton cell = b.getCell(row, col);
                if (cell.getAimed()){
                    cell.setAimed(false);
                    cell.hit();

                    System.out.println("ATIRAMIRADOS Célula x: " + cell.getCol() + " y: " + cell.getRow() + " Estado: " + cell.getState() + " isHit(): " + cell.isHit());
                }
            }
        }

        for (Ship ship : b.getShips()) {
            if (!ship.isAlive()) {
                b.setNumShips(b.getNumShips() -1);
            }
        }

    }

    //Corrige alguns erros e também é responsável por pintar as células de um Board recebido no parâmetro
    private void updateBoard(Board b) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                CellButton cell = b.getCell(row, col);
                Node cellNode = cell.getNode();

                if (cellNode != null) {
                    cellNode.getStyleClass().removeAll("cell-ship", "cell-hit", "cell-aimed", "cell-ship-hit");

                    if (cell.getAimed()){
                        cellNode.getStyleClass().add("cell-aimed");
                    } else {
                        if (cell.getState() == CellButton.State.SHIP && b == game.getPlayer1().getBoard()){
                            cellNode.getStyleClass().add("cell-ship");
                        }
                    }

                    if (cell.isHit()){
                        if (cell.getState() == CellButton.State.SHIP){
                            cellNode.getStyleClass().add("cell-ship-hit");
                            //cell.setState(CellButton.State.HIT);
                        } else if (cell.getState() == CellButton.State.WATER) {
                            cellNode.getStyleClass().add("cell-hit");
                            //cell.setState(CellButton.State.HIT);
                        }
                    }

                }
            }
        }
    }

    //atualiza a label principal de comunicação com o jogador
    public void updateLabel(String s){
        label.setText(s);
    }


    //serve para setar o posicionamento do navio para de pé ou deitado(padrão)
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



}
