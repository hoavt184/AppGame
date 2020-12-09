/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModels;

import java.io.Serializable;

/**
 *
 * @author Vu Tien Hoa
 */
public class User implements Serializable{
    private int user_id;
    private String name,userName,passWord;
    private float score,averageMoveWinMatch,averageMoveLostMatch;
    
    private int status,totalWinMatch,totalLostMatch,totalMoveWinMatch,totalMoveLost;

     public User() {
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public User(String name, String userName, String passWord) {
        this.name = name;
        this.userName = userName;
        this.passWord = passWord;
    }

    public User(String userName, float score, int status) {
        this.userName = userName;
        this.score = score;
        this.status = status;
    }

    public User(String userName, float averageMoveWinMatch, float averageMoveLostMatch, float score) {
        this.userName = userName;
        this.averageMoveWinMatch = averageMoveWinMatch;
        this.averageMoveLostMatch = averageMoveLostMatch;
        this.score = score;
    }

    public User(String userName, float score) {
        this.userName = userName;
        this.score = score;
    }

    
     
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getAverageMoveWinMatch() {
        return averageMoveWinMatch;
    }

    public void setAverageMoveWinMatch(float averageMoveWinMatch) {
        this.averageMoveWinMatch = averageMoveWinMatch;
    }

    public float getAverageMoveLostMatch() {
        return averageMoveLostMatch;
    }

    public void setAverageMoveLostMatch(float averageMoveLostMatch) {
        this.averageMoveLostMatch = averageMoveLostMatch;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalWinMatch() {
        return totalWinMatch;
    }

    public void setTotalWinMatch(int totalWinMatch) {
        this.totalWinMatch = totalWinMatch;
    }

    public int getTotalLostMatch() {
        return totalLostMatch;
    }

    public void setTotalLostMatch(int totalLostMatch) {
        this.totalLostMatch = totalLostMatch;
    }

    public int getTotalMoveWinMatch() {
        return totalMoveWinMatch;
    }

    public void setTotalMoveWinMatch(int totalMoveWinMatch) {
        this.totalMoveWinMatch = totalMoveWinMatch;
    }

    public int getTotalMoveLost() {
        return totalMoveLost;
    }

    public void setTotalMoveLost(int totalMoveLost) {
        this.totalMoveLost = totalMoveLost;
    }
    
   
    
}
