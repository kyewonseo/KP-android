package net.bluehack.kiosk.home.model;

public class Store {

    String name;
    String address;
    String image;

    public Store() {
    }

    public Store(String name, String address, String image) {
        this.name = name;
        this.address = address;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}