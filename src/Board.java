import java.util.Scanner;

class Board {

    public enum GameState { PLAYING, DRAW, PLAYER_WON, COMP_WON }

    private static Scanner scanner = new Scanner(System.in);

    // dimensions of the board
    static final int ROWS = 3,
                     COLS = 3;

    SquareState playerSymbol;
    private GameState gameState;
    private AIOpponent arty;

    Square[][] board;
    private int currentRow, currentCol;

    Board(String str) {
        gameState = GameState.PLAYING;
        if(str.equals("X")){
            playerSymbol = SquareState.X;
            arty = new AIOpponent(SquareState.O, this);
        }
        else{
            playerSymbol = SquareState.O;
            arty = new AIOpponent(SquareState.X, this);
        }
        board = new Square[ROWS][COLS];
    }


    void initialize() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = new Square();
                board[row][col].empty();
            }
        }
        print();
        play();
    }

    private void play(){
        while(gameState == GameState.PLAYING) {
            if (playerSymbol == SquareState.X) {
                playerMove();
                print();
                updateGameState(playerSymbol);
                if (gameState == GameState.PLAYING) {
                    compMove();
                    print();
                    updateGameState(arty.compSymbol);
                }
            }
            if (playerSymbol == SquareState.O) {
                compMove();
                print();
                updateGameState(arty.compSymbol);
                if (gameState == GameState.PLAYING)
                    playerMove();
                print();
                updateGameState(playerSymbol);
            }
        }
    }

    private void playerMove() {
        boolean validInput = false;
        while(!validInput) {
            System.out.print("\nEnter your move (row# column#): ");
            if (scanner.hasNextInt()) {
                int row = scanner.nextInt() - 1;
                if (scanner.hasNextInt()) {
                    int col = scanner.nextInt() - 1;
                    if (row >= 0 && row < Board.ROWS
                            && col >= 0
                            && col < Board.COLS
                            && board[row][col].state == SquareState.EMPTY) {
                        setSquare(row, col, playerSymbol);
                        validInput = true;
                    } else {
                        System.out.println("That square is already occupied.");
                    }
                } else {
                    System.out.println("Please input a valid move (row# column#)");
                    scanner.next();
                }
            } else {
                System.out.println("Please input a valid move");
                scanner.next();
                scanner.next();
            }
            System.out.println("\n");
        }
    }

    private void compMove(){
        System.out.println("\nComputer's turn...\n");
        int[] bestMove = arty.getBestMove();
        setSquare(bestMove[0], bestMove[1], arty.compSymbol);
    }

    private void setSquare(int r, int c, SquareState sS){
        board[r][c].state = sS;
        currentRow = r;
        currentCol = c;
    }

    private void updateGameState(SquareState sS) {
        if (checkWin(sS)) {
            if(sS == playerSymbol) {
                gameState = GameState.PLAYER_WON;
                System.out.println("\nYou won!");
            } else {
                gameState = GameState.COMP_WON;
                System.out.println("\nThe computer won!");
            }
        } else if (checkDraw()) {
            gameState = GameState.DRAW;
            System.out.println("\nIt's a draw!");
        }
    }

    private boolean checkDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col].state == SquareState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }


    boolean checkWin(SquareState sS) {
        boolean win = false;
        if (board[currentRow][0].state == sS
                && board[currentRow][1].state == sS
                && board[currentRow][2].state == sS
                || board[0][currentCol].state == sS
                && board[1][currentCol].state == sS
                && board[2][currentCol].state == sS
                || currentRow == currentCol
                && board[0][0].state == sS
                && board[1][1].state == sS
                && board[2][2].state == sS
                || currentRow + currentCol == 2
                && board[0][2].state == sS
                && board[1][1].state == sS
                && board[2][0].state == sS){
            win = true;
        }
        return win;
    }


    private void print() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col].print();
                if (col < COLS - 1) System.out.print("|");
            }
            System.out.println();
            if (row < ROWS - 1) {
                System.out.println("-----------");
            }
        }
    }
}