package net.bluehack.kiosk.cart.vo;

import java.io.Serializable;
import java.util.List;

public class CartMenuItem implements Serializable {

    private String menu_id;
    private String menu_name;
    private String menu_price;
    private String menu_point;
    private String menu_image;
    private String menu_description;
    private String menu_calory;
    private List<CartMenuRequireOptionItem> cartMenuRequireOptionItems;
    private List<CartMenuOptionItem> cartMenuOptionItems;

    public CartMenuItem() {
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_point() {
        return menu_point;
    }

    public void setMenu_point(String menu_point) {
        this.menu_point = menu_point;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getMenu_description() {
        return menu_description;
    }

    public void setMenu_description(String menu_description) {
        this.menu_description = menu_description;
    }

    public String getMenu_calory() {
        return menu_calory;
    }

    public void setMenu_calory(String menu_calory) {
        this.menu_calory = menu_calory;
    }

    public List<CartMenuRequireOptionItem> getCartMenuRequireOptionItems() {
        return cartMenuRequireOptionItems;
    }

    public void setCartMenuRequireOptionItems(List<CartMenuRequireOptionItem> cartMenuRequireOptionItems) {
        this.cartMenuRequireOptionItems = cartMenuRequireOptionItems;
    }

    public List<CartMenuOptionItem> getCartMenuOptionItems() {
        return cartMenuOptionItems;
    }

    public void setCartMenuOptionItems(List<CartMenuOptionItem> cartMenuOptionItems) {
        this.cartMenuOptionItems = cartMenuOptionItems;
    }
}
