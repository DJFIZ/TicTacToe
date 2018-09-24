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

    SquareState compSymbol;
    private Board board;

    AIOpponent(SquareState cS, Board b) {
        compSymbol = cS;
        board = b;
    }


    int[] getBestMove() {
        int[] bestMove = miniMax(2, compSymbol, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] {bestMove[1], bestMove[2]};
    }


    private List<int[]> getAvailableMoves() {
        List<int[]> possibleMoves = new ArrayList<>();

        if (board.checkWin(board.playerSymbol) || board.checkWin(compSymbol)) {
            return possibleMoves;
        }

        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.board[row][col].state == SquareState.EMPTY) {
                    possibleMoves.add(new int[] {row, col});
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

        if (board.board[r1][c1].state == compSymbol) {
            lineScore = 1;
        } else if (board.board[r1][c1].state == board.playerSymbol) {
            lineScore = -1;
        }

        if (board.board[r2][c2].state == compSymbol) {
            if (lineScore == 1) {
                lineScore = 10;
            } else if (lineScore == -1) {
                return 0;
            } else {
                lineScore = 1;
            }
        } else if (board.board[r2][c2].state == board.playerSymbol) {
            if (lineScore == -1) {
                lineScore = -10;
            } else if (lineScore == 1) {
                return 0;
            } else {
                lineScore = -1;
            }
        }

        if (board.board[r3][c3].state == compSymbol) {
            if (lineScore > 0) {
                lineScore *= 10;
            } else if (lineScore < 0) {
                return 0;
            } else {
                lineScore = 1;
            }
        } else if (board.board[r3][c3].state == board.playerSymbol) {
            if (lineScore < 0) {
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

        int score;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            score = evaluate();
            return new int[] {score, bestRow, bestCol};
        } else {
            for (int[] move : nextMoves) {
                board.board[move[0]][move[1]].state = sS;
                if (sS == compSymbol) {
                    score = miniMax(depth - 1, board.playerSymbol, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // compSymbol is minimizing player
                    score = miniMax(depth - 1, compSymbol, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                board.board[move[0]][move[1]].state = SquareState.EMPTY;
                if (alpha >= beta) { break; }
            }
            if(sS == compSymbol)
                return new int[] {alpha, bestRow, bestCol};
            else
                return new int[] {beta, bestRow, bestCol};
        }
    }
}
