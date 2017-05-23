package net.bluehack.kiosk.log;

import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.analytics.HitBuilders;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import io.swagger.client.model.AddOrderReq;

public class LogEventTracker {

    public static void userDeviceInfo(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put(DeviceInfo.USER_APP_VERSION_CODE_NAME, Build.VERSION.CODENAME);
            props.put(DeviceInfo.USER_APP_VERSION_RELEASE, Build.VERSION.RELEASE);
            props.put(DeviceInfo.USER_DEVICE, Build.DEVICE);
            props.put(DeviceInfo.USER_MODEL, Build.MODEL);
            props.put(DeviceInfo.USER_PRODUCT, Build.PRODUCT);
            props.put(DeviceInfo.USER_BRAND, Build.BRAND);

            pushEvent(gaCategory, LogEvent.USER_PROFILE, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void LoginBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.LOGIN_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void ViewAllBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);

            pushEvent(gaCategory, LogEvent.VIEW_ALL_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void NavMenuBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.NAV_MENU_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void MoblieOrderBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.MOBILE_ORDER_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void SlideMyInfoBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.MY_INFO_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void SlideStoreBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);

            pushEvent(gaCategory, LogEvent.STORE_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void SlideOrderHistoryBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);

            pushEvent(gaCategory, LogEvent.ORDER_HISTORY_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void SlideContactusBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("","");

            pushEvent(gaCategory, LogEvent.CONTACTUS_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void LogoutBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("","");

            pushEvent(gaCategory, LogEvent.LOGOUT_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void OrderHistoryEvent(String gaCategory, String order_id) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.ORDER_ID, order_id);
            props.put(LogEventKey.APP_LOCATION, gaCategory);

            pushEvent(gaCategory, LogEvent.ORDER_HISTORY, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void TapAddCartEvent(String gaCategory, CartMenuItem order_detail) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);
            props.put(LogEventKey.ORDER_DETAIL, UiUtil.toStringGson(order_detail));

            pushEvent(gaCategory, LogEvent.TAP_ADD_CART, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void CartBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);

            pushEvent(gaCategory, LogEvent.CART_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void ContactUsEvent(String gaCategory, String phone_num) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.PHONE_NUM, phone_num);

            pushEvent(gaCategory, LogEvent.CONTACT_US, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void TapCategoryEvent(String gaCategory, String category_name) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.CATEGORY, category_name);

            pushEvent(gaCategory, LogEvent.TAP_CATEGORY, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void TapMenuEvent(String gaCategory, String menu_id) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.MENU, menu_id);

            pushEvent(gaCategory, LogEvent.TAP_MENU, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void OrderBtnEvent(String gaCategory, ArrayList<CartMenuItem> cartMenuItems) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);
            props.put(LogEventKey.ORDER_DETAIL, UiUtil.toStringGson(cartMenuItems));

            pushEvent(gaCategory, LogEvent.ORDER_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void RemoveBtnEvent(String gaCategory, String menu_id) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.APP_LOCATION, gaCategory);
            props.put(LogEventKey.MENU, menu_id);

            pushEvent(gaCategory, LogEvent.REMOVE_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void TapStoreEvent(String gaCategory, String store_name) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.TO_STORE, store_name);

            pushEvent(gaCategory, LogEvent.TAP_STORE, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void ChangeStoreEvent(String gaCategory, String store_name) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.FROM_STORE, store_name);

            pushEvent(gaCategory, LogEvent.CHANGE_STORE_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void TapRecentOrderEvent(String gaCategory, String order_id) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.RECENT_ORDER, order_id);

            pushEvent(gaCategory, LogEvent.TAP_RECENT_ORDER, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void PaymentBtnEvent(String gaCategory, AddOrderReq orderItems) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.ORDER_DETAIL, UiUtil.toStringGson(orderItems));

            pushEvent(gaCategory, LogEvent.PAYMENT_BTN, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void OnUsePointsBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.ON_USEPOINTS, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void OffUsePointsBtnEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.OFF_USEPOINTS, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void OpenWebViewEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.OPEN_WEBVIEW, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void CloseWebViewEvent(String gaCategory) {
        try {
            JSONObject props = new JSONObject();
            props.put("", "");

            pushEvent(gaCategory, LogEvent.CLOSE_WEBVIEW, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }


    public static void OpenPopupEvent(String gaCategory, String popup_name) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.POPUP_NAME, popup_name);

            pushEvent(gaCategory, LogEvent.OPEN_POPUP, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }

    public static void ClosePopupEvent(String gaCategory, String popup_name, String button_label) {
        try {
            JSONObject props = new JSONObject();
            props.put(LogEventKey.POPUP_NAME, popup_name);
            props.put(LogEventKey.BUTTON_LABEL, button_label);

            pushEvent(gaCategory, LogEvent.CLOSE_POPUP, props);
        } catch (JSONException e) {
            //LOGE(TAG, "Unable to add properties to JSONObject", e);
        }
    }


    public static void pushEvent(String gaCategory, String event, JSONObject properties) {
        //Firebase log event
        Bundle bundle = new Bundle();
        Iterator<String> keys = properties.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = properties.get(key);
                if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof Long) {
                    bundle.putLong(key, (Long) value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ApplicationLoader.getFirebaseAnalytics().logEvent(event, bundle);
        ApplicationLoader.getGaTracker().send(new HitBuilders.EventBuilder()
                .setCategory(gaCategory)
                .setAction(event)
                .setLabel(ApplicationLoader.getUserId())
                .build());
    }
}
