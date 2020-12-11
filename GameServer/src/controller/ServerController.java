/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GameModels.*;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu Tien Hoa
 */
public class ServerController {

    private ServerSocket serverSocket;
    private Map<String, Player> listPlayer = new HashMap<>();
    private Connection con;

    public ServerController() {
        try {
            open();
            con = getDBConnection();
            new update().start();
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    
                    
                    new Listening(clientSocket).start();
                   

                } catch (IOException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    class update extends  Thread{
        public void run(){
            while(true){
                try {
                    this.sleep(5000);
                    updateOnline();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public void updateOnline(){
        for(Map.Entry<String,Player> p :listPlayer.entrySet()){
            if(p.getValue().getSocket().isClosed()){
                listPlayer.remove(p.getKey());
                
                break;
            }
        }
        System.out.println(listPlayer.size());
    }
    class Listening extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private User user;

        public Listening(Socket clientSocket) {
            try {
                this.clientSocket = clientSocket;
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        @Override
        public void run() {
            while (clientSocket.isConnected()) {                
                try {
                    Request response = (Request) ois.readObject();
                    switch(response.getAction()){
                        case "login":
                            handleLogin(response);
                            break;
                        case "register":
                            handleRegister(response);
                            break;
                        case "getListPlayer":
                            sendListPlayer();
                            break;
                        case "viewRank":
                            handleRank();
                            break;
                        case "challenge":
                            forwardInvite(response);
                            break;
                        case "acceptInvite":
                            handleAccept(response);
                            break;
                        case "Sur":
                            handleSur(response);
                            break;
                    }
                } catch (IOException ex) {
                    try {
                        clientSocket.close();
                        break;
//                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex1) {
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
                
            }
        }
        public void handleSur(Request res){
            String user =(String) res.getData();
            float score = getScore(user);
            score++;
            updateScore(user,score);
            int total = getTotalWinMatch(user);
            total++;
            updateTotalWinMatch(user, total);
            for(Map.Entry<String, Player> p :listPlayer.entrySet()){
                if(p.getKey().equals(user)){
                    try {
                        Request req = new Request("youWin",(Object)this.user.getUserName());
                        p.getValue().oos.writeObject(req);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
          
        }
        public void updateTotalWinMatch(String user, int total){
            try {
                String sql = "update tbl_user set totalWinMatch=? where userName=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1,total);
                ps.setString(2, user);
                ps.execute();
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void updateScore(String user,float score){
            try {
                String sql = "update tbl_user set score=? where userName=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setFloat(1,score);
                ps.setString(2, user);
                ps.execute();
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public int getTotalLostMatch(String user){
            try {
                String sql ="select * from tbl_user where userName=?";
                PreparedStatement ps =con.prepareStatement(sql);
                ps.setString(1,user);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt("totalLostMatch");
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
            
        }
        public float getScore(String user){
             try {
                String sql ="select * from tbl_user where userName=?";
                PreparedStatement ps =con.prepareStatement(sql);
                ps.setString(1,user);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt("score");
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
        public int getTotalWinMatch(String user){
             try {
                String sql ="select * from tbl_user where userName=?";
                PreparedStatement ps =con.prepareStatement(sql);
                ps.setString(1,user);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt("totalWinMatch");
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
        
        public void handleAccept(Request res){
            String user = (String) res.getData();
            Request req = new Request("play",(Object)user);
            send(req);
            for(Map.Entry<String ,Player> p : listPlayer.entrySet()){
                if(p.getKey().equals(this.user.getUserName())){
                    p.getValue().user.setStatus(2);
                    break;
                }
            }
            for(Map.Entry<String, Player> p :listPlayer.entrySet()){
                if(p.getKey().equals(user)){
                    try {
                        p.getValue().user.setStatus(2);
                        Request req1 = new Request("play",(Object)this.user.getUserName());
                        p.getValue().oos.writeObject(req1);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        public void forwardInvite(Request res){
            String userName = (String) res.getData();
            for(Map.Entry<String, Player> p : listPlayer.entrySet()){
                if(p.getKey().equals(userName)){
                    try {
                        Request req = new Request("sendInvite",(Object)this.user.getUserName());
                        p.getValue().oos.writeObject(req);
                        break;
                    } catch (IOException ex) {
                        
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        public void handleRank(){
            ArrayList<User> res = getListPlayer();
            Request req = new Request("sendRank",(Object) res);
            send(req);
        }
        public ArrayList<User> getListPlayer(){
            ArrayList<User> res = new ArrayList<>();
            try {
                String sql ="select * from tbl_user";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    res.add(new User(rs.getString("userName"), rs.getFloat("averageMoveWinMatch"),rs.getFloat("averageMoveLostMatch"), rs.getFloat("score")));
//                    res.add(new User(rs.getString("userName"), rs.getFloat("averageMoveWinMatch"),rs.getFloat("averageMoveLostMatch"), rs.getFloat("score")));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return res;
            
        }
        public void sendListPlayer(){
            ArrayList<User> res= new ArrayList<>();
            for(Map.Entry<String, Player> p : listPlayer.entrySet()){
                res.add(p.getValue().user);
            }
            Request req = new Request("sendListPlayer",(Object)res);
            send(req);
        }
        
        private void handleLogin(Request response) {
            Request req = null;
            try {

                User user = (User) response.getData();
                String query = "select * from tbl_user where userName=? and passWord=? limit 1";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassWord());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    this.user = user;
                    req = new Request("login", (Object) user);
                    Player player = new Player(this.clientSocket);
                    player.user = getUser(user.getUserName());
                    player.oos = this.oos;
                    player.user.setStatus(1);
                    
                    listPlayer.put(this.user.getUserName(),player);
                    send(req);
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            req = new Request("login", (Object) false);
            send(req);
        }
        public User getUser(String userName){
            User u = null;
            try {
                String sql ="select * from tbl_user where userName=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1,userName);
                ResultSet rs = ps.executeQuery();
                rs.next();
                u = new User(rs.getString("userName"),rs.getFloat("score"));
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return u;
        }
        private void handleRegister(Request response) {
            User user = (User) response.getData();
            String responseMsg = "failed";
            if (!checkExistUser(user.getUserName())) {
                try {
                    String query = "insert into tbl_user(name,userName,passWord) values(?,?,?)";
                    PreparedStatement ps;
                    ps = con.prepareStatement(query);
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getUserName());
                    ps.setString(3, user.getPassWord());
                    ps.execute();
                    responseMsg = "success";
                    send(new Request("register", (Object) responseMsg));
                } catch (SQLException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                responseMsg = "Username exist";
                send(new Request("register", (Object) responseMsg));
            }
            responseMsg = "failed";
            send(new Request("register", (Object) responseMsg));
            return;
        }

        private void send(Request req) {
            try {
                this.oos.writeObject(req);
                oos.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private boolean checkExistUser(String username) {
            try {
                String query = "select * from tbl_user where userName=? limit 1";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

    }

    private void open() throws IOException {
        serverSocket = new ServerSocket(8889);
    }

    private Connection getDBConnection() {
        Connection con = null;
        try {
            String dbUsername = "root";
            String dbPassword = "";
            String dbName = "btl";
            String url = "jdbc:mysql://localhost/" + dbName;

            String className = "com.mysql.jdbc.Driver";

            Class.forName(className);
            con = (Connection) DriverManager.getConnection(url, dbUsername, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
