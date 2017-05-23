package net.bluehack.kiosk.util;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.bluehack.kiosk.cart.vo.CartMenuItem;

import io.swagger.client.model.LoginResData;
import io.swagger.client.model.StoresResData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class KioskPreference {

    private static final String TAG = makeLogTag(KioskPreference.class);
    private Context context;

    public static final String LOGIN_INFO = "pref_login_info";
    public static final String STORE_INFO = "pref_store_info";
    public static final String CART_INFO = "pref_cart_info";

    private static ArrayList<CartMenuItem> setCartMenuList = new ArrayList<CartMenuItem>();

    private static class SingletonHolder {
        static final KioskPreference INSTANCE = new KioskPreference();
    }

    public static KioskPreference getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    public LoginResData getLoginInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String loginText = sp.getString(LOGIN_INFO, null);
        //LOGD(TAG, "getStoreInfo =>" + loginText);
        Gson gson = new Gson();
        LoginResData loginItem = gson.fromJson(loginText, LoginResData.class);

        return loginItem;
    }

    public void setLoginInfo(LoginResData loginResDataItem) {
        String loginInfo = UiUtil.toStringGson(loginResDataItem);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(LOGIN_INFO, loginInfo).apply();
    }

    public void clearLoginInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(LOGIN_INFO);
        editor.apply();
    }

    public StoresResData getStoreInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String storeText = sp.getString(STORE_INFO, null);
        //LOGD(TAG, "getStoreInfo =>" + storeText);
        Gson gson = new Gson();
        StoresResData storeItem = gson.fromJson(storeText, StoresResData.class);

        return storeItem;
    }

    public void setStoreInfo(StoresResData storesResDataItem) {
        String storeInfo = UiUtil.toStringGson(storesResDataItem);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(STORE_INFO, storeInfo).apply();
    }

    public void clearStoreInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(STORE_INFO);
        editor.apply();
    }

    public ArrayList<CartMenuItem> getCartInfo() {

        ArrayList<CartMenuItem> getCartMenuList = new ArrayList<CartMenuItem>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String cartList = sp.getString(CART_INFO, null);

        if (cartList == null) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(cartList);
            CartMenuItem item = null;
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    item = gson.fromJson(jsonObject.toString(), CartMenuItem.class);
                }
                getCartMenuList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getCartMenuList;
    }

    public void setCartInfo(CartMenuItem cartMenuItem) {

        if (getCartInfo() != null) {
            setCartMenuList = getCartInfo();
        }
        setCartMenuList.add(cartMenuItem);

        ArrayList<String> cartItems = new ArrayList<>();
        for (CartMenuItem cartInfo : setCartMenuList) {
            String newCartMenuList = UiUtil.toStringGson(cartInfo);
            cartItems.add(newCartMenuList);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(CART_INFO, cartItems.toString()).apply();

    }

    public void deleteCartInfo(int index) {
        ArrayList<CartMenuItem> cartMenuList = getCartInfo();

        LOGD(TAG, "deleteCartInfo index =>" + index);
        LOGD(TAG, "deleteCartInfo size =>" + cartMenuList.size());
        LOGD(TAG, "deleteCartInfo cartMenuList(index) =>" + cartMenuList.get(index));

        cartMenuList.remove(index);

        clearCartInfo();
        setCartMenuList = cartMenuList;

        ArrayList<String> cartItems = new ArrayList<>();
        for (CartMenuItem cartInfo : setCartMenuList) {
            String newCartMenuList = UiUtil.toStringGson(cartInfo);
            cartItems.add(newCartMenuList);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(CART_INFO, cartItems.toString()).apply();
    }

    public void clearCartInfo() {

        if (setCartMenuList != null) {
            setCartMenuList.clear();
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(CART_INFO);
        editor.apply();
    }
}