/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GameModels.Request;
import GameModels.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public ClientController(){
        open();
        login =new Login();
        login.setVisible(true);
        login.addListenBtnLogin( new ListenBtnLogin());
        login.addListenBtnRegister(new ListenBtnRegister());
        new Listening().start();
        new updateOnline().start();
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
    public void getListPlayer(){
            Request req = new Request("getListPlayer");
            send(req);
     }
    class Listening extends Thread{
        
        @Override
        public void run() {
//            super.run(); //To change body of generated methods, choose Tools | Templates.
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
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        class byScore implements Comparator<User>{

            @Override
            public int compare(User o1, User o2) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(o1.getScore()>o2.getScore()) return 1;
                if(o1.getScore()<o2.getScore()) return -1;
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
        public void showRankScore(ArrayList<User> l, Rank rank){
            Collections.sort(l,new byScore());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :l){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getScore())));
            }
            rank.showRank(ranks);
            
        }
        public void showRankWin(ArrayList<User> l, Rank rank){
            Collections.sort(l,new byWin());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :l){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getAverageMoveWinMatch())));
            }
            rank.showRank(ranks);
            
        }
        public void showRankLost(ArrayList<User> l, Rank rank){
            Collections.sort(l,new byLost());
            ArrayList<Pair<String,String>> ranks = new ArrayList<>();
            for(User u :l){
                ranks.add(new Pair(u.getUserName(),String.valueOf(u.getAverageMoveLostMatch())));
            }
            rank.showRank(ranks);
            
        }
        public void handleRank(Request res){
            ArrayList<User> listRank=(ArrayList<User>) res.getData();
            Rank rank = new Rank();
            rank.setVisible(true);
            showRankWin(listRank, rank);
        }
        public void handleListPlayer(Request res){
            ArrayList<User> list = (ArrayList<User>) res.getData();
            home.showListPlayer(list);
        }
        private void handleLogin(Request response) {
            if(response.getData() instanceof User){
                myUser = (User) response.getData();
                login.showM("Login Successfully!");
                home = new Home();
                home.setVisible(true);
                home.addListBtnRank(new ListenBtnRank());
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
