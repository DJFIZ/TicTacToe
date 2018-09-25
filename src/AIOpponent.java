/*
The AI uses the following coordinate system when determining the most optimal move.

0,0 | 0,1 | 0,2
---------------
1,0 | 1,1 | 1,2
---------------
2,0 | 2,1 | 2,2

 */

import java.util.ArrayList;
import java.util.List;

class AIOpponent {

    SquareState compSymbol; //Symbol for the AI player
    private Board board;

    //Constructor receives the current board and what symbol it will be in the game
    AIOpponent(SquareState cS, Board b) {
        compSymbol = cS;
        board = b;
    }

    //calls the search algorithm and returns the coords of the best move
    int[] getBestMove() {
        int[] bestMove = miniMax(2, compSymbol, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{bestMove[1], bestMove[2]};
    }

    //gets available spaces by finding all squares on the board with an empty state
    private List<int[]> getAvailableMoves() {
        List<int[]> possibleMoves = new ArrayList<>(); //list to hold possible moves

        //returns an empty list if either player has won (prevents comp from making a move once the game has concluded)
        if (board.checkWin(board.playerSymbol) || board.checkWin(compSymbol)) {
            return possibleMoves;
        }

        //loops to add all empty spaces to the list
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.board[row][col].state == SquareState.EMPTY) {
                    possibleMoves.add(new int[]{row, col});
                }
            }
        }
        return possibleMoves;
    }

    //Evaluates all possible win conditions in TicTacToe through examining each row, column, and diagonal
    private int evaluate() {
        int totalScore = 0;
        totalScore += evaluateLine(0, 0, 0, 1, 0, 2);
        totalScore += evaluateLine(1, 0, 1, 1, 1, 2);
        totalScore += evaluateLine(2, 0, 2, 1, 2, 2);
        totalScore += evaluateLine(0, 0, 1, 0, 2, 0);
        totalScore += evaluateLine(0, 1, 1, 1, 2, 1);
        totalScore += evaluateLine(0, 2, 1, 2, 2, 2);
        totalScore += evaluateLine(0, 0, 1, 1, 2, 2);
        totalScore += evaluateLine(0, 2, 1, 1, 2, 0);
        return totalScore;
    }

    //Evaluates an individual line and assigns a score
    //The line score is incremented or decremented depending on what symbol is present in each square
    //Lines evaluated that find the player is about to win receive an excessively negative line score
    //Lines evaluated that find the computer is about to win receive an excessively positive line score
    private int evaluateLine(int r1, int c1, int r2, int c2, int r3, int c3) {
        int lineScore = 0;

        //checks the status and square of the first square
        if (board.board[r1][c1].state == compSymbol) {
            lineScore = 1;
        } else if (board.board[r1][c1].state == board.playerSymbol) {
            lineScore = -1;
        }

        //checks the status and square of the second square
        if (board.board[r2][c2].state == compSymbol) {
            if (lineScore == 1) { //if the first square also had the same symbol, gives heavier weight
                lineScore = 10;
            } else if (lineScore == -1) {
                return 0;
            } else {
                lineScore = 1;
            }
        } else if (board.board[r2][c2].state == board.playerSymbol) {
            if (lineScore == -1) { //if the first square also had the same symbol, gives heavier weight
                lineScore = -10;
            } else if (lineScore == 1) {
                return 0;
            } else {
                lineScore = -1;
            }
        }

        //checks the status and square of the third square
        if (board.board[r3][c3].state == compSymbol) {
            if (lineScore > 0) { //if the other two squares also had the same symbol, gives immense weight
                lineScore *= 10;
            } else if (lineScore < 0) {
                return 0;
            } else {
                lineScore = 1;
            }
        } else if (board.board[r3][c3].state == board.playerSymbol) {
            if (lineScore < 0) { //if the other two squares also had the same symbol, gives immense weight
                lineScore *= 10;
            } else if (lineScore > 1) {
                return 0;
            } else {
                lineScore = -1;
            }
        }
        return lineScore;
    }

    //miniMax is the algorithm that finds the next best move for the AI
    //alpha-beta pruning to reduce the number of nodes that need to be evaluated
    private int[] miniMax(int depth, SquareState sS, int alpha, int beta) {
        List<int[]> nextMoves = getAvailableMoves();

        int score; //keeps track of the 'value' each move gives
        int bestRow = -1, //initializes variables for the best row/col
                bestCol = -1;

        //if there are no moves to make or intended depth has been reached, evaluates the best move and returns the coords
        if (nextMoves.isEmpty() || depth == 0) {
            score = evaluate();
            return new int[]{score, bestRow, bestCol};
        } else {
            for (int[] tmpMove : nextMoves) {//new temporary list of available moves
                board.board[tmpMove[0]][tmpMove[1]].state = sS; //temporarily sets a square to sS for evaluation
                //recursively loops to maximize the computer
                if (sS == compSymbol) {
                    score = miniMax(depth - 1, board.playerSymbol, alpha, beta)[0];
                    if (score > alpha) { //comparison to alpha to prevent unnecessary evaluation
                        alpha = score;
                        bestRow = tmpMove[0];
                        bestCol = tmpMove[1];
                    }
                } else {
                    //recursively loops to minimize the user
                    score = miniMax(depth - 1, compSymbol, alpha, beta)[0];
                    if (score < beta) { //comparison to beta to prevent unnecessary evaluation
                        beta = score;
                        bestRow = tmpMove[0];
                        bestCol = tmpMove[1];
                    }
                }
                board.board[tmpMove[0]][tmpMove[1]].state = SquareState.EMPTY; //resets the temporary square to empty
                if (alpha >= beta) {
                    break;
                } //breaks loop if maximizing evaluates to a higher or same score as minimizing
            }
            if (sS == compSymbol)
                return new int[]{alpha, bestRow, bestCol}; //returns best move for maximizing computer
            else
                return new int[]{beta, bestRow, bestCol}; //returns best move for minimizing user
        }
    }
}
