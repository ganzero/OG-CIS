package chess;
/**********************************************************************
 * This class represents a pawn piece for a chess simulator.
 * Class operation is determining valid "advance", "capture", and
 * "en passant" moves.
 *
 * @suthor Andrew Weston, Owen Ganzer, Dalton Claybaugh
 * @version Winter 2019
 *********************************************************************/
public class Pawn extends ChessPiece {

    /** Counter that tracks the number of king moves */
    protected int moveCounter;


    /******************************************************************
     * This constructor creates a Pawn object assigned to one player.
     *
     * @param player is an enum representing black or white player.
     *****************************************************************/
    public Pawn(Player player) {
        super(player);
        moveCounter = 0;
    }


    /******************************************************************
     * This method returns the Pawn object's type in a String format.
     *
     * @return a String representing the piece's type.
     *****************************************************************/
    public String type() {
        return "Pawn";
    }


    /******************************************************************
     * This method overrides the ChessPiece isValidMove().
     *
     * @param move is a prospective move of "this" piece.
     * @param board is a 2-D IChessPiece Array representing the game
     * board.
     * @return is true when the param move is a legal chess move for
     * "this" piece.
     *****************************************************************/
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        //check if move is generally valid in ChessPiece class
        if (!super.isValidMove(move, board))
            return false;

        //use helper methods to check valid types of moves
        if ((isAdvancing(move, board)) || (isCapturing(move, board)) || (isEnPassantCapturing(move, board)))
            return true;
        else return false;
    }


    /******************************************************************
     * This helper method checks if the prospective pawn move is an
     * "advance" when the pawn moves toward the opponent's side of
     * the board.
     *
     * @param move is a prospective move of "this" piece.
     * @param board is a 2-D IChessPiece Array representing the game
     * board.
     * @return is true if the move ends at a valid "castled" position.
     *****************************************************************/
    public boolean isAdvancing(Move move, IChessPiece[][] board) {
        if (move.fromColumn != move.toColumn)
            return false;
        if (moveCounter == 0) {
            if (this.player() == Player.BLACK) {
                if (move.toRow <= move.fromRow)
                    return false;
                if (move.toRow > move.fromRow + 2)
                    return false;
                if (move.toRow == move.fromRow + 2)
                    return ((board[move.toRow][move.toColumn] == null) && (board[move.toRow - 1][move.toColumn] == null));
                if (move.toRow == move.fromRow + 1)
                    if (board[move.toRow][move.toColumn] == null) {
                        return true;
                    }
            }
            if (this.player() == Player.WHITE) {
                if (move.toRow >= move.fromRow)
                    return false;
                if (move.toRow < move.fromRow - 2)
                    return false;
                if (move.toRow == move.fromRow - 2)
                    return ((board[move.toRow][move.toColumn] == null) && (board[move.toRow + 1][move.toColumn] == null));
                if (move.toRow == move.fromRow - 1)
                    if (board[move.toRow][move.toColumn] == null) {
                        return true;
                    }
            }
        } else {
            if (this.player() == Player.BLACK) {
                if (move.toRow <= move.fromRow)
                    return false;
                if (move.toRow > move.fromRow + 1)
                    return false;
                if (move.toRow == move.fromRow + 1)
                    return (board[move.toRow][move.toColumn] == null);
            }

            if (this.player() == Player.WHITE) {
                if (move.toRow >= move.fromRow)
                    return false;
                if (move.toRow < move.fromRow - 1)
                    return false;
                if (move.toRow == move.fromRow - 1)
                    return (board[move.toRow][move.toColumn] == null);
            }
        }
        return false;
    }


    /******************************************************************
     * This helper method checks if the prospective pawn move is a
     * "capture" when the pawn moves diagonally to destroy the
     * opponent's piece.
     *
     * @param move is a prospective move of "this" piece.
     * @param board is a 2-D IChessPiece Array representing the game
     * board.
     * @return is true if the move ends at a valid "castled" position.
     *****************************************************************/
    public boolean isCapturing(Move move, IChessPiece[][] board) {
        //if position is empty or has a friendly piece you are not capturing
        if (((board[move.toRow][move.toColumn] == null) ||
                (board[move.toRow][move.toColumn].player() == this.player())))
            return false;
        //black must move down the board
        if (this.player() == Player.BLACK) {
            //backwards, lateral, or more than one row are illegal
            if ((move.toRow <= move.fromRow) || (move.toRow > move.fromRow + 1))
                return false;
            if (move.toRow == move.fromRow + 1)
                //must capture diagonally (one forward and one across)
                if ((move.toColumn == move.fromColumn + 1) || (move.toColumn == move.fromColumn - 1))
                    return true;
        }
        //white must move up the board
        if (this.player() == Player.WHITE) {
            //backwards, lateral, or more than one row are illegal
            if ((move.toRow >= move.fromRow) || (move.toRow < move.fromRow - 1))
                return false;
            if (move.toRow == move.fromRow - 1)
                //must capture diagonally (one forward and one across)
                if ((move.toColumn == move.fromColumn + 1) || (move.toColumn == move.fromColumn - 1))
                    return true;
        }
        return false;
    }


    /******************************************************************
     * This helper method checks if the prospective pawn move is an
     * "en passant capture" when the pawn moves diagonally to destroy
     * the opponent's adjacent piece that just advanced two spaces.
     *
     * @param move is a prospective move of "this" piece.
     * @param board is a 2-D IChessPiece Array representing the game
     * board.
     * @return is true if the move ends at a valid "castled" position.
     *****************************************************************/
    public boolean isEnPassantCapturing(Move move, IChessPiece[][] board) {
        if (board[move.fromRow][move.fromColumn].player() == Player.WHITE) {
            if (move.fromRow != board.length - 5)
                return false;
            if (move.toRow != board.length - 6)
                return false;
            if (move.fromColumn - 1 != -1)
                if (board[move.fromRow][move.fromColumn - 1] != null) {
                    if (move.toColumn == move.fromColumn - 1) {
                        move.setEnPassantMove(true);
                        return true;
                    }
                }
            if (move.fromColumn + 1 != board.length)
                if (board[move.fromRow][move.fromColumn + 1] != null) {
                    if (move.toColumn == move.fromColumn + 1) {
                        move.setEnPassantMove(true);
                        return true;
                    }
                }
        }
        if (board[move.fromRow][move.fromColumn].player() == Player.BLACK) {
            if (move.fromRow != board.length - 4)
                return false;
            if (move.toRow != board.length - 3)
                return false;
            if (move.fromColumn - 1 != -1)
                if (board[move.fromRow][move.fromColumn - 1] != null) {
                    if (move.toColumn == move.fromColumn - 1) {
                        move.setEnPassantMove(true);
                        return true;
                    }
                }
            if (move.fromColumn + 1 != board.length)
                if (board[move.fromRow][move.fromColumn + 1] != null) {
                    if (move.toColumn == move.fromColumn + 1) {
                        move.setEnPassantMove(true);
                        return true;
                    }
                }
        }
        return false;
    }
}
