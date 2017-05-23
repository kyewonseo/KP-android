package net.bluehack.kiosk.cart.vo;

import java.io.Serializable;

public class CartMenuRequireOptionItem implements Serializable {

    private String menu_rq_option_id;
    private String menu_rq_option_name;
    private String menu_rq_option_price;
    private String menu_rq_option_calory;
    private boolean isChecked;
    private boolean requiredChecked;

    public String getMenu_rq_option_id() {
        return menu_rq_option_id;
    }

    public void setMenu_rq_option_id(String menu_rq_option_id) {
        this.menu_rq_option_id = menu_rq_option_id;
    }

    public String getMenu_rq_option_name() {
        return menu_rq_option_name;
    }

    public void setMenu_rq_option_name(String menu_rq_option_name) {
        this.menu_rq_option_name = menu_rq_option_name;
    }

    public String getMenu_rq_option_price() {
        return menu_rq_option_price;
    }

    public void setMenu_rq_option_price(String menu_rq_option_price) {
        this.menu_rq_option_price = menu_rq_option_price;
    }

    public String getMenu_rq_option_calory() {
        return menu_rq_option_calory;
    }

    public void setMenu_rq_option_calory(String menu_rq_option_calory) {
        this.menu_rq_option_calory = menu_rq_option_calory;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isRequiredChecked() {
        return requiredChecked;
    }

    public void setRequiredChecked(boolean requiredChecked) {
        this.requiredChecked = requiredChecked;
    }
}
