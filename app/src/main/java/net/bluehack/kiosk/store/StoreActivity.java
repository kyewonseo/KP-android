package net.bluehack.kiosk.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.model.StoresRes;
import net.bluehack.kiosk.model.StoresResDataItem;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;


public class StoreActivity extends Activity {

    private static final String TAG = makeLogTag(StoreActivity.class);
    private Context context;
    private StoreAdapter storeAdapter;
    private RecyclerView storeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String accountId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.bluehack.kiosk.R.layout.activity_store);

        context = this;

        storeRecyclerView = (RecyclerView) findViewById(R.id.store_rv_list);

        layoutManager = new LinearLayoutManager(this);
        storeAdapter = new StoreAdapter(context);

        storeRecyclerView.setHasFixedSize(true);
        storeRecyclerView.setLayoutManager(layoutManager);
        storeRecyclerView.setAdapter(storeAdapter);

        if (KioskPreference.getInstance().getAccountId() != null) {

            accountId = KioskPreference.getInstance().getAccountId();
            getStoreList(accountId);

        }else {
            //error
            //move login activity
            Toast.makeText(context, "can't get accountId ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getStoreList(final String accountId) {
        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().storesAccountIdGet(accountId, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StoresRes storesRes = (StoresRes) result;

                                    if (storesRes.getResponseStatus() != null && storesRes.getResponseStatus().equals("200")) {

                                        LOGD(TAG, "login success!");

                                        storeAdapter.clean();

                                        ArrayList<StoresResDataItem> storeItems = new ArrayList<>();

                                        for (StoresResDataItem item : storesRes.getData()) {

                                            StoresResDataItem storeDataItem = new StoresResDataItem();
                                            storeDataItem.setSLogo(item.getSLogo());
                                            storeDataItem.setStore(item.getStore());
                                            storeDataItem.setSAddress(item.getSAddress());

                                            storeItems.add(storeDataItem);
                                        }
                                        storeAdapter.addItem(storeItems);
                                        storeAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), "can't get store list.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(),"check network status!",Toast.LENGTH_SHORT).show();
        }
    }

}
