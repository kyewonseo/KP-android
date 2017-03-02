package net.bluehack.kiosk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import net.bluehack.kiosk.cart.CartMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KioskPreference {

    private Context context;

    public static final String LOGIN_INFO = "pref_login_info";
    public static final String ACCOUNT_ID = "pref_account_id";
    public static final String STORE_ID = "pref_store_id";
    public static final String STORE_NAME = "pref_store_name";
    public static final String CART_INFO = "pref_cart_info";

    public static ArrayList<String> setCartMenuList = new ArrayList<String>();

    private static class SingletonHolder {
        static final KioskPreference INSTANCE = new KioskPreference();
    }

    public static KioskPreference getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    public String getLoginInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(LOGIN_INFO, null);
    }

    public void setLoginInfo(String loginResDataItem) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("preference: ", loginResDataItem);
        sp.edit().putString(LOGIN_INFO, loginResDataItem).apply();
    }
    public void clearLoginInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.getString(LOGIN_INFO, null);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public String getAccountId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(ACCOUNT_ID, null);
    }

    public void setAccountId(String accountId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(ACCOUNT_ID, accountId).apply();
    }
    public void clearAccountId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.getString(ACCOUNT_ID, null);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public String getStoreId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(STORE_ID, null);
    }

    public void setStoreId(String storeId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(STORE_ID, storeId).apply();
    }
    public void clearStoreId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.getString(STORE_ID, null);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public String getStoreName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(STORE_NAME, null);
    }

    public void setStoreName(String storeName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(STORE_NAME, storeName).apply();
    }
    public void clearStoreName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.getString(STORE_NAME, null);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public ArrayList<CartMenuItem> getCartInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String cartList = sp.getString(CART_INFO, null);
        final ArrayList<CartMenuItem>  getCartMenuList = new ArrayList<CartMenuItem>();
        try {
            JSONArray jsonArray = new JSONArray(cartList);
            Gson gson = new Gson();
            for (int i = 0; i <jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CartMenuItem item = gson.fromJson(jsonObject.toString(),CartMenuItem.class);
                getCartMenuList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getCartMenuList;
    }

    public void setCartInfo(String cartMenuItem) {
        setCartMenuList.add(cartMenuItem);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(CART_INFO, setCartMenuList.toString()).apply();

    }

    public void deleteCartInfo(int index) {
        ArrayList<CartMenuItem> cartMenuList = null;
        cartMenuList = getCartInfo();

        if (cartMenuList != null && cartMenuList.size() != 0) {
            cartMenuList.remove(index);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putString(CART_INFO, cartMenuList.toString()).apply();

        }

    }
    //TODO: fixme => store가 바뀔때 clear
    public void clearCartInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.getString(CART_INFO, null);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}