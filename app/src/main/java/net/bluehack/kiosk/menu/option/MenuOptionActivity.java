package net.bluehack.kiosk.menu.option;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;

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

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.payment.PaymentActivity;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.swagger.client.ApiClient;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.AddOrderReqDetail;
import io.swagger.client.model.AddOrderReqOption;
import io.swagger.client.model.AddOrderRes;
import io.swagger.client.model.MenuOptionReq;
import io.swagger.client.model.MenuOptionRes;
import io.swagger.client.model.MenuOptionResOptions;
import io.swagger.client.model.MenuResData;

public class MenuOptionActivity extends BaseActivity {

    private static final String TAG = makeLogTag(MenuOptionActivity.class);
    private Context context;
    private MenuOptionAdapter menuOptionAdapter;
    private RecyclerView.LayoutManager menuOptionlayoutManager;
    private RecyclerView menu_option_rv;
    private ImageView menu_option_cart;
    private CircleImageView menu_option_iv_img;
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
    private TextView menu_cart_tv;
    private LinearLayout menu_option_ll_info_title;
    private LinearLayout menu_option_ll_info_content;
    private static int menu_option_count_sum = 1;
    private static boolean isVisibleMenuInfo = false;
    private static List<CartMenuOptionItem> optionList = null;
    private static List<CartMenuRequireOptionItem> requiredOptionList = null;
    private static CartMenuItem cartMenuItem = null;

    private String menu_id;
    private String menu_name;
    private Number menu_price;
    private Number menu_point;
    private String menu_image;
    private String menu_description;
    private Number menu_calory;

    private LinearLayout progress_ll;
    private ImageView progress_iv;
    private List<AddOrderReqDetail> orderList = null;

    public MenuOptionActivity() {
    }

    public void staticInit(){

        if (optionList != null) {
            optionList.clear();
            optionList = null;
            optionList = new ArrayList<>();
        } else {
            optionList = new ArrayList<>();
        }

        if (requiredOptionList != null) {
            requiredOptionList.clear();
            requiredOptionList = null;
            requiredOptionList = new ArrayList<>();
        } else {
            requiredOptionList = new ArrayList<>();
        }

        if (cartMenuItem != null) {
            cartMenuItem = null;
            cartMenuItem = new CartMenuItem();
        } else {
            cartMenuItem = new CartMenuItem();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option);

        context = this;

        staticInit();
        menu_option_rv = (RecyclerView) findViewById(R.id.menu_option_rv);
        menu_option_cart = (ImageView) findViewById(R.id.menu_option_cart);
        menu_option_iv_img = (CircleImageView) findViewById(R.id.menu_option_iv_img);
        menu_option_info_btn = (ImageView) findViewById(R.id.menu_option_info_btn);
        menu_option_ib_count_minus = (ImageView) findViewById(R.id.menu_option_ib_count_minus);
        menu_option_ib_count_plus = (ImageView) findViewById(R.id.menu_option_ib_count_plus);
        menu_option_btn_cart = (ImageView) findViewById(R.id.menu_option_btn_cart);
        menu_option_btn_payment = (ImageView) findViewById(R.id.menu_option_btn_payment);
        menu_option_tv_count = (TextView) findViewById(R.id.menu_option_tv_count);
        menu_option_tv_title = (TextView) findViewById(R.id.menu_option_tv_title);
        menu_option_tv_price = (TextView) findViewById(R.id.menu_option_tv_price);
        menu_option_tv_info = (TextView) findViewById(R.id.menu_option_tv_info);
        menu_option_tv_calory = (TextView) findViewById(R.id.menu_option_tv_calory);
        menu_cart_tv = (TextView) findViewById(R.id.menu_cart_tv);
        menu_option_ll_info_title = (LinearLayout) findViewById(R.id.menu_option_ll_info_title);
        menu_option_ll_info_content = (LinearLayout) findViewById(R.id.menu_option_ll_info_content);

        menuOptionlayoutManager = new LinearLayoutManager(this);
        menuOptionAdapter = new MenuOptionAdapter(context);
        menu_option_rv.setAdapter(menuOptionAdapter);
        menu_option_rv.setLayoutManager(menuOptionlayoutManager);

        Intent intent = getIntent();
        String menu_info = intent.getExtras().getString("menu_info");
        final Gson gson = new Gson();
        MenuResData menuResDataItem = gson.fromJson(menu_info, MenuResData.class);

        menu_id = menuResDataItem.getMenuId();
        menu_name = menuResDataItem.getMItem();
        menu_price = menuResDataItem.getPrice();
        menu_point = menuResDataItem.getPoints();
        menu_image = menuResDataItem.getFileId();
        menu_description = menuResDataItem.getDescription();
        menu_calory = menuResDataItem.getCalory();

        intent.removeExtra("menu_info");

        MenuOptionReq menuOptionReq = new MenuOptionReq();
        menuOptionReq.setMenuId(menu_id);

        String store_id = "";
        if (KioskPreference.getInstance().getStoreInfo() != null) {
            store_id = KioskPreference.getInstance().getStoreInfo().getStoreId();
        }
        menuOptionReq.setStoreId(store_id);

        final String url = UiUtil.getFileImgUrl();
        Glide.with(context)
                .load(url + menu_image)
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_c_default)
                .into(menu_option_iv_img);

        menu_option_tv_title.setText(menu_name);
        menu_option_tv_price.setText("$ " + String.valueOf(menu_price));
        menu_option_tv_calory.setText(String.valueOf(menu_calory) + " kcal");
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
                if (menu_option_count_sum <= 1) {
                    menu_option_count_sum = 1;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                } else {
                    menu_option_count_sum--;
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
                } else {
                    menu_option_count_sum++;
                    menu_option_tv_count.setText(String.valueOf(menu_option_count_sum));
                }
            }
        });

        getMenuOptionlist(context, menuOptionReq);

        //add cart
        menu_option_btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CartMenuItem cartMenuItem = makeMenuInfo();
                //set menu info preference
                if (menu_option_count_sum == 1) {
                    KioskPreference.getInstance().setCartInfo(cartMenuItem);
                } else {
                    for (int i = 0; i < menu_option_count_sum; i ++) {
                        KioskPreference.getInstance().setCartInfo(cartMenuItem);
                    }
                }
                Toast.makeText(context, context.getResources().getString(R.string.add_cart), Toast.LENGTH_SHORT).show();
                menu_cart_tv.setVisibility(View.VISIBLE);

                LogEventTracker.TapAddCartEvent(GaCategory.OPTION, cartMenuItem);
            }
        });

        menu_option_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start cartActivity
                LogEventTracker.CartBtnEvent(GaCategory.OPTION);
                Intent intent = new Intent(MenuOptionActivity.this, CartMenuActivity.class);
                startActivity(intent);
            }
        });

        menu_option_btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<CartMenuItem> selectMenuList = new ArrayList<CartMenuItem>();
                CartMenuItem cartMenuItem = makeMenuInfo();
                LOGE(TAG, "cartmenuItem => " + UiUtil.toStringGson(cartMenuItem));

                if (menu_option_count_sum == 1) {
                    selectMenuList.add(cartMenuItem);

                } else {
                    for (int i = 0; i < menu_option_count_sum; i ++) {
                        selectMenuList.add(cartMenuItem);
                    }

                }

                LogEventTracker.OrderBtnEvent(GaCategory.OPTION, selectMenuList);
                Intent intent = new Intent(MenuOptionActivity.this, PaymentActivity.class);
                intent.putExtra("selectMenuList", selectMenuList);
                startActivity(intent);
            }
        });
    }

    private void getMenuOptionlist(final Context context, final MenuOptionReq menuOptionReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().menuDetailPost(context, menuOptionReq, new ApiClient.ApiResponseListener() {
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

                                    if (menuOptionRes.getResponseStatus() != null && menuOptionRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        menuOptionAdapter.clean();

                                        final ArrayList<MenuOptionResOptions> menuOptionList = new ArrayList<MenuOptionResOptions>();  //required option name list

                                        for (int i = 0; i < menuOptionRes.getData().size(); i++) {
                                            for (int j = 0; j < menuOptionRes.getData().get(i).getOptions().size(); j++) {

                                                menuOptionList.add(menuOptionRes.getData().get(i).getOptions().get(j));
                                            }
                                        }

                                        menuOptionAdapter.addItem(menuOptionList);
                                        menuOptionAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.menu_option_not_found_menu), Toast.LENGTH_SHORT).show();
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

    private CartMenuItem makeMenuInfo() {

        //make cartMenuItem
        cartMenuItem = new CartMenuItem();
        cartMenuItem.setMenu_id(menu_id);
        cartMenuItem.setMenu_name(menu_name);
        cartMenuItem.setMenu_image(menu_image);
        cartMenuItem.setMenu_price(String.valueOf(menu_price));
        cartMenuItem.setMenu_point(String.valueOf(menu_point));
        cartMenuItem.setMenu_calory(String.valueOf(menu_calory));
        cartMenuItem.setMenu_description(menu_description);

        //get checked menu option list
        List<CartMenuOptionItem> isCheckedOptionList = new ArrayList<>();
        for (int i = 0; i < optionList.size(); i ++) {
            if (optionList.get(i).isChecked()) {
                isCheckedOptionList.add(optionList.get(i));
            }
        }
        cartMenuItem.setCartMenuOptionItems(isCheckedOptionList);

        //get checked menu required option list
        List<CartMenuRequireOptionItem> isCheckedRequiredOptionList = new ArrayList<>();
        if (isCheckedRequiredOptionList != null) {
            isCheckedRequiredOptionList.clear();
        }
        for (int i = 0; i < requiredOptionList.size(); i ++) {
            if (requiredOptionList.get(i).isChecked()) {
                isCheckedRequiredOptionList.add(requiredOptionList.get(i));
            }
        }
        cartMenuItem.setCartMenuRequireOptionItems(isCheckedRequiredOptionList);

        return cartMenuItem;
    }

    public List<CartMenuOptionItem> getMenuOptionList() {
        return optionList;
    }

    public void setMenuOptionList(CartMenuOptionItem optionItems) {
        if (optionItems.isChecked()) {
            optionList.add(optionItems);
        } else {
            for (int i = 0; i < optionList.size(); i ++) {
                if (optionList.get(i).getMenu_option_id().equals(optionItems.getMenu_option_id())) {
                    optionList.remove(i);
                    LOGE(TAG, "optionList =>" + UiUtil.toStringGson(optionList));
                }
            }

        }
    }

    public void clearMenuOptionListIndex() {
        if (optionList != null) {
            optionList.clear();
        }
    }

    public List<CartMenuRequireOptionItem> getMenuRequiredOptionMap() {
        return requiredOptionList;
    }

    public void setMenuRequiredOptionMap(CartMenuRequireOptionItem requiredItems) {
        if (requiredItems.isChecked()) {
            requiredOptionList.add(requiredItems);
        } else {
            for (int i = 0; i < requiredOptionList.size(); i ++) {
                if (requiredOptionList.get(i).getMenu_rq_option_id().equals(requiredItems.getMenu_rq_option_id())) {
                    requiredOptionList.remove(i);
                    LOGE(TAG, "requiredOptionList =>" + UiUtil.toStringGson(requiredOptionList));
                }
            }
        }
    }

    public void clearRequiredOptionMap() {
        if (requiredOptionList != null) {
            requiredOptionList.clear();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (optionList != null) {
            optionList.clear();
        }

        if (requiredOptionList != null) {
            requiredOptionList.clear();
        }

        if (cartMenuItem != null) {
            cartMenuItem = null;
        }

        menu_option_count_sum = 1;

        menuOptionAdapter.clean();
        menuOptionAdapter.notifyDataSetChanged();
    }
}
