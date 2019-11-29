package chess;


/**********************************************************************
 * This class represents the abstract attributes and operations of a
 * chess piece for a chess simulator.  Class attributes include a
 * player who owns the piece.  Class operations include determining
 * whether a prospective move is valid in general (for any piece).
 *
 * @suthor Andrew Weston, Owen Ganzer, Dalton Claybaugh
 * @version Winter 2019
 *********************************************************************/
public abstract class ChessPiece implements IChessPiece {

    /** Player who owns "this" piece */
    private Player owner;

    /******************************************************************
     * This constructor establishes the owner of a given chess piece.
     *
     * @param player is an enum representing black or white player.
     *****************************************************************/
    protected ChessPiece(Player player) {
        this.owner = player;
    }


    /******************************************************************
     * This method returns the chess piece object's type in a String
     * format.
     *
     * @return a String representing the piece's type.
     *****************************************************************/
    public abstract String type();


    /******************************************************************
     * This method returns the chess piece object's enum owner.
     *
     * @return Player is an enum representing black or white player.
     *****************************************************************/
    public Player player() {
        return owner;
    }


    /******************************************************************
     * This method checks whether a prospective move is valid (in
     * general) for any piece.
     *
     * @param move is a prospective move of "this" piece.
     * @param board is a 2-D IChessPiece Array representing the game
     * board.
     * @boolean is true when the param move is a legal chess move.
     *****************************************************************/
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
        //check you are not overtaking your own piece
        if ((board[move.toRow][move.toColumn] != null) && (this.owner == board[move.toRow][move.toColumn].player()))
            return false;
        else return true;
    }

    /******************************************************************
     * The equals method compares "this" ChessPiece extending object
     * to the parameter piece for piece owner and piece type.
     *
     * @param piece is an object for comparison to "this" ChessPiece.
     * @return is true if the parameter is of the same type and owner.
     *****************************************************************/
    @Override
    public boolean equals(Object piece) {
        boolean isEqual = false;
        if (piece instanceof ChessPiece)
            if ((this.owner == ((ChessPiece) piece).player()) &&
                    (this.type().equals(((ChessPiece) piece).type())))
                isEqual = true;
        return isEqual;
    }
}