package net.bluehack.kiosk.api;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.KioskAPIClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.model.LoginReq;
import net.bluehack.kiosk.model.LoginRes;
import net.bluehack.kiosk.model.MenuRes;
import net.bluehack.kiosk.model.StoresRes;
import net.bluehack.kiosk.model.SubcategoryRes;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class ApiClient {
    private static final String TAG = makeLogTag(ApiClient.class);
    private static ApiClient ourInstance = new ApiClient();
    private final ApiClientFactory factory;
    private final KioskAPIClient client;
    private final static String headerToken = "temp";

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
        factory = new ApiClientFactory();
        client = factory.build(KioskAPIClient.class);
    }

    public interface ApiResponseListener {
        void onResponse(Object result);
    }

    public KioskAPIClient getClient() {
        return client;
    }

    public void usersLoginPost(final LoginReq loginReq, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                LoginRes output = null;
                output = client.usersLoginPost(headerToken, loginReq);
                LOGD("usersLoginPost status :", String.valueOf(output.getResponseStatus()));

                listener.onResponse(output);
                return null;
            }

        }.execute();
    }

    public void storesAccountIdGet(final String accountId, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                StoresRes output = null;
                output = client.storesAccountIdGet(headerToken, accountId);
                LOGD("StoresRes status :", String.valueOf(output.getResponseStatus()));

                listener.onResponse(output);
                return null;
            }

        }.execute();
    }

    public void categoriesSubcategoriesStoreIdGet(final String store_id, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                SubcategoryRes output = null;

                //TODO: fixme => sample data에서 store_id로 연결된 매핑데이터를 찾기 힘든점 때문에 "store_id" 값을 하드코딩하여 테스트함
                output = client.categoriesSubcategoriesStoreIdGet("449", headerToken);

                listener.onResponse(output);
                return null;
            }

        }.execute();
    }

    public void menuSubCategoryIdGet(final String sub_category_id, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                MenuRes output = null;

                output = client.menuSubCategoryIdGet(sub_category_id, headerToken);

                listener.onResponse(output);
                return null;
            }

        }.execute();
    }
}
