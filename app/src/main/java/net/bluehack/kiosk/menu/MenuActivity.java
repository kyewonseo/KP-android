package net.bluehack.kiosk.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.model.MenuRes;
import net.bluehack.kiosk.model.MenuResDataItem;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuActivity extends Activity {

    private static final String TAG = makeLogTag(MenuActivity.class);
    private Context context;
    private TextView menu_toolbar_name;
    private ImageView menu_cart;
    private ImageView menu_search;
    private MenuAdapter menuAdapter;
    private RecyclerView menuRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String sub_category_id = null;
    private String sub_category_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = this;

        menuRecyclerView    = (RecyclerView) findViewById(R.id.menu_rv_list);
        menu_toolbar_name   = (TextView) findViewById(R.id.menu_toolbar_name);
        menu_cart           = (ImageView) findViewById(R.id.menu_cart);
        menu_search         = (ImageView) findViewById(R.id.menu_search);

        layoutManager       = new LinearLayoutManager(this);
        menuAdapter  = new MenuAdapter(context);

        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setLayoutManager(layoutManager);
        menuRecyclerView.setAdapter(menuAdapter);

        Intent intent = getIntent();
        sub_category_id = intent.getExtras().getString("sub_category_id");
        sub_category_name = intent.getExtras().getString("sub_category_name");
        LOGE(TAG, "sub_category_id : " + sub_category_id);
        LOGE(TAG, "sub_category_name: " + sub_category_name);

        menu_toolbar_name.setText(sub_category_name);

        getMenuList(sub_category_id);
    }

    private void getMenuList(final String sub_category_id) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().menuSubCategoryIdGet(sub_category_id, new ApiClient.ApiResponseListener() {
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

                                    if (menuRes.getResponseStatus() != null && menuRes.getResponseStatus().equals("200")) {

                                        LOGD(TAG, "login success!");

                                        menuAdapter.clean();

                                        ArrayList<MenuResDataItem> menuResDataItems = new ArrayList<>();

                                        for (MenuResDataItem item : menuRes.getData()) {

                                            MenuResDataItem menuResDataItem = new MenuResDataItem();
                                            menuResDataItem.setMenuId(item.getMenuId());
                                            menuResDataItem.setMItem(item.getMItem());
                                            menuResDataItem.setPrice(item.getPrice());
                                            menuResDataItem.setPoints(item.getPoints());
                                            menuResDataItem.setDescription(item.getDescription());
                                            menuResDataItem.setCalory(item.getCalory());
                                            menuResDataItem.setServer(item.getServer());
                                            menuResDataItem.setVolume(item.getVolume());
                                            menuResDataItem.setPath(item.getPath());
                                            menuResDataItem.setName(item.getName());

                                            menuResDataItems.add(menuResDataItem);
                                        }
                                        menuAdapter.addItem(menuResDataItems);
                                        menuAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), "can't get menu list.", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
