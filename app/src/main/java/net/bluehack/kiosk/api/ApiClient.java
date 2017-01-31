package net.bluehack.kiosk.api;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.Gson;

import net.bluehack.kiosk.KioskAPIClient;
import net.bluehack.kiosk.main.RecentCardAdapter;
import net.bluehack.kiosk.main.RecentItem;
import net.bluehack.kiosk.model.Menu;
import net.bluehack.kiosk.model.MenuDataItem;
import net.bluehack.kiosk.model.SideMenu;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzs.TAG;
import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;

public class ApiClient {
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

    /*public void menuGet(final String query, final ApiResponseListener listener) {
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
    }*/


    public void menuGet(final String query) {

        //final ArrayList<MenuDataItem> menuItems = new ArrayList<MenuDataItem>();

        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {

                Menu output = null;
                output = client.menuGet(query);
                Log.e("menuGet:", String.valueOf(output));

                return output;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                Menu menu = (Menu) result;
                ArrayList<MenuDataItem> menuItems = new ArrayList<>();
                RecentItem recentItem = new RecentItem();

                if (menu.getSuccess()) {
                    Gson gson = new Gson();
                    LOGD(TAG, "응답 " + gson.toJson(menu.getData()));

                    for (MenuDataItem item : menu.getData()) {

                        MenuDataItem menuDataItem = new MenuDataItem();
                        menuDataItem.setName(item.getName());
                        menuDataItem.setPrice(item.getPrice());

                        menuItems.add(menuDataItem);
                    }

                    RecentCardAdapter recentCardAdapter = new RecentCardAdapter();
                    recentCardAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}
