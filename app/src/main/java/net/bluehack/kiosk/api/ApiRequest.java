package net.bluehack.kiosk.api;

import android.support.v4.util.Pair;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public abstract class ApiRequest {

    private static final String TAG = makeLogTag(ApiRequest.class);

    private static String API_URL = "https://maps.googleapis.com/maps/api/place/search/json";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    protected static Response get(ArrayList<Pair<String, String>> params) throws IOException {
        String url = API_URL;

        if (params != null && params.size() > 0) {
            url += '?';

            int lastIndex = params.size()-1;
            int iteratorIndex = 0;

            for (Pair<String, String> param : params) {
                if (iteratorIndex == lastIndex) {
                    url += param.first + '=' + param.second;
                } else {
                    url += param.first + '=' + param.second + '&';
                    iteratorIndex ++;
                }
            }
        }

        Log.d(TAG, "[ApiRequest] : " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }
}
