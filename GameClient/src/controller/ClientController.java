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
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Home;
import view.Login;
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
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void handleLogin(Request response) {
            if(response.getData() instanceof User){
                myUser = (User) response.getData();
                login.showM("Login Successfully!");
                home = new Home();
                home.setVisible(true);
                login.dispose();
            }
            else{
                login.showM("Login Failed!");
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
