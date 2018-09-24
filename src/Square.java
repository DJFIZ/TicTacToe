
class Square {

    SquareState state;

    Square() {
        empty();
    }

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

    void empty() {
        state = SquareState.EMPTY;
    }
}
