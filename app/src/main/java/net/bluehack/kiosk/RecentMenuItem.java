package net.bluehack.kiosk;

public class RecentMenuItem {
    private String title;
    private String kind;
    private int img;

    public RecentMenuItem(String title, String kind, int img) {
        this.title = title;
        this.kind = kind;
        this.img = img;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
