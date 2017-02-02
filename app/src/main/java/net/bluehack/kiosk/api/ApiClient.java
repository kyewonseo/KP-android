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

import static com.google.android.gms.internal.zzs.TAG;
import static net.bluehack.kiosk.api.ApiRequest.get;
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

    public ArrayList<StoreItem> googlePlaceStoreGet(Location locat, String radi, String types) throws IOException {

        ArrayList<StoreItem> info = null;
        final String LOCATION = "location";
        final String RADIUS = "radius";
        final String SENSOR = "sensor";
        final String TYPES = "types";
        final String KEY = "key";

        try{
            GetMethodParam params = new GetMethodParam.Builder()
                    .addParam(LOCATION, locat.getLatitude() + "," + locat.getLongitude())
                    .addParam(RADIUS, radi)
                    .addParam(SENSOR, "false")
                    .addParam(TYPES, types)
                    .addParam(KEY, "AIzaSyA7av6NBZ3U-CwHzYMPex2M96OWP1zQz4Y")
                    .build();

            Response response = get(params.getParams());
            if (response.isSuccessful()) {
                Log.d(TAG, "response Suceess !");

                String data = response.body().toString();

                Log.e(TAG, "response =" + data);

                ArrayList<StoreItem> storeList = new ArrayList<StoreItem>();
                JSONObject jsonStore;
                JSONObject jsonObject = new JSONObject(data);
                JSONArray resultArray = jsonObject.getJSONArray("results");

                JSONObject nameJson = resultArray.getJSONObject(0);

                LOGE(TAG, "nameJson : " + nameJson.toString());
                /*for (int i = 0; i < jsonArray.length(); i ++) {
                    jsonStore = jsonArray.getJSONObject(i);
                    storeList.add((StoreItem) jsonStore.get("name"));
                    storeList.add((StoreItem) jsonStore.get("address"));
                    storeList.add((StoreItem) jsonStore.get("latitude"));
                    storeList.add((StoreItem) jsonStore.get("longitude"));
                }*/

                info = storeList;
//                Gson gson = new Gson();
//                Type type = new TypeToken<ArrayList<StoreItem>>() {}.getType();
//                info = gson.fromJson(data, type);

                response.body().close();
            } else {
                Log.e(TAG, "response code =" + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
}
