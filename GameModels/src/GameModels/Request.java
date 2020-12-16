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
public class Request implements Serializable{
    private String action;
    private Object data;
    private Object data2;
    public Object getData2(){
        return this.data2;
    }
    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }
     public Request(String action, Object data,Object data2) {
        this.action = action;
        this.data = data;
        this.data2 = data2;
    }
    public Request(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
