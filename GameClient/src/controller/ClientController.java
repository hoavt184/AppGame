/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GameModels.Request;
import GameModels.User;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import view.ChessBoard;
import view.ChessGameDemo;
import view.Home;
import view.Login;
import view.Rank;
import view.Register;

/**
 *
 * @author Vu Tien Hoa
 */
public class ClientController {
    private Socket clientSocet;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String serverName = "localhost";
    private int port = 8889;
    private Login login;
    private Register register;
    private User myUser;
    private Home home;
    private Rank rank;
    private ArrayList<User> listRank;
    private ChessBoard chess;
    private ChessGameDemo test;
    private int bx,by, ax,ay;
    private String userNameCompititor;
    public ClientController(){
       
        open();
        login =new Login();
        login.setVisible(true);
        login.addListenBtnLogin( new ListenBtnLogin());
        login.addListenBtnRegister(new ListenBtnRegister());
        
        new Listening().start();
        new updateOnline().start();

    }
    
    class ListenTableMain implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable table = home.getTable();
            int row = table.rowAtPoint(e.getPoint());
            String userName = (String) table.getValueAt(row, 0);
            if(home.showConfirmYesNo("Bạn có muốn thách đấu với "+userName+" không?", "Mời thách đấu")==0){
                Request req = new Request("challenge",(Object)userName);
                send(req);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }
        
    }
    class updateOnline extends Thread{
        public void run(){
            while(true){
                try {
                    this.sleep(5000);
                    getListPlayer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public void getListPlayer() {
        Request req = new Request("getListPlayer");
        send(req);
    }
    class Listening extends Thread{
        
        @Override
        public void run() {
            while (true) {                
                try {
                    Request response = (Request) ois.readObject();
                    switch(response.getAction()){
                        case "login":
                            handleLogin(response);
                            break;
                        case "register":
                            handleRegister(response);
                            break;
                        case "sendListPlayer":
                            handleListPlayer(response);
                            break;
                        case "sendRank":
                            handleRank(response);
                            break;
                        case "sendInvite":
                            handleInvite(response);
                            break;
                        case "play":
                            handlePlay(response);
                            break;
                        case "youWin":
                            handleWin(response);
                            break;
                        case "move":
                            handleMove(response);
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public void handleMove(Request res){
            String user = (String) res.getData2();
            int[] moves = (int[]) res.getData();
            
            test.movePiece(moves[0],moves[1],moves[2], moves[3]);
        }
        public void handleWin(Request res){
            System.out.println("ok");
            String user = (String)res.getData();
            home.showM("Đối thủ đã đầu hàng!\nBạn đã thắng "+user);
            test.dispose();
        }
        public void handlePlay(Request res){
            String dataResponse = (String) res.getData();
            String[] tmp = dataResponse.split(",");
            String user = tmp[1];
            userNameCompititor = tmp[1];
            String nameAction = tmp[0];
            test = new ChessGameDemo(nameAction);
            test.addListenBtnSur(new ListenBtnSur((user)));
            test.addListenChess(new ListenChessBoard(), new ListenMouseMotion());
            test.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            test.pack();
            test.setResizable(true);
            test.setLocationRelativeTo(null);
            test.setVisible(true);
        }
        class ListenMouseMotion implements MouseMotionListener{

            @Override
            public void mouseDragged(MouseEvent me) {
                if (test.chessPiece == null) {
                    return;
                }
                test.chessPiece.setLocation(me.getX() + test.xAdjustment, me.getY() + test.yAdjustment);
                
            }

            
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        }
        
        class ListenChessBoard implements MouseListener{

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                test.chessPiece = null;
                Component c = test.chessBoard.findComponentAt(e.getX(), e.getY());

                if (c instanceof JPanel) {
                    return;
                }

                Point parentLocation = c.getParent().getLocation();

                // parentLocation.x,y la toa do vi tri hien tamousei;
                test.xAdjustment = parentLocation.x - e.getX();
                test.yAdjustment = parentLocation.y - e.getY();
//                System.out.println("From ");
//                System.out.println(parentLocation.x + "+" + parentLocation.y);
                bx = parentLocation.x;
                by = parentLocation.y;
                test.chessPiece = (JLabel) c;
                test.chessPiece.setLocation(e.getX() + test.xAdjustment, e.getY() + test.yAdjustment);
                
                System.out.println((e.getX()+test.xAdjustment)+"-"+(e.getY()+test.yAdjustment));
//                bx = (e.getX()+test.xAdjustment);
//                by = (e.getY()+test.yAdjustment);
//                System.out.println();
                test.chessPiece.setSize(test.chessPiece.getWidth(), test.chessPiece.getHeight());
                test.layeredPane.add(test.chessPiece, JLayeredPane.DRAG_LAYER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (test.chessPiece == null) {
                    return;
                }
                 
                test.chessPiece.setVisible(false);

                Component c = test.chessBoard.findComponentAt(e.getX(), e.getY());
//                System.out.println(" to ");
//                System.out.println(c.getX() + "+" + c.getY());
                ax = c.getX();
                ay = c.getY();
                if (c instanceof JLabel) {
//                    System.out.println("if");
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(test.chessPiece);
                } else {
//                    System.out.println("else
                    Container parent = (Container) c;
                    parent.add(test.chessPiece);
                }
                
                test.sum++;
                System.out.println(test.sum);
                int[] data ={bx,by,ax,ay,test.sum};
                Request req = new Request("move",(Object)data,(Object)(userNameCompititor));
                send(req);
                test.chessPiece.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        }
        class ListenBtnSur implements ActionListener{
            private String user ;
//            private ChessBoard chess;
            public ListenBtnSur(String user){
                this.user = user;
//                this.chess = chess;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                if(test.showConfirmYesNo("Bạn có chắc chắn muốn đầu hàng không?", "Đầu hàng")==0){
                    test.dispose();
                    Request req = new Request("Sur",(Object)user);
                    send(req);
                }
            }            
        }
        public void handleInvite(Request res){
            String user = (String) res.getData();
            if(home.showConfirmYesNo(user +" muốn thách đấu với bạn", "Lời mời thách đấu")==0){
                Request req = new Request("acceptInvite",(Object)user);
                send(req);
            }
        }
        class byScore implements Comparator<User>{
            @Override
            public int compare(User o1, User o2) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(o1.getScore()<o2.getScore()) return 1;
                if(o1.getScore()>o2.getScore()) return -1;
                return 0;
            }
        }
        class byWin implements Comparator<User>{

            @Override
            public int compare(User o1, User o2) {
                if(o1.getAverageMoveWinMatch()<o2.getAverageMoveWinMatch()) return -1;
                if(o1.getAverageMoveWinMatch()>o2.getAverageMoveWinMatch()) return 1;
                return 0;
            }
            
        }
        class byLost implements Comparator<User>{

            @Override
            public int compare(User o1, User o2) {
                if(o1.getAverageMoveLostMatch()<o2.getAverageMoveLostMatch()) return 1;
                if(o1.getAverageMoveLostMatch()>o2.getAverageMoveLostMatch()) return -1;
                return 0;
            }
            
        }
        public void showRankScore( Rank rank){
            Collections.sort(listRank,new byScore());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :listRank){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getScore())));
            }
            rank.showRank(ranks);
            
        }
        public void showRankWin( Rank rank){
            Collections.sort(listRank,new byWin());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :listRank){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getAverageMoveWinMatch())));
            }
            rank.showRank(ranks);
            
        }
        public void showRankLost(Rank rank){
            Collections.sort(listRank,new byLost());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :listRank){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getAverageMoveLostMatch())));
            }
            rank.showRank(ranks);
            
        }
        public void handleRank(Request res){
            listRank=(ArrayList<User>) res.getData();
            rank = new Rank();
            rank.setVisible(true);
            rank.addListenBtn(new ListenBtnRankBy());
            showRankScore(rank);
        }
        class ListenBtnRankBy implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                switch(btn.getName()){
                    case "score":
                        showRankScore(rank);
                        break;
                    case "lost":
                        showRankLost(rank);
                        break;
                    case "win":
                        showRankWin(rank);
                        break;
                    case "close":
                        rank.dispose();
                        break;
                }
            }
            
        }
        public void handleListPlayer(Request res){
            ArrayList<User> list = (ArrayList<User>) res.getData();
            if(home instanceof Home){
                home.showListPlayer(list);
            }
            
        }
        private void handleLogin(Request response) {
            if(response.getData() instanceof User){
                myUser = (User) response.getData();
                login.showM("Login Successfully!");
                home = new Home();
                home.setVisible(true);
                home.addListBtnRank(new ListenBtnRank());
                home.addListenTable(new ListenTableMain());
                getListPlayer();
                login.dispose();
            }
            else{
                login.showM("Login Failed!");
            }
        }
        class ListenBtnRank implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
               Request req = new Request("viewRank");
               send(req);
            }
            
        }
        
        private void handleRegister(Request response) {
            String msg = (String) response.getData();
            if(msg.equals("success")){
                register.showM("Register Successfully!");
                register.dispose();;
                login.setVisible(true);
            }
            else if(msg.equals("Username exist")){
                register.showM(msg);
            }
        }
    }
     
    class ListenBtnLogin implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            User user = login.getUser();
            Request req = new Request("login", (Object)user);
            send(req);
        }
    }
    class ListenBtnRegister implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            login.dispose();
            register = new Register();
            register.setVisible(true);
            register.addListenBtnRegisterMain(new listenBtnRegisterMain());
            register.addListenCancelRegister(new listenCancelRegister());
        }
        
    }
    class listenBtnRegisterMain implements ActionListener{

        public listenBtnRegisterMain() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(register.checkEmpty()){
                User user = register.getUser();
                Request req = new Request("register", (Object)user);
                send(req);
            }
        }
    }
    class listenCancelRegister implements ActionListener{

        public listenCancelRegister() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            register.dispose();
//            login = new Login();
            login.setVisible(true);
//            login.addListenBtnLogin(new ListenBtnLogin());
//            login.addListenBtnRegister(new ListenBtnRegister());
        }
        
    }
    private void send(Request req) {
        try {
            oos.writeObject(req);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void open() {
        try {
            clientSocet = new Socket(serverName, port);
            oos = new ObjectOutputStream(clientSocet.getOutputStream());
            ois = new ObjectInputStream(clientSocet.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
