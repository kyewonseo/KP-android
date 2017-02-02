package net.bluehack.kiosk.store;

import java.util.ArrayList;
import java.util.List;

public class StoreData {

    public StoreData (){}
    private int storeKey;
    private ArrayList<StoreItem> storeItems;

    public int getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(int storeKey) {
        this.storeKey = storeKey;
    }

    public ArrayList<StoreItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(ArrayList<StoreItem> storeItems) {
        this.storeItems = storeItems;
    }
}
