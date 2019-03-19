package chess;

public class King extends ChessPiece {

    protected int moveCounter;

    public King(Player player) {
        super(player);
        moveCounter = 0;
    }

    public String type() {
        return "King";
    }

    @Override
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        if (!super.isValidMove(move, board))
            return false;
        if (isCastling(move, board)) {
            if ((move.toRow == 0) || (move.toRow == 7)) {
                if (move.toColumn == board[0].length - 2)
                    if (board[move.toRow][move.toColumn + 1] instanceof Rook)
                        return ((Rook) board[move.toRow][move.toColumn + 1]).moveCounter == 0;
                if (move.toColumn == board[0].length - 6)
                    if (board[move.toRow][move.toColumn - 2] instanceof Rook)
                        return ((Rook) board[move.toRow][move.toColumn - 2]).moveCounter == 0;
            }
        }
        //move is invalid if king moves more than one space
        return !((move.toRow < move.fromRow - 1) ||
                (move.toRow > move.fromRow + 1) ||
                (move.toColumn < move.fromColumn - 1) ||
                (move.toColumn > move.fromColumn + 1));
    }

    public boolean isCastling(Move move, IChessPiece[][] board) {
        boolean castling = false;
        if (((this.moveCounter ==0) && (validEndPosition(move, board)) &&
                (this.isClear(move, board)))) {
            move.castlingMove = true;
            castling = true;
        }
        return castling;
    }

    public boolean validEndPosition(Move move, IChessPiece[][] board) {
        if ((move.toRow == 0) || (move.toRow == 7))
            return (move.toColumn == board[0].length - 2) ||
                    (move.toColumn == board[0].length - 6);
        else return false;
    }

    public boolean isClear(Move move, IChessPiece[][] board) {
        if (move.fromColumn > move.toColumn)
            return ((board[move.fromRow][move.fromColumn - 1] == null) &&
                    (board[move.fromRow][move.fromColumn - 2] == null) &&
                    (board[move.fromRow][move.fromColumn - 3] == null));

        if (move.fromColumn < move.toColumn)
            return ((board[move.fromRow][move.fromColumn + 1] == null) &&
                    (board[move.fromRow][move.fromColumn + 2] == null));
        else return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        King clone = (King) super.clone();
        clone.moveCounter = this.moveCounter;
        return clone;
    }

    @Override
    public String toString() {
        return "King{" +
                "moveCounter=" + moveCounter +
                '}';
    }
}