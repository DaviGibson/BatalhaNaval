package br.ufrn.imd.visao;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import br.ufrn.imd.visao.*;

public class Controller {

    @FXML
    private AnchorPane gamePane;
    @FXML
    private GridPane playerGrid;

    @FXML
    private GridPane computerGrid;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label label;

    @FXML
    public void initialize() {
        gamePane.setStyle("-fx-background-color: #B9D9EB;");
        createGrid(playerGrid,"jogador");
        createGrid(computerGrid, "computador");
    }

    private void createGrid(GridPane grid, String gridType) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                StackPane cell = new StackPane();

                // Adiciona classe própria da célula
                cell.getStyleClass().add("cell");
                // Adiciona classes CSS específicas
//                if (gridType.equals("player")) {
//                    cell.getStyleClass().add("cell-player");
//                } else if (gridType.equals("computer")) {
//                    cell.getStyleClass().add("cell-computer");
//                }

                grid.add(cell, col, row);
                cell.setOnMouseClicked(event -> handleCellClick(event, gridType));
            }
        }
    }

    // Método para lidar com o clique em uma célula
    private void handleCellClick(MouseEvent event, String gridType) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        if (clickedNode != null) {
            int colIndex = GridPane.getColumnIndex(clickedNode);
            int rowIndex = GridPane.getRowIndex(clickedNode);

            System.out.println("Célula clicada em: [" + rowIndex + ", " + colIndex + "] no grid: " + gridType);
            updateLabel("Célula clicada em: [" + rowIndex + ", " + colIndex + "] no grid: " + gridType);
            // Aqui você pode adicionar lógica específica para cada grid
            if (gridType.equals("player")) {
                // Ação específica para o GridPane do jogador
                //System.out.println("Ação do jogador.");
            } else if (gridType.equals("computer")) {
                // Ação específica para o GridPane do computador
                //System.out.println("Ação do computador.");
            }
        }
    }

    public void updateLabel(String s){
        label.setText(s);
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
