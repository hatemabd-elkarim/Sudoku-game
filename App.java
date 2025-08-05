package com.mycompany.sudokuapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * @title     : Sudoku game
 * @author    : Hatem Ayman
 * @github    : hatemabd-elkarim
 * @edx       : hatemabd-elkarim
 * @country   : Egypt
 * @date      : Mon 5 Aug 2025
*/
public class App extends Application {
    
    static BoardGenerator board;
    static int[][] solution;
    static final int SIZE = 9;
    TextField[][] cells = new TextField[SIZE][SIZE];
    private TextField selectedCell = null;
    
    private Label infoLabel;
    private Timeline timer;
    private int secondsElapsed;
    private int mistakes = 0;
    private final int MAX_MISTAKES = 3;
    private String difficulty;
    private VBox GameContainer;
    private HBox numberSelector;
    GridPane grid;
    Stage primaryStage;
    
    @Override
    public void start(Stage stage) {
    
    primaryStage = stage;
    showStartScene();
    
    }


    // Scene 1: Welcome screen with "New Game"
    private void showStartScene() {
    VBox layout = new VBox(30);
    layout.setAlignment(Pos.CENTER);
    layout.setStyle("-fx-background-color: #FAFAFA;");

    Label title = new Label("Welcome to Sudoku!");
    title.setStyle(
        "-fx-font-size: 32px;" +
        "-fx-text-fill: #2E3A59;" +
        "-fx-font-weight: bold;"
    );

    Button newGameButton = new Button("New Game");
    newGameButton.setStyle(
        "-fx-background-color: #272AF5;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 10;" +
        "-fx-cursor: hand;"
    );

    // Hover effect
    newGameButton.setOnMouseEntered(e -> newGameButton.setStyle(
        "-fx-background-color: #5C6F87;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 10;" +
        "-fx-cursor: hand;"
    ));

    newGameButton.setOnMouseExited(e -> newGameButton.setStyle(
        "-fx-background-color: #272AF5;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 10;" +
        "-fx-cursor: hand;"
    ));

    newGameButton.setOnAction(e -> showDifficultySelection());

    layout.getChildren().addAll(title, newGameButton);

    Scene scene = new Scene(layout, 500, 400);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Sudoku");
    primaryStage.show();
}

    // Scene 2: Choose difficulty
    private void showDifficultySelection() {
    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.setStyle("-fx-background-color: #F9F9F9;");

    Label prompt = new Label("Choose Difficulty");
    prompt.setStyle(
        "-fx-font-size: 24px;" +
        "-fx-text-fill: #2E3A59;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 10 0 20 0;"
    );

    Button easy = createStyledButton("Easy", Level.Easy);
    Button medium = createStyledButton("Medium", Level.Medium);
    Button hard = createStyledButton("Hard", Level.Hard);
    Button expert = createStyledButton("Expert", Level.Expert);

    layout.getChildren().addAll(prompt, easy, medium, hard, expert);
    Scene scene = new Scene(layout, 500, 450);
    primaryStage.setScene(scene);
}

    // Scene 3: Launch the game
    private void startGame(Level level) {
        difficulty = level+"";
        mistakes = 0;
        secondsElapsed = 0;
        board = new BoardGenerator(level);
        solution = board.solver.solution;
        
        GameContainer = new VBox(20);
        GameContainer.setAlignment(Pos.CENTER);

        // Create and style the info label
        infoLabel = new Label();
        infoLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-text-fill: #2d3436;" +
            "-fx-background-color: #dfe6e9;" +
            "-fx-padding: 10px 15px;" +
            "-fx-background-radius: 8;"
        );

        updateInfoLabel();

        grid = makeGrid();
        numberSelector = makeNumberSelector();

        GameContainer.getChildren().addAll(infoLabel, grid, numberSelector);
        Scene gameScene = new Scene(GameContainer, 600, 700);
        primaryStage.setScene(gameScene);
        startTimer();
    }
    
    
    GridPane makeGrid() {
    grid = new GridPane();
    grid.setAlignment(Pos.CENTER);

    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            TextField cell = new TextField();
            cell.setPrefSize(50, 50);
            cell.setAlignment(Pos.CENTER);
            cell.setFocusTraversable(false);
            cell.setEditable(false);  // Always managed manually

            int value = board.board[row][col];

            if (value != 0) {
                // Pre-filled cell
                cell.setText(String.valueOf(value));
                cell.setUserData("locked");
                cell.setMouseTransparent(true);  // Not clickable
                cell.setStyle(
                    "-fx-text-fill: #45556C;" +
                    "-fx-font-size: 18;" +
                    "-fx-background-color: #E0E0E0;" +
                    "-fx-opacity: 1.0;" +
                    computeBorderStyle(row, col)
                );
            } else {
                // Editable cell
                cell.setUserData("editable");
                cell.setMouseTransparent(false);
                cell.setStyle(
                    "-fx-text-fill: #45556C;" +
                    "-fx-font-size: 18;" +
                    "-fx-background-color: white;" +
                    "-fx-focus-color: #66b2ff;" +
                    "-fx-faint-focus-color: transparent;" +
                    computeBorderStyle(row, col)
                );

                int finalRow = row;
                int finalCol = col;

                cell.setOnMouseClicked(e -> {
                if (!"locked".equals(cell.getUserData())) {
                    clearHighlights();
                    highlightRowAndCol(finalRow, finalCol);
                    selectedCell = cell;

                    if ("wrong".equals(cell.getUserData())) {
                        // Apply wrong + selected styling
                        cell.setStyle(
                            "-fx-text-fill: white;" +                 // white text because background is blue
                            "-fx-font-size: 18;" +
                            "-fx-background-color: #66B2FF;" +        // selected cell highlight
                            computeBorderStyle(finalRow, finalCol)
                        );
                    } else {
                        // Normal editable cell selected
                        cell.setStyle(
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18;" +
                            "-fx-background-color: #66B2FF;" +
                            computeBorderStyle(finalRow, finalCol)
                        );
                    }
                }
            });

            }

            grid.add(cell, col, row);
            cells[row][col] = cell;
        }
    }

    return grid;
}
    
    // 3*3 border logic
    String computeBorderStyle(int row, int col) {
    int top = (row % 3 == 0) ? 2 : 1;
    int left = (col % 3 == 0) ? 2 : 1;
    int bottom = (row == SIZE - 1) ? 2 : 1;
    int right = (col == SIZE - 1) ? 2 : 1;

    return String.format("-fx-border-color: black; -fx-border-width: %d %d %d %d;", top, right, bottom, left);
}

    // Clear highlight from all cells
    void clearHighlights() {
    for (int r = 0; r < SIZE; r++) {
        for (int c = 0; c < SIZE; c++) {
            TextField cell = cells[r][c];
            Object userData = cell.getUserData();

            if ("wrong".equals(userData)) {
                // Keep red text for wrong cells
                cell.setStyle(
                    "-fx-text-fill: #FB2C36;" +
                    "-fx-font-size: 18;" +
                    "-fx-background-color: white;" +
                    computeBorderStyle(r, c)
                );
            } else if ("locked".equals(userData)) {
                // Locked cells - don't change anything
                continue;
            } else {
                // Reset style for editable cells
                cell.setStyle(
                    "-fx-text-fill: #45556C;" +
                    "-fx-font-size: 18;" +
                    "-fx-background-color: white;" +
                    computeBorderStyle(r, c)
                );
            }
        }
    }
}

    // Highlight entire row and column
    void highlightRowAndCol(int row, int col) {
    for (int i = 0; i < SIZE; i++) {
        // Row
        if (i != col && board.board[row][i] == 0) {
            String color = "wrong".equals(cells[row][i].getUserData()) ? "#FB2C36" : "#45556C";
            cells[row][i].setStyle(
                "-fx-background-color: #D9EFFF;" +
                "-fx-font-size: 18;" +
                "-fx-text-fill: " + color + ";" +
                computeBorderStyle(row, i)
            );
        }

        // Column
        if (i != row && board.board[i][col] == 0) {
            String color = "wrong".equals(cells[i][col].getUserData()) ? "#FB2C36" : "#45556C";
            cells[i][col].setStyle(
                "-fx-background-color: #D9EFFF;" +
                "-fx-font-size: 18;" +
                "-fx-text-fill: " + color + ";" +
                computeBorderStyle(i, col)
            );
        }
    }

    // Selected cell itself
    if (board.board[row][col] == 0) {
        String color = "wrong".equals(cells[row][col].getUserData()) ? "#FB2C36" : "white";
        String bg = "wrong".equals(cells[row][col].getUserData()) ? "#66B2FF" : "#66B2FF";
        cells[row][col].setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-font-size: 18;" +
            "-fx-text-fill: " + color + ";" +
            computeBorderStyle(row, col)
        );
    }
}

    private HBox makeNumberSelector() {
    HBox numberRow = new HBox(10);
    numberRow.setAlignment(Pos.CENTER);

    for (int i = 1; i <= 9; i++) {
        Label num = new Label(String.valueOf(i));
        num.setPrefSize(40, 40);
        num.setAlignment(Pos.CENTER);
        num.setStyle(
        "-fx-background-color: #d0e8ff;" + 
        "-fx-border-color: #74b9ff;" +
        "-fx-border-radius: 6;" +
        "-fx-background-radius: 6;" +
        "-fx-font-size: 18px;" +
        "-fx-font-family: 'Segoe UI';" +
        "-fx-text-fill: #2d3436;" +
        "-fx-cursor: hand;" +
        "-fx-padding: 8px;"
    );

        int number = i;

        num.setOnMouseClicked(e -> {
            if (selectedCell == null) return;

            int selectedRow = -1;
            int selectedCol = -1;

            outer:
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (cells[r][c] == selectedCell) {
                        selectedRow = r;
                        selectedCol = c;
                        break outer;
                    }
                }
            }

            if (selectedRow == -1 || selectedCol == -1) return;

            if (number == solution[selectedRow][selectedCol]) {
                // Correct answer
                selectedCell.setText(String.valueOf(number));
                selectedCell.setEditable(false);
                selectedCell.setMouseTransparent(true);
                selectedCell.setUserData("locked");
                selectedCell.setStyle(
                    "-fx-text-fill: #45556C;" +
                    "-fx-font-size: 18;" +
                    "-fx-background-color: white;" +
                    computeBorderStyle(selectedRow, selectedCol)
                );
                selectedCell = null;
                checkWin();

            } else {
                // Wrong answer
                selectedCell.setText(String.valueOf(number));
                selectedCell.setEditable(false);            // No I-beam
                selectedCell.setMouseTransparent(false);     // Still clickable
                selectedCell.setUserData("wrong");
                selectedCell.setStyle(
                    "-fx-text-fill: #FB2C36;" +               // Red text
                    "-fx-font-size: 18;" +
                    "-fx-background-color: white;" +
                    computeBorderStyle(selectedRow, selectedCol)
                );
                
            mistakes++;
            updateInfoLabel();

            if (mistakes >= MAX_MISTAKES) {
                timer.stop();
                showGameOverControls();
            }
            }
        });

        numberRow.getChildren().add(num);
    }

    return numberRow;
}

    private void updateInfoLabel() {
    int minutes = secondsElapsed / 60;
    int seconds = secondsElapsed % 60;
    infoLabel.setText("Difficulty: " + difficulty +
                      "    Mistakes: " + mistakes + " / " + MAX_MISTAKES +
                      "    Time: " + String.format("%02d:%02d", minutes, seconds));
}

    private void startTimer() {
    timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        secondsElapsed++;
        updateInfoLabel();
    }));
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
}

    private Button createStyledButton(String label, Level level) {
    Button btn = new Button(label);
    btn.setPrefWidth(200);
    btn.setPrefHeight(40);

    btn.setStyle(
        "-fx-background-color: #45556C;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 16px;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    );

    btn.setOnMouseEntered(e -> btn.setStyle(
        "-fx-background-color: #5C6F87;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 16px;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    ));

    btn.setOnMouseExited(e -> btn.setStyle(
        "-fx-background-color: #45556C;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 16px;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    ));

    btn.setOnAction(e -> startGame(level));

    return btn;
}
    
    private void showGameOverControls() {
    // Remove number selector (assumes it's the 3rd child)
    GameContainer.getChildren().remove(numberSelector);

    // Create "Game Over" message and button
    VBox gameOverBox = new VBox(10);
    gameOverBox.setAlignment(Pos.CENTER);

    Label gameOverLabel = new Label("Game Over");
    gameOverLabel.setStyle(
    "-fx-font-family: 'Impact';" +
    "-fx-font-size: 48px;" +
    "-fx-text-fill: red;" +
    "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 2, 2);"
);


    Button newGameBtn = new Button("New Game");
    newGameBtn.setStyle(
    "-fx-font-family: 'Impact';" +
    "-fx-font-size: 22px;" +
    "-fx-text-fill: white;" +
    "-fx-background-color: #d62828;" +
    "-fx-background-radius: 10px;" +
    "-fx-padding: 10 20 10 20;" +
    "-fx-cursor: hand;" +
    "-fx-effect: dropshadow(gaussian, black, 3, 0.5, 1, 1);"
);

    
    
    newGameBtn.setOnAction(e -> showDifficultySelection());

    gameOverBox.getChildren().addAll(gameOverLabel, newGameBtn);

    // Add this box in place of number selector
    GameContainer.getChildren().add(gameOverBox);
}
    
    private void checkWin() {
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            TextField cell = cells[row][col];
            String text = cell.getText();

            if (text.isEmpty() || !text.equals(String.valueOf(solution[row][col]))) {
                return;  // Grid is not complete or incorrect cell found
            }
        }
    }

    // All cells are correct and filled
    timer.stop();  // Stop the timer
    showWinUI();
}

    private void showWinUI() {
    GameContainer.getChildren().remove(numberSelector);

    Label winLabel = new Label("You solved it!");
    winLabel.setStyle(
    "-fx-font-family: 'Impact';" +
    "-fx-font-size: 48px;" +
    "-fx-text-fill: limegreen;" +
    "-fx-effect: dropshadow(gaussian, black, 5, 0.6, 2, 2);" +
    "-fx-padding: 10px;"
);
    Button newGameButton = new Button("New Game");
    newGameButton.setStyle(
    "-fx-font-size: 18px;" +
    "-fx-font-weight: bold;" +
    "-fx-font-family: 'Arial Black';" +
    "-fx-text-fill: white;" +
    "-fx-background-color: #32CD32;" +  // LimeGreen
    "-fx-background-radius: 10;" +
    "-fx-padding: 10px 20px;" +
    "-fx-effect: dropshadow(gaussian, black, 4, 0.5, 2, 2);"
);

    
    newGameButton.setOnAction(e -> showDifficultySelection());

    VBox winBox = new VBox(10, winLabel, newGameButton);
    winBox.setAlignment(Pos.CENTER);

    GameContainer.getChildren().add(winBox);
}



    public static void main(String[] args) {
        launch(args);
    }
}
