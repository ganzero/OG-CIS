package chess;

public abstract class ChessPiece implements IChessPiece, Cloneable {

    private Player owner;

    protected ChessPiece(Player player) {
        this.owner = player;
    }

    public abstract String type();

    public Player player() {
        return owner;
    }

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        //get board dimensions
        int boardRow = board.length;
        int boardCol = board[0].length;
        //check move starts/ends on the board
        if ((move.fromRow >= boardRow) || (move.toRow >= boardRow) ||
                (move.fromRow < 0) || (move.toRow < 0) ||
                (move.fromColumn >= boardCol) || (move.toColumn >= boardCol) ||
                (move.fromColumn < 0) || (move.toColumn < 0))
            return false;
        //check the "move" is not to the exact same position
        if (((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)))
            return false;
        //check this piece is in the starting position
//        if (!this.equals(board[move.fromRow][move.fromColumn]))
//            return false;
        if ((board[move.toRow][move.toColumn] != null) && (this.owner == board[move.toRow][move.toColumn].player()))
            return false;
        else return true;
    }

    @Override
    public boolean equals(Object piece) {
        boolean isEqual = false;
        if (piece instanceof ChessPiece)
            if ((this.owner == ((ChessPiece) piece).player()) &&
                    (this.type().equals(((ChessPiece) piece).type())))
                isEqual = true;
        return isEqual;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChessPiece clone = (ChessPiece)super.clone();
        clone.owner = this.owner;
        return clone;
    }
}