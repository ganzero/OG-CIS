package chess;

import javax.swing.*;
import java.util.ArrayList;

import static chess.Player.BLACK;
import static chess.Player.WHITE;

import static java.lang.Enum.valueOf;

public class ChessModel implements IChessModel, Cloneable {
    protected Player player;
    protected IChessPiece[][] board;
    protected boolean AIEnabled;
    ArrayList<Move> moveArrayList;
    ChessPiece promotedPiece;

    public ChessModel() {
        board = new IChessPiece[8][8];
        player = WHITE;

        board[7][0] = new Rook(WHITE);
        board[7][1] = new Knight(WHITE);
        board[7][2] = new Bishop(WHITE);
        board[7][3] = new Queen(WHITE);
        board[7][4] = new King(WHITE);
        board[7][5] = new Bishop(WHITE);
        board[7][6] = new Knight(WHITE);
        board[7][7] = new Rook(WHITE);

        for (int i = 0; i < board[0].length; i++) {
            board[board.length - 2][i] = new Pawn(WHITE);
        }

        board[0][0] = new Rook(BLACK);
        board[0][1] = new Knight(BLACK);
        board[0][2] = new Bishop(BLACK);
        board[0][3] = new Queen(BLACK);
        board[0][4] = new King(BLACK);
        board[0][5] = new Bishop(BLACK);
        board[0][6] = new Knight(BLACK);
        board[0][7] = new Rook(BLACK);

        for (int i = 0; i < board[0].length; i++) {
            board[1][i] = new Pawn(BLACK);
        }

        moveArrayList = new ArrayList<Move>();

        promotedPiece = null;
        AIEnabled = false;


    }

    /**
     * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
     * {@code [move.fromRow, move.fromColumn]}.
     *
     * @param move a {@link chess.Move} object describing the move to be made.
     * @return {@code true} if the proposed move is valid, {@code false} otherwise.
     */
    public boolean isValidMove(Move move) {
        return isValidMove(move, this.currentPlayer());
    }

    /**
     * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
     * {@code [move.fromRow, move.fromColumn]}.
     *
     * @param movingPlayer pass the player who is suppose to be moving a piece.
     * @param move a {@link chess.Move} object describing the move to be made.
     * @return {@code true} if the proposed move is valid, {@code false} otherwise.
     */
    public boolean isValidMove(Move move, Player movingPlayer) {
        boolean valid = false;
        Move lastMove;

        if ((board[move.fromRow][move.fromColumn] != null) &&
                (board[move.fromRow][move.fromColumn].isValidMove(move, board)) &&
                (board[move.fromRow][move.fromColumn].player() == movingPlayer)) {
            if (inCheck(movingPlayer)) {
                valid = isCheckBroken(move);
            } else if (isSelfCheck(move))
                valid = false;
            else valid = true;
            if (valid) {
                if ((board[move.fromRow][move.fromColumn] instanceof Pawn) &&
                        (((Pawn) board[move.fromRow][move.fromColumn]).isEnPassantCapturing(move, board))) {
                    //check last move was enemy pawn 2-space move
                    lastMove = moveArrayList.get(moveArrayList.size() - 1);
                    if ((Math.abs(lastMove.toRow - lastMove.fromRow) == 2) &&
                            (lastMove.toColumn == move.toColumn))
                        valid = true;
                    else valid = false;
                }
                if ((board[move.fromRow][move.fromColumn] instanceof King) &&
                        (((King) board[move.fromRow][move.fromColumn]).isCastling(move, board))) {
                    if (inCheck(board[move.fromRow][move.fromColumn].player()))
                        valid = false;
                    if (castlingThruCheck(move))
                        valid = false;
                }
            }
            return valid;
        }
        return valid;
    }

    /**
     * Report whether the current player p is in check.
     * @param  p {@link chess.Move} the Player being checked
     * @return {@code true} if the current player is in check, {@code false} otherwise.
     */
    public boolean inCheck(Player p) {
        int kingRow = -1;
        int kingCol = -1;
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c] instanceof King) &&
                        (board[r][c].player() == p)) {
                    kingRow = r;
                    kingCol = c;
                    //break out of nested loop search
                    r = board.length;
                    c = board[0].length;
                }
            }
        //is the other player putting you in check
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c].player() != p) &&
                        (board[r][c].isValidMove(new Move(r, c, kingRow, kingCol), board))) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Checks if the move made has taken the player out of check.
     *
     * @param move a move by a piece.
     * @return {@code true} if the player is no longer in check, {@code false} if player is still in check.
     */
    private boolean isCheckBroken(Move move) {
        boolean broken = false;
        this.move(move);
        if (!inCheck(player))
            broken = true;
        this.undo();
        return broken;
    }

    /**
     * Checks if the move made has put the current player into check.
     *
     * @param move a move by a piece.
     * @return {@code true} if the player has put themself into check, {@code false} otherwise.
     */
    private boolean isSelfCheck(Move move) {
        boolean selfCheck = false;
        this.move(move);
        if (inCheck(player))
            selfCheck = true;
        this.undo();
        player.next();
        return selfCheck;
    }

    /**
     * Checks if the castling move crossed through check.
     *
     * @param move a move by a piece.
     * @return {@code true} , {@code false} if player is still in check.
     */
    public boolean castlingThruCheck(Move move) {
        boolean thruCheck = false;
        int travelRow = move.fromRow;
        int travelCol = move.fromColumn;

        if (move.fromColumn > move.toColumn) {
            travelCol--;
            thruCheck = isThruCheck(thruCheck, travelRow, travelCol, board[move.fromRow][move.fromColumn]);

            //check 2 squares are clear of being checked for the queen's side castle (the final square is checked by isSelfCheck()
            if (!thruCheck) {
                travelCol--;
                thruCheck = isThruCheck(thruCheck, travelRow, travelCol, board[move.fromRow][move.fromColumn]);
            }
        }
        if (move.fromColumn < move.toColumn) {
            travelCol++;
            thruCheck = isThruCheck(thruCheck, travelRow, travelCol, board[move.fromRow][move.fromColumn]);
        }
        return thruCheck;
    }

    private boolean isThruCheck(boolean thruCheck, int travelRow, int travelCol, IChessPiece iChessPiece) {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c].player() != iChessPiece.player()) &&
                        (board[r][c].isValidMove(new Move(r, c, travelRow, travelCol), board)))
                    thruCheck = true;
            }
        return thruCheck;
    }

    /**
     * Moves the piece from location {@code [move.fromRow, move.fromColumn]} to location {@code [move.fromRow,
     * move.fromColumn]}.
     *
     * @param move a {@link chess.Move} object describing the move to be made.
     */
    public void move(Move move) {

        //if you are destroying a piece then send the move arraylist archive the reference to the piece being destroyed
        if (board[move.toRow][move.toColumn] != null) {
            moveArrayList.add(new Move(move.fromRow, move.fromColumn, move.toRow, move.toColumn));
            moveArrayList.get(moveArrayList.size() - 1).setDestroyedPiece(board[move.toRow][move.toColumn]);
        } else
            moveArrayList.add(move);

        //update move arraylist for destroyed enPassant pieces that are not at toRow toCol like other destroyed pieces
        if ((board[move.fromRow][move.fromColumn] instanceof Pawn) &&
                (((Pawn) board[move.fromRow][move.fromColumn]).isEnPassantCapturing(move, board))) {
            if (board[move.fromRow][move.fromColumn].player() == WHITE) {
                moveArrayList.get(moveArrayList.size() - 1).setDestroyedPiece(board[move.toRow + 1][move.toColumn]);
                board[move.toRow + 1][move.toColumn] = null;
            }
            if (board[move.fromRow][move.fromColumn].player() == BLACK) {
                moveArrayList.get(moveArrayList.size() - 1).setDestroyedPiece(board[move.toRow - 1][move.toColumn]);
                board[move.toRow - 1][move.toColumn] = null;
            }
        }

        //check if castlingMove before moving anything
        if (board[move.fromRow][move.fromColumn] instanceof King) {
            if (move.castlingMove) {
                castlingRookHelper(move);
            }
        }

        //move the piece
        board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;


        if (board[move.toRow][move.toColumn] instanceof Rook)
            ((Rook) board[move.toRow][move.toColumn]).moveCounter++;

        if (board[move.toRow][move.toColumn] instanceof King)
            ((King) board[move.toRow][move.toColumn]).moveCounter++;

        if (board[move.toRow][move.toColumn] instanceof Pawn) {
            ((Pawn) board[move.toRow][move.toColumn]).moveCounter++;

            //check for "queening"
            if ((board[move.toRow][move.toColumn].player() == BLACK) &&
                    (move.toRow == board.length - 1)) {
                board[move.toRow][move.toColumn] = new Queen(BLACK);
                moveArrayList.get(moveArrayList.size() - 1).queeningMove = true;
            }

            if ((board[move.toRow][move.toColumn].player() == WHITE) &&
                    (move.toRow == 0)) {
                board[move.toRow][move.toColumn] = new Queen(WHITE);
                moveArrayList.get(moveArrayList.size() - 1).queeningMove = true;
            }
        }

    }

    public void castlingRookHelper(Move kingMove) {
        int rookColStart = -1;
        int rookColEnd = -1;

        if (kingMove.toRow == 0) {
            if (kingMove.toColumn == board[0].length - 2) {
                rookColStart = board[0].length - 1;
                rookColEnd = board[0].length - 3;
            }
            if (kingMove.toColumn == board[0].length - 6) {
                rookColStart = 0;
                rookColEnd = board[0].length - 5;
            }
        }
        if (kingMove.toRow == 7) {
            if (kingMove.toColumn == board[0].length - 2) {
                rookColStart = board[0].length - 1;
                rookColEnd = board[0].length - 3;
            }
            if (kingMove.toColumn == board[0].length - 6) {
                rookColStart = 0;
                rookColEnd = board[0].length - 5;
            }
        }
        Move rookCastlingMove = new Move(kingMove.toRow, rookColStart, kingMove.toRow, rookColEnd);
        rookCastlingMove.setCastlingMove(true);
        move(rookCastlingMove);
    }

    public void undo() {
        try {
            Move lastMove = moveArrayList.get(moveArrayList.size() - 1);

            //undo a simple move
            if ((!lastMove.queeningMove) && (!lastMove.castlingMove) && (!lastMove.enPassantMove)) {
                mundaneUndo(lastMove);
            } else {
                if (lastMove.castlingMove) {
                    //requires 2 undo moves for castling; reverse rook
                    mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
                    //reverse King
                    mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
                } else if (lastMove.enPassantMove) {
                    if (board[lastMove.toRow][lastMove.toColumn].player() == WHITE) {
                        mundaneUndo(lastMove);
                        board[lastMove.toRow + 1][lastMove.toColumn] = board[lastMove.toRow][lastMove.toColumn];
                        board[lastMove.toRow][lastMove.toColumn] = null;
                    } else if (board[lastMove.toRow][lastMove.toColumn].player() == BLACK) {
                        mundaneUndo(lastMove);
                        board[lastMove.toRow - 1][lastMove.toColumn] = board[lastMove.toRow][lastMove.toColumn];
                        board[lastMove.toRow][lastMove.toColumn] = null;
                    }
                }
                //queening
                else {
                    mundaneUndo(lastMove);
                    Player queenedPlayer = board[lastMove.fromRow][lastMove.fromColumn].player();
                    board[lastMove.fromRow][lastMove.fromColumn] = new Pawn(queenedPlayer);

                }
            }
            //if we undo all of the moves we reset the player to black
            //the controller immediately passes the turn to white
        } catch (ArrayIndexOutOfBoundsException e) {
            player = BLACK;
        }
    }

    private void mundaneUndo(Move lastMove) {
        //reverse the last move
        move(new Move(lastMove.toRow, lastMove.toColumn, lastMove.fromRow, lastMove.fromColumn));

        //remove the fake reversal "move" from the archive
        moveArrayList.remove(moveArrayList.size() - 1);
        //remove the original move from the archive
        moveArrayList.remove(moveArrayList.size() - 1);

        //if you reverse the move then also decrement the moveCounter
        // (once for the reversed move and once for the reversal "move")
        if (board[lastMove.fromRow][lastMove.fromColumn] instanceof Pawn)
            ((Pawn) board[lastMove.fromRow][lastMove.fromColumn]).moveCounter -= 2;
        if (board[lastMove.fromRow][lastMove.fromColumn] instanceof Rook)
            ((Rook) board[lastMove.fromRow][lastMove.fromColumn]).moveCounter -= 2;
        if (board[lastMove.fromRow][lastMove.fromColumn] instanceof King)
            ((King) board[lastMove.fromRow][lastMove.fromColumn]).moveCounter -= 2;
        if (lastMove.destroyedPiece != null)
            board[lastMove.toRow][lastMove.toColumn] = lastMove.destroyedPiece;
        else board[lastMove.toRow][lastMove.toColumn] = null;
    }

    /**
     * Returns whether the game is complete.
     *
     * @return {@code true} if complete, {@code false} otherwise.
     */
    public boolean isComplete() {
        boolean complete = true;
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c].player() == player)) {
                    for (int row = 0; row < board.length; row++)
                        for (int col = 0; col < board[0].length; col++) {
                            if (this.isValidMove(new Move(r, c, row, col))) {
                                complete = false;
                                r = board.length;
                                c = board[0].length;
                                row = board.length;
                                col = board[0].length;
                            }
                        }
                }
            }
        return complete;
    }

    /**
     * Return the current player.
     *
     * @return the current player
     */
    public Player currentPlayer() {
        return player;
    }

    /**
     * Return the number of rows on the board.
     *
     * @return an int of how many rows there are.
     */
    public int numRows() {
        return board.length;
    }

    /**
     * Return the number of columns on the board.
     *
     * @return an int of how many columns there are.
     */
    public int numColumns() {
        return board[0].length;
    }

    /**
     * Return the piece at a certain row and column.
     *
     * @param row the row that wants to be searched
     * @param column the column that wants to be searched
     * @return the piece at row, column.
     */
    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    /**
     * Sets the player to the next player.
     */
    public void setNextPlayer() {
        player = player.next();
    }

    /**
     * Place a piece at a certain spot on the board
     *
     * @param row a certain row the piece is going to be placed at.
     * @param column a certain column the piece is going to be placed at.
     * @param piece the specific piece the user wants to place.
     */
    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }


    public void AI() {
        Move AIMove = null;
        if (inCheck(BLACK)) {
            for (int fr = 0; fr < board.length; fr++)
                for (int fc = 0; fc < board[0].length; fc++) {
                    if ((board[fr][fc] != null) &&
                            (board[fr][fc].player() == BLACK))
                        for (int tr = 0; tr < board.length; tr++)
                            for (int tc = 0; tc < board[0].length; tc++) {
                                Move testedmove = new Move(fr, fc, tr, tc);
                                if (isValidMove(testedmove, BLACK)) {
                                    AIMove = testedmove;
                                    fr = board.length;
                                    fc = board[0].length;
                                    tr = board.length;
                                    tc = board[0].length;
                                }
                            }
                }

        }
        //attempt to check
        if (AIMove == null) {
            for (int fr = 0; fr < board.length; fr++)
                for (int fc = 0; fc < board[0].length; fc++)
                    if (board[fr][fc] != null) {
                        if (board[fr][fc].player()==BLACK)
                            for (int tr = 0; tr < board.length; tr++)
                                for (int tc = 0; tc < board[0].length; tc++) {
                                    Move testedmove = new Move(fr, fc, tr, tc);
                                    if (isValidMove(testedmove, BLACK)) {
                                        move(testedmove);
                                        if (inCheck(WHITE)) {
                                            AIMove = testedmove;
                                            fr = board.length;
                                            fc = board[0].length;
                                            tr = board.length;
                                            tc = board[0].length;
                                        }
                                        undo();
                                    }
                                }
                    }
        }
        if (AIMove == null) {
            //Find Black piece, find White piece, can black take White? Take it
            for (int fr = 0; fr < board.length; fr++)
                for (int fc = 0; fc < board[0].length; fc++)
                    if (board[fr][fc] != null) {
                        if (board[fr][fc].player() == BLACK) {
                            for (int tr = 0; tr < board.length; tr++)
                                for (int tc = 0; tc < board[0].length; tc++)
                                    if ((board[tr][tc] != null) &&
                                            (board[tr][tc].player().equals(WHITE))) {
                                        Move testedMove = new Move(fr, fc, tr, tc);
                                        if (isValidMove(testedMove)) {
                                            AIMove = testedMove;
                                            fr = board.length;
                                            fc = board[0].length;
                                            tr = board.length;
                                            tc = board[0].length;
                                        }
                                    }
                        }
                    }
        }
        if (AIMove == null) {
            //move pawns
            int pawnCol = (int) (Math.random() * 8);
            if (board[1][pawnCol] != null) {
                Move testedMove = new Move(1, pawnCol, 3, pawnCol);
                if (board[1][pawnCol].isValidMove(testedMove, board))
                    AIMove = testedMove;
            }
        }

        if (AIMove == null) {
            boolean valid = false;

            while (!valid) {
                int fr = (int) (Math.random() * 8);
                int fc = (int) (Math.random() * 8);
                if ((board[fr][fc]) != null && (board[fr][fc].player() == BLACK)) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            Move testedMove = new Move(fr, fc, r, c);
                            if (isValidMove(testedMove)) {
                                AIMove = testedMove;
                                valid = true;
                            }
                        }
                    }
                }
            }
        }
        move(AIMove);
        setNextPlayer();
    }
}