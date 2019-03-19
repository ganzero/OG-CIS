package chess;

public class Rook extends ChessPiece {

    int moveCounter;

    public Rook(Player player) {
        super(player);
        moveCounter = 0;
    }


    public String type() {
        return "Rook";
    }

    public boolean isCastling(Move move, IChessPiece[][] board) {         //FIXME does castlingMove require action from rook or just game logic; castlingMove is initiated by king
        //probably not because moving rook to castlingMove position is also legal but has different meaning
        return false;
    }

    @Override
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        if (!super.isValidMove(move, board))
            return false;
        if ((isUpDownLeftRightMove(move, board)) || (isCastling(move, board)))
            return true;
        else return false;
    }

    public boolean isUpDownLeftRightMove(Move move, IChessPiece[][] board) {
        //if row and column both change the move is illegal
        if ((move.fromRow != move.toRow) && (move.fromColumn != move.toColumn))
            return false;

        else if (checkClearPath(move, board))
            return true;

        else return false;
    }

    public boolean checkClearPath(Move move, IChessPiece[][] board) {
        if (move.fromRow < move.toRow) {
            int checkRow = move.fromRow + 1;
            while (checkRow < move.toRow) {
                if (board[checkRow][move.toColumn] != null)
                    return false;
                checkRow++;
            }
            return true;
        }
        if (move.fromRow > move.toRow) {
            int checkRow = move.fromRow - 1;
            while (checkRow > move.toRow) {
                if (board[checkRow][move.toColumn] != null)
                    return false;
                checkRow--;
            }
            return true;
        }
        if (move.fromColumn < move.toColumn) {
            int checkColumn = move.fromColumn + 1;
            while (checkColumn < move.toColumn) {
                if (board[move.toRow][checkColumn] != null)
                    return false;
                checkColumn++;
            }
            return true;
        }
        if (move.fromColumn > move.toColumn) {
            int checkColumn = move.fromColumn - 1;
            while (checkColumn > move.toColumn) {
                if (board[move.toRow][checkColumn] != null)
                    return false;
                checkColumn--;
            }
            return true;
        } else return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Rook clone = (Rook) super.clone();
        clone.moveCounter = this.moveCounter;
        return clone;
    }

    @Override
    public String toString() {
        return "Rook{" +
                "moveCounter=" + moveCounter +
                '}';
    }
}