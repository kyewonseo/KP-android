package net.bluehack.kiosk.home.model;

import java.util.List;

public class Ticket {

    public enum State {
        Payment,
        Prepare,
        InCook,
        Ready,
        Expire
    }

    ;

    public static final int PAYMENT = 1;
    public static final int RECEIVE = 2;
    public static final int MAKING = 3;
    public static final int COMPLETE = 4;
    public static final int EXPIRE = 0;

    int state;

    String orderNo;
    String date;
    String time;
    String price;

    List<Menu> menus;

    Store store;

    public Ticket() {
    }

    // Fixme : 기본 생성자로만 처리하기
    public Ticket(int state, String orderNo,
                  String date, String time,
                  String price, List<Menu> menus) {
        this.state = state;
        this.orderNo = orderNo;
        this.date = date;
        this.time = time;
        this.price = price;
        this.menus = menus;
    }

    public static int getPAYMENT() {
        return PAYMENT;
    }

    public static int getRECEIVE() {
        return RECEIVE;
    }

    public static int getMAKING() {
        return MAKING;
    }

    public static int getCOMPLETE() {
        return COMPLETE;
    }

    public static int getEXPIRE() {
        return EXPIRE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }


}