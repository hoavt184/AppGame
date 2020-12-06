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

    class Listening extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

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

        private void handleLogin(Request response) {
            Request req = null;
            try {

                User user = (User) response.getData();
                String query = "select * from users where username=? and password=? limit 1";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    req = new Request("login", (Object) user);
                    send(req);
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            req = new Request("login", (Object) false);
            send(req);
        }

        private void handleRegister(Request response) {
            User user = (User) response.getData();
            String responseMsg = "failed";
            if (!checkExistUser(user.getUsername())) {
                try {
                    String query = "insert into users(name,username,password) values(?,?,?)";
                    PreparedStatement ps;
                    ps = con.prepareStatement(query);
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getUsername());
                    ps.setString(3, user.getPassword());
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
                String query = "select * from users where username=? limit 1";
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
            String dbName = "appgame";
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
