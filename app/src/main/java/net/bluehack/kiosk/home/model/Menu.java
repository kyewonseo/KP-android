package net.bluehack.kiosk.home.model;

import java.util.List;

public class Menu {

    String menu;
    String image;
    int count;

    int point;
    int price;
    List<String> options;

    public Menu() {
    }

    // Fixme : UI Dummy를 위한 생성자로 삭제
    public Menu(String menu, String image, int count) {
        this.menu = menu;
        this.image = image;
        this.count = count;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
