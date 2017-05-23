package net.bluehack.kiosk.cart.vo;

import java.io.Serializable;

public class CartMenuOptionItem implements Serializable {

    private String menu_option_id;
    private String menu_option_name;
    private String menu_option_price;
    private String menu_option_calory;
    private boolean isChecked;

    public String getMenu_option_id() {
        return menu_option_id;
    }

    public void setMenu_option_id(String menu_option_id) {
        this.menu_option_id = menu_option_id;
    }

    public String getMenu_option_name() {
        return menu_option_name;
    }

    public void setMenu_option_name(String menu_option_name) {
        this.menu_option_name = menu_option_name;
    }

    public String getMenu_option_price() {
        return menu_option_price;
    }

    public void setMenu_option_price(String menu_option_price) {
        this.menu_option_price = menu_option_price;
    }

    public String getMenu_option_calory() {
        return menu_option_calory;
    }

    public void setMenu_option_calory(String menu_option_calory) {
        this.menu_option_calory = menu_option_calory;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
