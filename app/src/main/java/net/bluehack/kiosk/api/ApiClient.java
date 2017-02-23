package net.bluehack.kiosk.api;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.KioskAPIClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.model.LoginReq;
import net.bluehack.kiosk.model.LoginRes;
import net.bluehack.kiosk.model.StoresRes;

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

    /*public void menuGet(final String query, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Menu output = null;

                output = client.menuGet(query);
                Log.e("menuGet:", String.valueOf(output));

                listener.onResponse(output);

                return null;
            }

        }.execute();
    }*/
}
