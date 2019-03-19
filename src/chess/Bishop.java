package chess;

public class Bishop extends ChessPiece {

    public Bishop(Player player) {
        super(player);
    }

    public String type() {
        return "Bishop";
    }

    @Override
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        if (!super.isValidMove(move, board))
            return false;
        if (Math.abs(move.fromRow - move.toRow) != Math.abs(move.fromColumn - move.toColumn))
            return false;
        int i = 1;

        if ((move.fromRow > move.toRow) && (move.fromColumn > move.toColumn)) {
            while (i < Math.abs(move.fromRow - move.toRow)) {
                if (board[move.fromRow - i][move.fromColumn - i] != null)
                    return false;
                i++;
            }
        }

        if ((move.fromRow < move.toRow) && (move.fromColumn < move.toColumn)) {
            while (i < Math.abs(move.fromRow - move.toRow)) {
                if (board[move.fromRow + i][move.fromColumn + i] != null)
                    return false;
                i++;
            }
        }

        if ((move.fromRow > move.toRow) && (move.fromColumn < move.toColumn)) {
            while (i < Math.abs(move.fromRow - move.toRow)) {
                if (board[move.fromRow - i][move.fromColumn + i] != null)
                    return false;
                i++;
            }
        }

        if ((move.fromRow < move.toRow) && (move.fromColumn > move.toColumn)) {
            while (i < Math.abs(move.fromRow - move.toRow)) {
                if (board[move.fromRow + i][move.fromColumn - i] != null)
                    return false;
                i++;
            }
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Bishop clone = (Bishop) super.clone();
        return clone;
    }
}