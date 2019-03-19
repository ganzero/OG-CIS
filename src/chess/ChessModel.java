package chess;

import java.util.ArrayList;

import static Project3.Player.BLACK;
import static Project3.Player.WHITE;

public class ChessModel implements IChessModel, Cloneable {
    protected Player player;
    ArrayList<Move> moveArrayList;
    private IChessPiece[][] board;

    // declare other instance variables as needed

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
    }


    public boolean isValidMove(Move move) {
        boolean valid = false;

        if ((board[move.fromRow][move.fromColumn] != null) &&
                (board[move.fromRow][move.fromColumn].isValidMove(move, board)) &&
                (board[move.fromRow][move.fromColumn].player() == currentPlayer())) {
            // System.out.println("Got here");
//                if(inCheck(player))
//                    return true;

            if (inCheck(player)) {
                return isCheckBroken(move);
            }
            if (isSelfCheck(move))
                return false;
            else valid = true;
            valid = true;
        }

        return valid;
    }

    private boolean isCheckBroken(Move move) {                                  //Fixme
        boolean broken = false;
        this.move(move);
        if (!inCheck(player))
            broken = true;
        //System.out.println( ((King) board[move.fromRow][move.fromColumn]).moveCounter);

        this.undo();
        return broken;
    }

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
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c].player() != p) &&
                        (board[r][c].isValidMove(new Move(r, c, kingRow, kingCol), board)))
                    return true;
            }
        return false;
    }


    private boolean isSelfCheck(Move move) {                                  //Fixme
        boolean selfCheck = false;
        this.move(move);
        if (inCheck(player))
            selfCheck = true;
        this.undo();
        return selfCheck;
    }

//    public boolean inCheck(Player p) {
//        for (int r = 0; r < 8; r++) {
//            for (int c = 0; c < 8; c++) {
//                // if location contains enemy piece
//                if (this.containsPiece(r, c) && board[r][c].player() != p) {
//                    for (int z = 0; z < this.possibleMoves(r, c).size(); z++) {
//                        int tRow = this.possibleMoves(r, c).get(z).toRow;//capture a piece in row
//                        int tCol = this.possibleMoves(r, c).get(z).toColumn;//capture a piece in col
//                        // if target can be king, in check
//                        if (this.containsPiece(tRow, tCol)) {
//                            if (board[tRow][tCol].type() == "King" && board[tRow][tCol].player() == p)
//                                return true;
//                        }
//                    }
//                }
//            }
//        }
//
//        return false;
//    }



//    public boolean containsPiece(int row, int column) {
//        if (board[row][column].type() == null)
//            return false; //No Piece
//        else
//            return true; //Piece in row and col
//    }



    public void move(Move move) {

        //if you are destroying a piece then send the move arraylist archive the reference to the piece being destroyed
        if (board[move.toRow][move.toColumn] != null) {
            moveArrayList.add(new Move(move.fromRow, move.fromColumn, move.toRow, move.toColumn));
            moveArrayList.get(moveArrayList.size() - 1).setDestroyedPiece(board[move.toRow][move.toColumn]);
        } else
            moveArrayList.add(move);


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
                move.queeningMove = true;
            }

            if ((board[move.toRow][move.toColumn].player() == WHITE) &&
                    (move.toRow == 0)) {
                board[move.toRow][move.toColumn] = new Queen(WHITE);
                move.queeningMove = true;
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
        boolean complexUndo = false;
        try {
            //check if undoing castling
//            if (moveArrayList.size() >= 2) {
//                Move secondToLastMove = moveArrayList.get(moveArrayList.size() - 2);
//                if (secondToLastMove.castlingMove)
//                    complexUndo = true;
//            }
//            //check if undoing queening
            Move lastMove = moveArrayList.get(moveArrayList.size() - 1);
            if ((lastMove.queeningMove) || (lastMove.castlingMove))
                complexUndo = true;

            //undo a simple move
            if (!complexUndo) {
                mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
            } else {
                if (lastMove.castlingMove) {
                    //requires 2 undo moves for castling
                    // reverse Rook move during castling
                    mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
                    //reverse King move during castling
                    mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
                }
                //queening
                else {
                    Move queeningMove = moveArrayList.get(moveArrayList.size() - 1);
                    mundaneUndo(moveArrayList.get(moveArrayList.size() - 1));
                    Player queenedPlayer = board[queeningMove.fromRow][queeningMove.fromColumn].player();
                    board[queeningMove.fromRow][queeningMove.fromColumn] = new Pawn(queenedPlayer);
                }
            }
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

        //if you reverse the move then also decrement the moveCounter (once for the reversed move and once for the reversal "move")
        if (board[lastMove.fromRow][lastMove.toColumn] instanceof Pawn) {
            ((Pawn) board[lastMove.fromRow][lastMove.toColumn]).moveCounter -= 2;
        }

        if (board[lastMove.fromRow][lastMove.fromColumn] instanceof Rook)
            ((Rook) board[lastMove.fromRow][lastMove.fromColumn]).moveCounter -= 2;

        if (board[lastMove.fromRow][lastMove.fromColumn] instanceof King)
            ((King) board[lastMove.fromRow][lastMove.fromColumn]).moveCounter -= 2;

        if (lastMove.destroyedPiece != null)
            board[lastMove.toRow][lastMove.toColumn] = lastMove.destroyedPiece;
        else board[lastMove.toRow][lastMove.toColumn] = null;
    }


    public boolean isComplete() {
        boolean complete = true;
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if ((board[r][c] != null) &&
                        (board[r][c].player() == player)) {
                    for (int row = 0; row < board.length; row++)
                        for (int col = 0; col < board[0].length; col++)
                            if (board[r][c].isValidMove(new Move(r, c, row, col), board)) {
                                complete = false;
                                r = board.length;
                                c = board[0].length;
                                row = board.length;
                                col = board[0].length;
                            }
                }
            }
        return complete;
    }


    public Player currentPlayer() {
        return player;
    }

    public int numRows() {
        return board.length;
    }

    public int numColumns() {
        return board[0].length;
    }

    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    public void setNextPlayer() {
        player = player.next();
    }

    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    public void AI() {

        /*
         * Write a simple AI set of rules in the following order.
         * a. Check to see if you are in check.
         * 		i. If so, get out of check by moving the king or placing a piece to block the check
         *
         * b. Attempt to put opponent into check (or checkmate).
         * 		i. Attempt to put opponent into check without losing your piece
         *		ii. Perhaps you have won the game.
         *
         *c. Determine if any of your pieces are in danger,
         *		i. Move them if you can.
         *		ii. Attempt to protect that piece.
         *
         *d. Move a piece (pawns first) forward toward opponent king
         *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
         */

    }

    public ArrayList<Move> generateMoves() {
        ArrayList<Move> possMoves = new ArrayList<>();
        Move test;

        // creates a Array for a passed pieces possible moves
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                for (int tr = 0; tr < 8; tr++) {
                    for (int tc = 0; tc < 8; tc++) {
                        test = new Move(r, c, tr, tc);
                        if (this.isValidMove(test))
                            possMoves.add(test);
                    }
                }
            }
        }
        return possMoves;
    }

    public Move getMove() {
        return getBestMove();
    }

    private Move getBestMove() {
        ArrayList<Move> validMoves = generateMoves();
        int bestResult = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : validMoves) {
            move(move);
            int evaluationResult = this.evaluateState();
            undo();
            if (evaluationResult == bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluateState() {
        int scoreWhite = 0;
        int scoreBlack = 0;
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c].player() == BLACK) {
                    if (board[r][c].type() == "Pawn")
                        scoreBlack += 10;
                    if (board[r][c].type() == "Knight")
                        scoreBlack += 30;
                    if (board[r][c].type() == "Bishop")
                        scoreBlack += 30;
                    if (board[r][c].type() == "Rook")
                        scoreBlack += 50;
                    if(board[r][c].type() == "Queen")
                        scoreBlack += 90;
                    if(board[r][c].type() == "King")
                        scoreBlack += 999;
                }
                if (board[r][c].player() == WHITE) {
                    if (board[r][c].type() == "Pawn")
                        scoreWhite += 10;
                    if (board[r][c].type() == "Knight")
                        scoreWhite += 30;
                    if (board[r][c].type() == "Bishop")
                        scoreWhite += 30;
                    if (board[r][c].type() == "Rook")
                        scoreWhite += 50;
                    if (board[r][c].type() == "Queen")
                        scoreWhite += 90;
                    if (board[r][c].type() == "King")
                        scoreWhite += 999;
                }
            }
        if (BLACK == currentPlayer())
            return scoreBlack - scoreWhite;
        else
            return scoreWhite - scoreBlack;
    }



//    @Override
//    public Object clone() {
//        ChessModel clone = new ChessModel();
//        try {
//            clone = (ChessModel) super.clone();
//            clone.board = this.board.clone();
//            // clone.player = this.player;    //probably ok but could be an issue
//        } catch (CloneNotSupportedException e) {
//            System.out.println("Error  ");    //FIXME
//        }
//        return clone;
//    }


    //use the king's ending position to reverse-engineer the rook's position
//        int kingCastledPositionRow = rookCastlingMove.toRow;
//        int kingCastlePositionCol = rookCastlingMove.toColumn;

//        int rookColStart = -1;
//        int rookColEnd = -1;
//
//        if (kingCastledPositionRow == 0) {
//            if (kingCastlePositionCol == board[0].length - 2) {
//                rookColStart = board[0].length - 3;
//                rookColEnd = board[0].length - 1;
//            }
//            if (kingCastlePositionCol == board[0].length - 6) {
//                rookColStart = board[0].length - 5;
//                rookColEnd = 0;
//            }
//        }
//        if (kingCastledPositionRow == 7) {
//            if (kingCastlePositionCol == board[0].length - 2) {
//                rookColStart = board[0].length - 3;
//                rookColEnd = board[0].length - 1;
//            }
//            if (kingCastlePositionCol == board[0].length - 6) {
//                rookColStart = board[0].length - 5;
//                rookColEnd = 0;
//            }
//        }

}