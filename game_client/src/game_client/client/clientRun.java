/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_client.client;

import game_client.client.view.Login;

/**
 *
 * @author Vu Tien Hoa
 */
public class clientRun {
    public static void main(String[] args) {
        System.out.println("client is running...");
        Login view = new Login();
        view.setVisible(true);
    }
}
