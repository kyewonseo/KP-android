package net.bluehack.kiosk.menu;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;

import io.swagger.client.ApiClient;
import io.swagger.client.model.MenuReq;
import io.swagger.client.model.MenuRes;
import io.swagger.client.model.MenuResData;

public class MenuActivity extends BaseActivity {

    private static final String TAG = makeLogTag(MenuActivity.class);
    private Context context;
    private TextView menu_toolbar_name;
    private TextView menu_cart_tv;
    private ImageView menu_cart;
    private MenuAdapter menuAdapter;
    private RecyclerView menuRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = this;

        menuRecyclerView = (RecyclerView) findViewById(R.id.menu_rv_list);
        menu_toolbar_name = (TextView) findViewById(R.id.menu_toolbar_name);
        menu_cart_tv = (TextView) findViewById(R.id.menu_cart_tv);
        menu_cart = (ImageView) findViewById(R.id.menu_cart);

        layoutManager = new LinearLayoutManager(this);
        menuAdapter = new MenuAdapter(context);

        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setLayoutManager(layoutManager);
        menuRecyclerView.setAdapter(menuAdapter);

        Intent intent = getIntent();
        String sub_category_id = intent.getExtras().getString("sub_category_id");
        String sub_category_name = intent.getExtras().getString("sub_category_name");
        intent.removeExtra("sub_category_id");
        intent.removeExtra("sub_category_name");
        LOGE(TAG, "sub_category_id : " + sub_category_id);
        LOGE(TAG, "sub_category_name: " + sub_category_name);

        menu_toolbar_name.setText(sub_category_name);

        String store_id = "";
        if (KioskPreference.getInstance().getStoreInfo() != null) {
            store_id = KioskPreference.getInstance().getStoreInfo().getStoreId();
        }

        MenuReq menuReq = new MenuReq();
        menuReq.setStoreId(store_id);
        menuReq.setSubCategoryId(sub_category_id);
        getMenuList(context, menuReq);

        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.CartBtnEvent(GaCategory.MENU);
                Intent cartIntent = new Intent(context, CartMenuActivity.class);
                startActivity(cartIntent);
            }
        });
    }

    private void getMenuList(final Context context, final MenuReq menuReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().menuListPost(context, menuReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MenuRes menuRes = (MenuRes) result;

                                    Gson gson = new Gson();
                                    LOGE(TAG, "menuRes result : " + gson.toJson(result));

                                    if (menuRes.getResponseStatus() != null && menuRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        menuAdapter.clean();

                                        ArrayList<MenuResData> menuResDataItems = new ArrayList<>();

                                        for (MenuResData item : menuRes.getData()) {

                                            MenuResData menuResDataItem = new MenuResData();
                                            menuResDataItem.setMenuId(item.getMenuId());
                                            menuResDataItem.setSubCategoryId(item.getSubCategoryId());
                                            menuResDataItem.setBarcode(item.getBarcode());
                                            menuResDataItem.setCalory(item.getCalory());
                                            menuResDataItem.setDescription(item.getDescription());
                                            menuResDataItem.setFileId(item.getFileId());
                                            menuResDataItem.setMItem(item.getMItem());
                                            menuResDataItem.setMRegidate(item.getMRegidate());
                                            menuResDataItem.setMType(item.getMType());
                                            menuResDataItem.setPoints(item.getPoints());
                                            menuResDataItem.setPrice(item.getPrice());
                                            menuResDataItem.setUseYN(item.getUseYN());
                                            menuResDataItem.setCalory(item.getCalory());

                                            menuResDataItems.add(menuResDataItem);
                                        }
                                        menuAdapter.addItem(menuResDataItems);
                                        menuAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.menu_not_found_list), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (KioskPreference.getInstance().getCartInfo() != null) {

            if (KioskPreference.getInstance().getCartInfo().size() != 0) {
                menu_cart_tv.setVisibility(View.VISIBLE);
            } else {
                menu_cart_tv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
