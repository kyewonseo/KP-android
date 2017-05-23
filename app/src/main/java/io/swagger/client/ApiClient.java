package io.swagger.client;

import android.content.Context;
import android.os.AsyncTask;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.util.UiUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.api.AccountApi;
import io.swagger.client.api.CategoryApi;
import io.swagger.client.api.MenuApi;
import io.swagger.client.api.OrderApi;
import io.swagger.client.api.StoreApi;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.AddOrderRes;
import io.swagger.client.model.LoginReq;
import io.swagger.client.model.LoginRes;
import io.swagger.client.model.MenuOptionReq;
import io.swagger.client.model.MenuOptionRes;
import io.swagger.client.model.MenuReq;
import io.swagger.client.model.MenuRes;
import io.swagger.client.model.OrderCompleteReq;
import io.swagger.client.model.OrderCompleteRes;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;
import io.swagger.client.model.StoresReq;
import io.swagger.client.model.StoresRes;
import io.swagger.client.model.SubcategoryReq;
import io.swagger.client.model.SubcategoryRes;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class ApiClient {
    private static final String TAG = makeLogTag(ApiClient.class);
    private static ApiClient ourInstance = new ApiClient();
    private final String header = "application/json";

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
    }

    public interface ApiResponseListener {
        void onResponse(Object result);
    }

    public void accountLoginPost(final Context context, final LoginReq loginReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                LoginRes output = null;
                AccountApi api = new AccountApi();
                try {
                    output = api.accountLoginPost(header, loginReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void storesAccountListPost(final Context context, final StoresReq storesReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                StoresRes output = null;
                StoreApi api = new StoreApi();
                try {
                    output = api.storeAccountListPost(header, storesReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void categoryListMenuPost(final Context context, final SubcategoryReq subcategoryReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                SubcategoryRes output = null;
                CategoryApi api = new CategoryApi();
                try {
                    output = api.categoryListMenuPost(header, subcategoryReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void menuListPost(final Context context, final MenuReq menuReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                MenuRes output = null;
                MenuApi api = new MenuApi();
                try {
                    output = api.menuListPost(header, menuReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void menuDetailPost(final Context context, final MenuOptionReq menuOptionReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                MenuOptionRes output = null;
                MenuApi api = new MenuApi();
                try {
                    output = api.menuDetailPost(header, menuOptionReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void orderPaymentListPost(final Context context, final OrderReq orderReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                OrderRes output = null;
                OrderApi api = new OrderApi();
                try {
                    output = api.orderPaymentListPost(header, orderReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void ordersAddPost(final Context context, final AddOrderReq addOrderReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                AddOrderRes output = null;
                OrderApi api = new OrderApi();
                try {
                    output = api.orderAddPost(header, addOrderReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }

    public void ordersPaymentCompletePost(final Context context, final OrderCompleteReq orderCompleteReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                OrderCompleteRes output = null;
                OrderApi api = new OrderApi();
                try {
                    output = api.orderPaymentCompletePost(header, orderCompleteReq);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                } catch (ApiException e) {
                    e.printStackTrace();
                    NetworkManager.getInstance().showServerErrorPopup(context);
                }
                return null;
            }

        }.execute();
    }
}

