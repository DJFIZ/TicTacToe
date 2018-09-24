//Square.java
//Square is an object used to create a board.
class Square {

    SquareState state; //Used to store the symbol the square has

    Square() {
        empty();
    }

    //Prints the symbol held in the square
    void print() {
        if(state == SquareState.X) {
            System.out.print(" X ");
        }
        else if(state == SquareState.O) {
            System.out.print(" O ");
        }
        else if(state == SquareState.EMPTY) {
            System.out.print("   ");
        }
    }

    //method to set the squares state to empty
    void empty() {
        state = SquareState.EMPTY;
    }
}
