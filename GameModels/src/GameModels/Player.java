/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModels;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Vu Tien Hoa
 */
public class Player {
    private Socket socket;
    private int status;
    public User user;
    public ObjectOutputStream oos;
    public ObjectInputStream ois ;
    public int sum = 0;
    public Player(Socket socket){
        this.socket = socket;
       
    }
    public Socket getSocket(){
        return this.socket;
    }
    public int getStatus(){
        return this.status;
    }
}
