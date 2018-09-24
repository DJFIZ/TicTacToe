//Board.java
//board manages the game itself, prompting the player and printing the board

import java.util.Scanner;

class Board {

    public enum GameState { PLAYING, DRAW, PLAYER_WON, COMP_WON } //various symbols to indicate what is happening in the game

    private static Scanner scanner = new Scanner(System.in); //scanner initialized to take input from user

    //dimensions of the board
    static final int ROWS = 3,
                     COLS = 3;

    SquareState playerSymbol; //symbol chosen by the player
    private GameState gameState; //state of the game
    private AIOpponent arty; //AI player

    Square[][] board; //a new board of squares

    //variables used to check whether the game has been decided at these coords
    private int currentRow,
                currentCol;

    //constructor takes a string and determines what symbol the player chose
    //it then sets the AI player to have the opposite symbol
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

    //creates a new board of empty squares dictated by the dimensions (3x3)
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

    //play manages much of the game
    //while an ending condition has not been met, the user player and AI player alternate taking turns
    //following each player's turn, updateGameState is called to check if the gameState needs to be changed
    private void play(){
        while(gameState == GameState.PLAYING) {
            //Since X goes first, game checks to see what symbol each player is
            if (playerSymbol == SquareState.X) { //this chunk occurs only when player goes first
                playerMove();
                print();
                updateGameState(playerSymbol);
                if (gameState == GameState.PLAYING) {
                    compMove();
                    print();
                    updateGameState(arty.compSymbol);
                }
            }
            if (playerSymbol == SquareState.O) { //this chunk occurs only when the computer goes first
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


    /*prompts the player for coordinate entry based on the following grid

            1,1 | 1,2 | 1,3
            ---------------
            2,1 | 2,2 | 2,3
            ---------------
            3,1 | 3,2 | 3,3
     */
    private void playerMove() {
        boolean validInput = false;
        while(!validInput) {
            System.out.print("\nEnter your move (row# column#): ");
            //scanner checks to ensure that only integers are entered
            if (scanner.hasNextInt()) {
                int row = scanner.nextInt() - 1;
                if (scanner.hasNextInt()) {
                    int col = scanner.nextInt() - 1;
                    //if statement checks to ensure that the input is within scope of the possible squares
                    //and if the square desired is empty
                    if (row >= 0 && row < Board.ROWS
                            && col >= 0
                            && col < Board.COLS
                            && board[row][col].state == SquareState.EMPTY) {
                        setSquare(row, col, playerSymbol);
                        validInput = true;
                    } else {
                        System.out.println("That square is already occupied or the value entered is too large.");
                    }
                } else {
                    System.out.println("Please input a valid move (row# column#)");
                    scanner.next();
                }
            } else {
                System.out.println("Please input a valid move");
                scanner.next(); //needed to properly read after an invalid input
                scanner.next();
            }
            System.out.println("\n");
        }
    }

    //calls the AI player to move, getting its best move
    private void compMove(){
        System.out.println("\nComputer's turn...\n");
        int[] bestMove = arty.getBestMove();
        setSquare(bestMove[0], bestMove[1], arty.compSymbol);
    }


    //sets the square to hold the given symbol/state
    private void setSquare(int r, int c, SquareState sS){
        board[r][c].state = sS;
        currentRow = r;
        currentCol = c;
    }


    //checks to see if the most recent move has ended the game, displaying what the outcome is if it did
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

    //checks to see if any squares are empty, returning false if one is; returns true if all squares have a symbol
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

    //checkWin checks if the symbol passed has won any combination given the most recent move
    //Wins in TicTacToe are evaluated per full row, full column or on either diagonal
    boolean checkWin(SquareState sS) {
        boolean win = false;
        if (    //checks win at the current row
                   board[currentRow][0].state == sS
                && board[currentRow][1].state == sS
                && board[currentRow][2].state == sS
                //checks win at current column
                || board[0][currentCol].state == sS
                && board[1][currentCol].state == sS
                && board[2][currentCol].state == sS
                //checks win at diagonal 1
                || currentRow == currentCol
                && board[0][0].state == sS
                && board[1][1].state == sS
                && board[2][2].state == sS
                //checks win at diagonal 2
                || currentRow + currentCol == 2
                && board[0][2].state == sS
                && board[1][1].state == sS
                && board[2][0].state == sS){
            win = true;
        }
        return win;
    }

    //prints the board and any symbols contained within its squares
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