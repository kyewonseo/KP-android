package net.bluehack.kiosk.api;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import net.bluehack.kiosk.KioskAPIClient;
import net.bluehack.kiosk.model.Menu;

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

                listener.onResponse(output);

                return null;
            }

        }.execute();
    }
}
