package chess;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static chess.Player.BLACK;
import static chess.Player.WHITE;

public class ChessPanel extends JPanel {

    JPanel promotionPanel;
    JRadioButton queenRadBtn, rookRadBtn, knightRadBtn, bishopRadBtn;
    ButtonGroup promotionGroup;
    private JButton[][] board;
    private ChessModel model;
    private ImageIcon wRook;
    private ImageIcon wBishop;
    private ImageIcon wQueen;
    private ImageIcon wKing;
    private ImageIcon wPawn;
    private ImageIcon wKnight;
    private ImageIcon bRook;
    private ImageIcon bBishop;
    private ImageIcon bQueen;
    private ImageIcon bKing;
    private ImageIcon bPawn;
    private ImageIcon bKnight;
    private boolean firstTurnFlag;
    private int fromRow;
    private int toRow;
    private int fromCol;
    private int toCol;
    private JButton undoButton;

    // declare other instance variables as needed
    private listener listener;

    public ChessPanel() {
        model = new ChessModel();
        board = new JButton[model.numRows()][model.numColumns()];
        listener = new listener();
        createIcons();

        JPanel boardpanel = new JPanel();
        JPanel buttonpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(model.numRows() + 1, model.numColumns(), 1, 1));

        undoButton = new JButton("Undo");
        undoButton.addActionListener(listener);


        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++) {

                if (model.pieceAt(r, c) == null) {
                    board[r][c] = new JButton("", null);
                    board[r][c].addActionListener(listener);
                } else if (model.pieceAt(r, c).player() == Player.BLACK) {
                    placeBlackPieces(r, c);
                } else if (model.pieceAt(r, c).player() == Player.WHITE) {
                    placeWhitePieces(r, c);
                }

                setBackGroundColor(r, c);
                buttonpanel.add(board[r][c]);
                boardpanel.add(board[r][c]);
            }
        }

        add(undoButton, BorderLayout.NORTH);
        add(boardpanel, BorderLayout.WEST);
        boardpanel.setPreferredSize(new Dimension(600, 600));
        add(buttonpanel, BorderLayout.SOUTH);
        firstTurnFlag = true;


        promotionPanel = new JPanel();

        queenRadBtn = new JRadioButton("Queen");
        queenRadBtn.setSelected(true);
        rookRadBtn = new JRadioButton("Rook");
        knightRadBtn = new JRadioButton("Knight");
        bishopRadBtn = new JRadioButton("Bishop");

        promotionGroup = new ButtonGroup();
        promotionGroup.add(queenRadBtn);
        promotionGroup.add(rookRadBtn);
        promotionGroup.add(knightRadBtn);
        promotionGroup.add(bishopRadBtn);


        promotionPanel.add(queenRadBtn);
        promotionPanel.add(rookRadBtn);
        promotionPanel.add(knightRadBtn);
        promotionPanel.add(bishopRadBtn);

        queenRadBtn.addActionListener(listener);
        rookRadBtn.addActionListener(listener);
        knightRadBtn.addActionListener(listener);
        bishopRadBtn.addActionListener(listener);
    }

    private void setBackGroundColor(int r, int c) {
        if ((c % 2 == 1 && r % 2 == 0) || (c % 2 == 0 && r % 2 == 1)) {
            board[r][c].setBackground(Color.LIGHT_GRAY);
        } else if ((c % 2 == 0 && r % 2 == 0) || (c % 2 == 1 && r % 2 == 1)) {
            board[r][c].setBackground(Color.WHITE);
        }
    }

    private void placeWhitePieces(int r, int c) {
        if (model.pieceAt(r, c).type().equals("Pawn")) {
            board[r][c] = new JButton(null, wPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")) {
            board[r][c] = new JButton(null, wRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")) {
            board[r][c] = new JButton(null, wKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")) {
            board[r][c] = new JButton(null, wBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")) {
            board[r][c] = new JButton(null, wQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")) {
            board[r][c] = new JButton(null, wKing);
            board[r][c].addActionListener(listener);
        }
    }

    private void placeBlackPieces(int r, int c) {
        if (model.pieceAt(r, c).type().equals("Pawn")) {
            board[r][c] = new JButton(null, bPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")) {
            board[r][c] = new JButton(null, bRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")) {
            board[r][c] = new JButton(null, bKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")) {
            board[r][c] = new JButton(null, bBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")) {
            board[r][c] = new JButton(null, bQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")) {
            board[r][c] = new JButton(null, bKing);
            board[r][c].addActionListener(listener);
        }
    }

    private void createIcons() {
        // Sets the Image for white player pieces
        wRook = new ImageIcon("src/chess/wRook.png");
        wBishop = new ImageIcon("src/chess/wBishop.png");
        wQueen = new ImageIcon("src/chess/wQueen.png");
        wKing = new ImageIcon("src/chess/wKing.png");
        wPawn = new ImageIcon("src/chess/wPawn.png");
        wKnight = new ImageIcon("src/chess/wKnight.png");

        // Sets the image for the black player pieces
        bRook = new ImageIcon("src/chess/bRook.png");
        bBishop = new ImageIcon("src/chess/bBishop.png");
        bQueen = new ImageIcon("src/chess/bQueen.png");
        bKing = new ImageIcon("src/chess/bKing.png");
        bPawn = new ImageIcon("src/chess/bPawn.png");
        bKnight = new ImageIcon("src/chess/bKnight.png");
    }

    // method that updates the board
    private void displayBoard() {

        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++)
                if (model.pieceAt(r, c) == null)
                    board[r][c].setIcon(null);
                else if (model.pieceAt(r, c).player() == Player.WHITE) {
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(wPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(wRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(wKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(wBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(wQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(wKing);
                } else {
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(bPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(bRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(bKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(bBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(bQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(bKing);
                }
        }
        repaint();
    }

    // inner class that represents action listener for buttons
    private class listener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            for (int r = 0; r < model.numRows(); r++)
                for (int c = 0; c < model.numColumns(); c++) {
                    if (board[r][c] == event.getSource()) {
                        if ((firstTurnFlag == true) && (model.pieceAt(r, c) != null)) {
                            fromRow = r;
                            fromCol = c;
                            firstTurnFlag = false;
                        } else {
                            toRow = r;
                            toCol = c;
                            firstTurnFlag = true;
                            Move m = new Move(fromRow, fromCol, toRow, toCol);
                            if ((model.isValidMove(m))) {
                                model.move(m);
                                displayBoard();
                                model.setNextPlayer();
                                checkGameStatus();
                                enableAI();
                            }
                        }
                    }
                }
            if (event.getSource() == undoButton) {
                model.undo();
                for (Move movePrinter : model.moveArrayList)
                    System.out.println(movePrinter);
                displayBoard();
                model.setNextPlayer();
                System.out.println(model.currentPlayer());
            }
        }

        private void enableAI() {
            model.AI();
            displayBoard();
            checkGameStatus();
        }

        private void checkGameStatus() {
            if (model.isComplete())
                JOptionPane.showMessageDialog(null, "Checkmate!");
            else if (model.inCheck(model.currentPlayer())) {
                if (model.currentPlayer() == WHITE)
                    JOptionPane.showMessageDialog(null, "White is in Check");
                else
                    JOptionPane.showMessageDialog(null, "Black is in Check");
            }
        }

//        private void promotionHelper(int row, int col) {
//            int selection = JOptionPane.showOptionDialog(null, promotionPanel,
//                    "Choose Your Promotion", JOptionPane.OK_OPTION, 0, null);
//            System.out.println("Selection is " + selection);
//           // model.setPromotionPiece(new Rook(model.pieceAt(row, col).player()));
//        }

//        private void promotionHelper(int row, int col) {
//            if (((row == 0) || (row == model.board.length - 1)) && (model.pieceAt(row, col) instanceof Pawn)) {
//                Object[] possibilities = {"Queen", "Rook", "Bishop", "Knight"};
//                String s = (String) JOptionPane.showInputDialog(
//                        null,
//                        "Choose promotion",
//                        "Promotion Dialog",
//                        JOptionPane.PLAIN_MESSAGE,
//                        null,
//                        possibilities,
//                        "Queen");
//
//                model.setPromotionPiece(s, row, col);
//            }

    }
}

