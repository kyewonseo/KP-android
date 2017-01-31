package net.bluehack.kiosk.main;

public class RecentItem {
    private String menu_idx;
    private String category;
    private String name;
    private String calory;
    private String price;
    private String description;
    private String barcode;
    private String use_YN;

    public RecentItem(){}

    public String getMenu_idx() {
        return menu_idx;
    }

    public void setMenu_idx(String menu_idx) {
        this.menu_idx = menu_idx;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalory() {
        return calory;
    }

    public void setCalory(String calory) {
        this.calory = calory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUse_YN() {
        return use_YN;
    }

    public void setUse_YN(String use_YN) {
        this.use_YN = use_YN;
    }
}
