package net.bluehack.kiosk.store;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;

import io.swagger.client.ApiClient;
import io.swagger.client.model.StoresReq;
import io.swagger.client.model.StoresRes;
import io.swagger.client.model.StoresResData;


public class StoreActivity extends BaseActivity {

    private static final String TAG = makeLogTag(StoreActivity.class);
    private Context context;
    private StoreAdapter storeAdapter;
    private RecyclerView storeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private KioskPopup popup;
    private boolean ischeckedPopup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.bluehack.kiosk.R.layout.activity_store);

        context = this;
        popup = new KioskPopup(context);

        storeRecyclerView = (RecyclerView) findViewById(R.id.store_rv_list);

        layoutManager = new LinearLayoutManager(this);
        storeAdapter = new StoreAdapter(context);

        storeRecyclerView.setHasFixedSize(true);
        storeRecyclerView.setLayoutManager(layoutManager);
        storeRecyclerView.setAdapter(storeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ischeckedPopup) {
            popup.dismiss();
        }
        getStoreList(context, createStoresReq());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private StoresReq createStoresReq() {

        StoresReq storesReq = new StoresReq();

        if (KioskPreference.getInstance().getLoginInfo() != null) {
            String accountId = KioskPreference.getInstance().getLoginInfo().getAccountId();
            storesReq.setAccountId(accountId);
        }
        return storesReq;
    }

    private void getStoreList(final Context context, final StoresReq storesReq) {
        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().storesAccountListPost(context, storesReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StoresRes storesRes = (StoresRes) result;

                                    if (storesRes.getResponseStatus() != null && storesRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        storeAdapter.clean();

                                        ArrayList<StoresResData> storeItems = new ArrayList<>();

                                        for (StoresResData item : storesRes.getData()) {

                                            StoresResData storeDataItem = new StoresResData();
                                            storeDataItem.setSLogo(item.getSLogo());
                                            storeDataItem.setStore(item.getStore());
                                            storeDataItem.setSAddress(item.getSAddress());
                                            storeDataItem.setStoreId(item.getStoreId());
                                            storeDataItem.setUserId(item.getUserId());
                                            storeDataItem.setSTax(item.getSTax());
                                            storeDataItem.setSPhone(item.getSPhone());

                                            storeItems.add(storeDataItem);
                                        }
                                        storeAdapter.addItem(storeItems);
                                        storeAdapter.notifyDataSetChanged();

                                    } else {
                                        //Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.store_not_found_message), Toast.LENGTH_SHORT).show();

                                        popup.show();
                                        ischeckedPopup = true;
                                        TextView tvTitle = (TextView) popup.getContentView().findViewById(R.id.tvTitle);
                                        ImageView ivImage = (ImageView) popup.getContentView().findViewById(R.id.ivImage);
                                        TextView tvMessage = (TextView) popup.getContentView().findViewById(R.id.tvMessage);
                                        TextView tvSubMessage
                                                = (TextView) popup.getContentView().findViewById(R.id.tvSubMessage);
                                        final TextView tvLeftButton
                                                = (TextView) popup.getContentView().findViewById(R.id.tvLeftButton);
                                        final TextView tvRightButton
                                                = (TextView) popup.getContentView().findViewById(R.id.tvRightButton);

                                        ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_store_failed));
                                        tvTitle.setText(context.getResources().getString(R.string.popup_store_error_title));
                                        tvMessage.setText(context.getResources().getString(R.string.popup_store_error_message));
                                        tvSubMessage.setText(context.getResources().getString(R.string.popup_store_error_sub));
                                        tvRightButton.setText(context.getResources().getString(R.string.popup_store_error_right_btn));
                                        LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                                context.getResources().getString(R.string.popup_store_error_title));

                                        tvLeftButton.setVisibility(View.GONE);
                                        tvRightButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                                        context.getResources().getString(R.string.popup_store_error_title),
                                                        tvRightButton.getText().toString());
                                                popup.dismiss();
                                                ischeckedPopup = false;
                                                getStoreList(context, createStoresReq());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(),getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
        }
    }
}
