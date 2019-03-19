package chess;

public class Knight extends ChessPiece {

    public Knight(Player player) {
        super(player);
    }

    public String type() {
        return "Knight";
    }

    @Override
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        if (!super.isValidMove(move, board))
            return false;

        if ((Math.abs(move.fromRow - move.toRow) == 2) &&
                (Math.abs(move.fromColumn - move.toColumn) == 1))
            return true;

        if ((Math.abs(move.fromColumn - move.toColumn) == 2) &&
                (Math.abs(move.fromRow - move.toRow) == 1))
            return true;

        return false;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Knight clone = (Knight) super.clone();
        return clone;
    }
}