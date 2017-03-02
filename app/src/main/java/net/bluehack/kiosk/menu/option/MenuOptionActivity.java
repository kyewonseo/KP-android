package net.bluehack.kiosk.menu.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.CartMenuItem;
import net.bluehack.kiosk.cart.CartMenuOptionItem;
import net.bluehack.kiosk.cart.CartMenuRequireOptionItem;
import net.bluehack.kiosk.main.MainActivity;
import net.bluehack.kiosk.menu.MenuActivity;
import net.bluehack.kiosk.model.MenuOptionRes;
import net.bluehack.kiosk.model.MenuOptionResDataItem;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuOptionActivity extends Activity {

    private static final String TAG = makeLogTag(MenuActivity.class);
    private Context context;
    private MenuOption1Adapter menuOption1Adapter;
    private MenuOption2Adapter menuOption2Adapter;
    private RecyclerView.LayoutManager menuOption1layoutManager;
    private RecyclerView.LayoutManager menuOption2layoutManager;
    private RecyclerView menu_option1_rv_list;
    private RecyclerView menu_option2_rv_list;
    private ImageView menu_option_cart;
    private ImageView menu_option_search;
    private ImageView menu_option_iv_img;
    private ImageView menu_option_info_btn;
    private ImageView menu_option_ib_count_minus;
    private ImageView menu_option_ib_count_plus;
    private ImageView menu_option_btn_cart;
    private ImageView menu_option_btn_payment;
    private TextView menu_option_tv_count;
    private TextView menu_option_tv_title;
    private TextView menu_option_tv_price;
    private TextView menu_option_tv_info;
    private TextView menu_option_tv_calory;
    private LinearLayout menu_option_ll_info_title;
    private LinearLayout menu_option_ll_info_content;
    private String menu_id;
    private String menu_name;
    private String menu_price;
    private String menu_point;
    private String menu_image;
    private String menu_description;
    private String menu_calory;
    private static int menu_option_count_sum = 0;
    private static boolean isVisibleMenuInfo = false;
    private static HashMap<Integer, ArrayList<CartMenuOptionItem>> map = new HashMap<Integer, ArrayList<CartMenuOptionItem>>();
    private static HashMap<Integer, ArrayList<CartMenuRequireOptionItem>> requiredMap = new HashMap<Integer, ArrayList<CartMenuRequireOptionItem>>();

    public MenuOptionActivity (){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option);

        context = this;

        menu_option1_rv_list            = (RecyclerView) findViewById(R.id.menu_option1_rv_list);
        menu_option2_rv_list            = (RecyclerView) findViewById(R.id.menu_option2_rv_list);
        menu_option_cart                = (ImageView) findViewById(R.id.menu_option_cart);
        menu_option_search              = (ImageView) findViewById(R.id.menu_option_search);
        menu_option_iv_img              = (ImageView) findViewById(R.id.menu_option_iv_img);
        menu_option_info_btn            = (ImageView) findViewById(R.id.menu_option_info_btn);
        menu_option_ib_count_minus      = (ImageView) findViewById(R.id.menu_option_ib_count_minus);
        menu_option_ib_count_plus       = (ImageView) findViewById(R.id.menu_option_ib_count_plus);
        menu_option_btn_cart            = (ImageView) findViewById(R.id.menu_option_btn_cart);
        menu_option_btn_payment         = (ImageView) findViewById(R.id.menu_option_btn_payment);
        menu_option_tv_count            = (TextView) findViewById(R.id.menu_option_tv_count);
        menu_option_tv_title            = (TextView) findViewById(R.id.menu_option_tv_title);
        menu_option_tv_price            = (TextView) findViewById(R.id.menu_option_tv_price);
        menu_option_tv_info             = (TextView) findViewById(R.id.menu_option_tv_info);
        menu_option_tv_calory           = (TextView) findViewById(R.id.menu_option_tv_calory);
        menu_option_ll_info_title       = (LinearLayout) findViewById(R.id.menu_option_ll_info_title);
        menu_option_ll_info_content     = (LinearLayout) findViewById(R.id.menu_option_ll_info_content);

        menuOption1layoutManager       = new LinearLayoutManager(this);
        menuOption2layoutManager       = new LinearLayoutManager(this);
        menuOption1Adapter  = new MenuOption1Adapter(context);
        menuOption2Adapter  = new MenuOption2Adapter(context);

        menu_option1_rv_list.setHasFixedSize(true);
        menu_option1_rv_list.setLayoutManager(menuOption1layoutManager);
        menu_option1_rv_list.setAdapter(menuOption1Adapter);
        menu_option2_rv_list.setHasFixedSize(true);
        menu_option2_rv_list.setLayoutManager(menuOption2layoutManager);
        menu_option2_rv_list.setAdapter(menuOption2Adapter);


        Intent intent = getIntent();
        menu_id             = intent.getExtras().getString("menu_id");
        menu_name           = intent.getExtras().getString("menu_name");
        menu_price          = intent.getExtras().getString("menu_price");
        menu_point          = intent.getExtras().getString("menu_point");
        menu_image          = intent.getExtras().getString("menu_image");
        menu_description    = intent.getExtras().getString("menu_description");
        menu_calory         = intent.getExtras().getString("menu_calory");

        intent.removeExtra("menu_id");
        intent.removeExtra("menu_name");
        intent.removeExtra("menu_price");
        intent.removeExtra("menu_point");
        intent.removeExtra("menu_image");
        intent.removeExtra("menu_description");
        intent.removeExtra("menu_calory");

        Glide.with(context)
                .load(menu_image)
                .override(80,80)
                .placeholder(R.drawable.img_storelogo_default)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .into(menu_option_iv_img);

        menu_option_tv_title.setText(menu_name);
        menu_option_tv_price.setText(menu_price + "$" + "(" + menu_point + "P" + ")");
        menu_option_tv_calory.setText(menu_calory + " Kcal");
        menu_option_tv_info.setText(menu_description);
        menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));

        menu_option_ll_info_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisibleMenuInfo) {
                    menu_option_ll_info_content.setVisibility(View.VISIBLE);
                    menu_option_info_btn.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_arrow_up_nor));
                    isVisibleMenuInfo = true;
                } else {
                    menu_option_ll_info_content.setVisibility(View.GONE);
                    menu_option_info_btn.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_arrow_down_nor));
                    isVisibleMenuInfo = false;
                }
            }
        });
        LOGD(TAG, "menu_id : " + menu_id);
        LOGD(TAG, "menu_name: " + menu_name);
        LOGD(TAG, "menu_image: " + menu_image);


        menu_option_ib_count_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu_option_count_sum <= 0 ) {
                    menu_option_count_sum = 0;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                }else {
                    menu_option_count_sum --;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                }
            }
        });
        menu_option_ib_count_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu_option_count_sum >= 99) {
                    menu_option_count_sum = 99;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                }else {
                    menu_option_count_sum ++;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                }
            }
        });

        getMenuOptionlist(menu_id);

        menu_option_btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set cartMenuItem
                Gson gson = new Gson();
                CartMenuItem cartMenuItem = new CartMenuItem();
                cartMenuItem.setMenu_id(menu_id);
                cartMenuItem.setMenu_name(menu_name);
                cartMenuItem.setMenu_image(menu_image);
                cartMenuItem.setMenu_price(menu_price);
                cartMenuItem.setMenu_point(menu_point);
                cartMenuItem.setMenu_calory(menu_calory);
                cartMenuItem.setMenu_description(menu_description);

                //get checked menu option list
                List<CartMenuOptionItem> cartMenuOptionItemList = new ArrayList<CartMenuOptionItem>();
                HashMap<Integer, ArrayList<CartMenuOptionItem>> hashMap = getMenuOptionMap();
                Iterator<Integer> iter = hashMap.keySet().iterator();
                while(iter.hasNext()) {
                    int key = iter.next();
                    ArrayList<CartMenuOptionItem> valueList = hashMap.get(key);
                    LOGD(TAG, "CartMenuOptionItem valueList => " + valueList.get(key).toString());
                    cartMenuOptionItemList.add(valueList.get(key));
                }
                cartMenuItem.setCartMenuOptionItems(cartMenuOptionItemList);

                //get checked menu required option list
                List<CartMenuRequireOptionItem> cartMenuRequireOptionItemList = new ArrayList<CartMenuRequireOptionItem>();
                HashMap<Integer, ArrayList<CartMenuRequireOptionItem>> requiredHashMap = getMenuRequiredOptionMap();
                Iterator<Integer> requiredIter = requiredHashMap.keySet().iterator();
                while(requiredIter.hasNext()) {
                    int key = requiredIter.next();
                    ArrayList<CartMenuRequireOptionItem> valueList = requiredHashMap.get(key);
                    LOGD(TAG, "CartMenuRequireOptionItem valueList => " + valueList.get(key).toString());
                    cartMenuRequireOptionItemList.add(valueList.get(key));
                }
                cartMenuItem.setCartMenuRequireOptionItems(cartMenuRequireOptionItemList);

                //set menu info preference
                String cartObject = gson.toJson(cartMenuItem);
                KioskPreference.getInstance().setCartInfo(cartObject);

                Toast.makeText(context, "add cart", Toast.LENGTH_SHORT).show();
                //start cartActivity
                //Intent intent = new Intent(MenuOptionActivity.this, CartMenuActivity.class);
                //startActivity(intent);
                //finish();
            }
        });

        menu_option_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start cartActivity
                Intent intent = new Intent(MenuOptionActivity.this, CartMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMenuOptionlist(String menu_id) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().menuMenuOptionMenuIdGet(menu_id, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MenuOptionRes menuOptionRes = (MenuOptionRes) result;

                                    Gson gson = new Gson();
                                    LOGE(TAG, "menuOptionRes result : " + gson.toJson(result));

                                    if (menuOptionRes.getResponseStatus() != null && menuOptionRes.getResponseStatus().equals("200")) {

                                        LOGD(TAG, "login success!");

                                        final ArrayList<MenuOptionResDataItem> menuOption1ResDataItems = new ArrayList<>();
                                        final ArrayList<MenuOptionResDataItem> menuOption2ResDataItems = new ArrayList<>();

                                        menuOption1Adapter.clean();
                                        menuOption2Adapter.clean();

                                        MenuOptionResDataItem menuOption1ResDataItem = new MenuOptionResDataItem();
                                        MenuOptionResDataItem menuOption2ResDataItem = new MenuOptionResDataItem();

                                        if (menuOption1ResDataItems.size() != 0) {
                                            menuOption1ResDataItems.clear();
                                        }
                                        if (menuOption2ResDataItems.size() != 0) {
                                            menuOption2ResDataItems.clear();
                                        }
                                        //TODO: fixme => menu option에 관련된 타입이 fixed 될때 변경
                                        for (int i = 0; i < menuOptionRes.getData().size(); i ++) {
                                            //TODO: TEST VERSION
                                            menuOption2ResDataItem.setMenuId(menuOptionRes.getData().get(i).getMenuId());
                                            menuOption2ResDataItem.setMItem(menuOptionRes.getData().get(i).getMItem());
                                            menuOption2ResDataItem.setPrice(menuOptionRes.getData().get(i).getPrice());
                                            menuOption2ResDataItems.add(menuOption2ResDataItem);
                                            /*if (menuOptionRes.getData().get(i).getMType().equals("0")) {
                                                //type 1
                                                menuOption1ResDataItem.setMenuId(menuOptionRes.getData().get(i).getMenuId());
                                                menuOption1ResDataItem.setMItem(menuOptionRes.getData().get(i).getMItem());
                                                menuOption1ResDataItem.setPrice(menuOptionRes.getData().get(i).getPrice());
                                                menuOption1ResDataItems.add(menuOption1ResDataItem);

                                            } else if (menuOptionRes.getData().get(i).getMType().equals("1")) {
                                                //type 2
                                                menuOption2ResDataItem.setMenuId(menuOptionRes.getData().get(i).getMenuId());
                                                menuOption2ResDataItem.setMItem(menuOptionRes.getData().get(i).getMItem());
                                                menuOption2ResDataItem.setPrice(menuOptionRes.getData().get(i).getPrice());
                                                menuOption2ResDataItems.add(menuOption2ResDataItem);
                                            }*/
                                        }

                                        menuOption1Adapter.addItem(menuOption1ResDataItems);
                                        menuOption1Adapter.notifyDataSetChanged();
                                        menuOption2Adapter.addItem(menuOption2ResDataItems);
                                        menuOption2Adapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), "can't get menu option list.", Toast.LENGTH_SHORT).show();
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

    public HashMap<Integer, ArrayList<CartMenuOptionItem>> getMenuOptionMap() {
        return map;
    }
    public void setMenuOptionMap(HashMap<Integer, ArrayList<CartMenuOptionItem>> map) {
        this.map = map;
    }

    public HashMap<Integer, ArrayList<CartMenuRequireOptionItem>> getMenuRequiredOptionMap() {
        LOGE(TAG, "getMenuRequiredOptionMap() =>" + requiredMap);
        return requiredMap;
    }
    public void setMenuRequiredOptionMap(HashMap<Integer, ArrayList<CartMenuRequireOptionItem>> requiredMap) {
        LOGE(TAG, "setMenuRequiredOptionMap() =>" + requiredMap);
        this.requiredMap = requiredMap;
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
