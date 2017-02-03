package net.bluehack.kiosk.api;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.bluehack.kiosk.KioskAPIClient;
import net.bluehack.kiosk.main.RecentCardAdapter;
import net.bluehack.kiosk.main.RecentItem;
import net.bluehack.kiosk.model.Menu;
import net.bluehack.kiosk.model.MenuDataItem;
import net.bluehack.kiosk.model.SideMenu;
import net.bluehack.kiosk.store.StoreItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class ApiClient {
    private static final String TAG = makeLogTag(ApiClient.class);
    private static ApiClient ourInstance = new ApiClient();
    private final ApiClientFactory factory;
    private final KioskAPIClient client;

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

    public void menuGet(final String query, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Menu output = null;

                output = client.menuGet(query);
                Log.e("menuGet:", String.valueOf(output));

                RecentCardAdapter recentCardAdapter = new RecentCardAdapter();
                recentCardAdapter.notifyDataSetChanged();
                listener.onResponse(output);

                return null;
            }

        }.execute();
    }
}
