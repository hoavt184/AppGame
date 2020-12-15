/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Vu Tien Hoa
 */
public class ChessGameDemo extends JFrame implements MouseListener, MouseMotionListener, ActionListener {

    JLayeredPane layeredPane;
    JButton btnSur = new JButton("Surrender");
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
    String nameAction;

    public void addListenBtnSur(ActionListener l) {
        btnSur.addActionListener(this);
    }

    public ChessGameDemo(String nameAction) {
        this.nameAction = nameAction;

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(800, 600));
        getContentPane().add(mainPanel);
        Dimension boardSize = new Dimension(600, 600);
        layeredPane = new JLayeredPane();
        mainPanel.add(layeredPane);
        mainPanel.add(btnSur);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);

            int row = (i / 8) % 2;
            if (row == 0) {
                square.setBackground(i % 2 == 0 ? Color.gray : Color.white);
            } else {
                square.setBackground(i % 2 == 0 ? Color.white : Color.gray);
            }
        }
        createboard();
    }

    public void createboard() {
        JLabel piece;
        JPanel panel;
        if (nameAction.equals("bi thach dau")) {
            for (int i = 8; i < 16; i++) {
                piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Pawn-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
                panel = (JPanel) chessBoard.getComponent(i);
                panel.add(piece);
            }

            //xe
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(0);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(7);
            panel.add(piece);

            //ma
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(1);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(6);
            panel.add(piece);

            //tuong
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(2);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(5);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Queen-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(3);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/King-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(4);
            panel.add(piece);

//        white
            //tot white
            for (int i = 48; i < 56; i++) {
                piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Pawn-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
                panel = (JPanel) chessBoard.getComponent(i);
                panel.add(piece);
            }
            //xe white
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(56);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(63);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(57);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(62);
            panel.add(piece);

            //tuong
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(58);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(61);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Queen-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(59);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/King-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(60);
            panel.add(piece);
        } else {
            for (int i = 8; i < 16; i++) {
                piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Pawn-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
                panel = (JPanel) chessBoard.getComponent(i);
                panel.add(piece);
            }

            //xe
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(0);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(7);
            panel.add(piece);

            //ma
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(1);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(6);
            panel.add(piece);

            //tuong
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(2);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(5);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Queen-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(3);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/King-white.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(4);
            panel.add(piece);

//        white
            //tot white
            for (int i = 48; i < 56; i++) {
                piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Pawn-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
                panel = (JPanel) chessBoard.getComponent(i);
                panel.add(piece);
            }
            //xe white
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(56);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Rook-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(63);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(57);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Knight-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(62);
            panel.add(piece);

            //tuong
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(58);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Bishop-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(61);
            panel.add(piece);

            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/Queen-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(59);
            panel.add(piece);
            piece = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Res/King-black.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
            panel = (JPanel) chessBoard.getComponent(60);
            panel.add(piece);
        }

    }

    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) {
            return;
        }

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel) c;
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) {
            return;
        }
        chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
    }

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) {
            return;
        }

        chessPiece.setVisible(false);
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());

        if (c instanceof JLabel) {
            Container parent = c.getParent();
            parent.remove(0);
            parent.add(chessPiece);
        } else {
            Container parent = (Container) c;
            parent.add(chessPiece);
        }
        chessPiece.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) {
        JFrame frame = new ChessGameDemo("thach dau");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
