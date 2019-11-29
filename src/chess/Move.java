package chess;

public class Move {
    protected int fromRow, fromColumn, toRow, toColumn;
    protected IChessPiece destroyedPiece;
    protected boolean castlingMove;
    protected boolean queeningMove;
    protected boolean enPassantMove;

    public Move() {
    }

    public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
        this.destroyedPiece = null;
        this.castlingMove = false;
        this.queeningMove = false;
        this.enPassantMove = false;
    }

    public Move(int fromRow, int fromColumn, int toRow, int toColumn, IChessPiece destroyedPiece, boolean castlingMove, boolean queeningMove, boolean enPassantMove, boolean isCompleteMove) {
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
        this.destroyedPiece = destroyedPiece;
        this.castlingMove = castlingMove;
        this.queeningMove = queeningMove;
        this.enPassantMove = enPassantMove;
    }

    public void setDestroyedPiece(IChessPiece destroyedPiece) {
        this.destroyedPiece = destroyedPiece;
    }

    public void setCastlingMove(boolean castlingMove) {
        this.castlingMove = castlingMove;
    }

    public void setQueeningMove(boolean queeningMove) {
        this.queeningMove = queeningMove;
    }

    public void setEnPassantMove(boolean isEnPassantMove) {
        this.enPassantMove = isEnPassantMove;
    }

    @Override
    public String toString() {
        String s = "Move [fromRow=" + fromRow + ", fromColumn=" + fromColumn + ", toRow=" + toRow + ", toColumn=" + toColumn
                + "]";
        if (destroyedPiece != null)
            s += " Destroyed Piece = " + destroyedPiece.player() + " " + destroyedPiece.type();

        if (castlingMove)
            s += " castlingMove " + castlingMove;

        if (queeningMove)
            s += " queeningMove " + queeningMove;

        if (enPassantMove)
            s += " enPassantMove " + enPassantMove;
        return s;
    }
}