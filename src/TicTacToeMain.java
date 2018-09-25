//TicTacToeMain.java

import javax.swing.*;

public class TicTacToeMain {

    public static void main(String[] args) {
        TicTacToeMain newGame = new TicTacToeMain();
        newGame.promptPlayer();
    }

    //prompts the player on whether they wish to play as X or O
    private void promptPlayer() {
        Board board;
        Object[] options = {"X", "O"};
        int n = JOptionPane.showOptionDialog(null,
                "Would you like to play as X's or O's?",
                "Pong", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (n == JOptionPane.YES_OPTION) {
            board = new Board("X");
            board.initialize();
        } else if (n == JOptionPane.NO_OPTION) {
            board = new Board("O");
            board.initialize();
        } else
            System.exit(0);
    }
}
