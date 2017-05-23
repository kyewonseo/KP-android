package net.bluehack.kiosk.home;


import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.contact.ContactActivity;
import net.bluehack.kiosk.history.HistoryActivity;
import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.myinfo.MyInfoActivity;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.store.StoreActivity;
import net.bluehack.kiosk.subcategory.SubCategoryActivity;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.swagger.client.ApiClient;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;
import io.swagger.client.model.OrderResData;
import io.swagger.client.model.StoresResData;

public class HomeActivity extends BaseActivity {

    private static final String TAG = makeLogTag(HomeActivity.class);
    private Context context;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout drawer_header_iv_close;
    private CircleImageView ivStoreLogo;
    private TextView tvStoreName;
    private TextView tvStoreAddress;
    private TextView tvGiftPoint;
    private ListView lvRecentlyOrder;
    private LinearLayout llHomeRecentlyOrder;
    private LinearLayout llHomeMobilePayment;
    private TicketAdapter ticketAdapter;
    private TextView menu_cart_tv;
    private LinearLayout nav_item_01;
    private LinearLayout nav_item_02;
    private LinearLayout nav_item_03;
    private LinearLayout nav_item_04;
    private LinearLayout nav_item_05;
    private TextView tvOtherView;
    private String storeName;

    public HomeActivity(){}

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.btn_main_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawer_header_iv_close = (FrameLayout) findViewById(R.id.drawer_header_iv_close);
        menu_cart_tv = (TextView) findViewById(R.id.menu_cart_tv);
        nav_item_01 = (LinearLayout) findViewById(R.id.nav_item_01);
        nav_item_02 = (LinearLayout) findViewById(R.id.nav_item_02);
        nav_item_03 = (LinearLayout) findViewById(R.id.nav_item_03);
        nav_item_04 = (LinearLayout) findViewById(R.id.nav_item_04);
        nav_item_05 = (LinearLayout) findViewById(R.id.nav_item_05);

        drawer_header_iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        nav_item_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.SlideMyInfoBtnEvent(GaCategory.SLIDEMENU);
                Intent myinfoIntent = new Intent(context, MyInfoActivity.class);
                myinfoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myinfoIntent);
            }
        });

        nav_item_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.SlideStoreBtnEvent(GaCategory.SLIDEMENU);
                Intent storeIntent = new Intent(context, StoreActivity.class);
                storeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(storeIntent);
            }
        });

        nav_item_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.SlideOrderHistoryBtnEvent(GaCategory.SLIDEMENU);
                Intent historyIntent = new Intent(context, HistoryActivity.class);
                historyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(historyIntent);
            }
        });

        nav_item_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.CartBtnEvent(GaCategory.SLIDEMENU);
                Intent cartIntent = new Intent(context, CartMenuActivity.class);
                cartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cartIntent);
            }
        });

        nav_item_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.SlideContactusBtnEvent(GaCategory.SLIDEMENU);
                Intent contactIntent = new Intent(context, ContactActivity.class);
                contactIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(contactIntent);
            }
        });

        tvOtherView = (TextView) findViewById(R.id.tvOtherView);
        tvOtherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.ViewAllBtnEvent(GaCategory.HOME);
                Intent i = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

        TextView tvMobileOther = (TextView) findViewById(R.id.tvMobileOrder);
        tvMobileOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.MoblieOrderBtnEvent(GaCategory.HOME);
                Intent i = new Intent(HomeActivity.this, SubCategoryActivity.class);
                startActivity(i);
                //startActivityForResult(i, 0);
            }
        });

        // Store UI 연결
        ivStoreLogo = (CircleImageView) findViewById(R.id.ivStoreLogo);
        tvStoreName = (TextView) findViewById(R.id.tvStoreName);
        tvStoreAddress = (TextView) findViewById(R.id.tvStoreAddress);
        tvGiftPoint = (TextView) findViewById(R.id.tvGiftPoint);

        llHomeRecentlyOrder = (LinearLayout) findViewById(R.id.llHomeRecentlyOrder);
        llHomeMobilePayment = (LinearLayout) findViewById(R.id.llHomeMobilePayment);

        // Recently order
        lvRecentlyOrder = (ListView) findViewById(R.id.lvRecentlyOrder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvRecentlyOrder.setNestedScrollingEnabled(false);
        }

        ticketAdapter = new TicketAdapter();
        lvRecentlyOrder.setAdapter(ticketAdapter);

        updateStoreUI();

        ImageView ivChangeStore = (ImageView) findViewById(R.id.ivChangeStore);
        ivChangeStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.ChangeStoreEvent(GaCategory.HOME, storeName);
                Intent intent = new Intent(context, StoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getRecentOrderlist(final Context context, final OrderReq orderReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().orderPaymentListPost(context, orderReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OrderRes orderRes = (OrderRes) result;

                                    Gson gson = new Gson();
                                    LOGD(TAG, "orderPaymentListPost result : " + gson.toJson(result));

                                    if (orderRes.getResponseStatus() != null && orderRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        ticketAdapter.clean();

                                        List<OrderResData> orderResDataList = new ArrayList<OrderResData>();
                                        for (int i = 0; i < orderRes.getData().size(); i++) {
                                            orderResDataList.add(orderRes.getData().get(i));
                                        }
                                        //ticketAdapter.add(orderResDataList);
                                        ticketAdapter.addItem(orderResDataList, tvOtherView);
                                        ticketAdapter.notifyDataSetChanged();

                                    } else {
                                        //Toast.makeText(ApplicationLoader.getContext(), "can't get menu option list.", Toast.LENGTH_SHORT).show();
                                    }

                                    updateRecentlyOrder();
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


    private void updateStoreUI() {
        // store info
        final StoresResData storeInfo = KioskPreference.getInstance().getStoreInfo();

        String storeImg = storeInfo.getSLogo();
        storeName = storeInfo.getStore();
        String storeAddr = storeInfo.getSAddress();
        int storePoint = 0;
        if (storeInfo.getUPoints() != null) {
            storePoint = storeInfo.getUPoints();
        }

        // logo image, name, address
        final String url = UiUtil.getFileImgUrl();

        Glide.with(context)
                .load(url + storeImg)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_sample_03)
                .into(ivStoreLogo);

        tvStoreName.setText(storeName);
        tvStoreAddress.setText(storeAddr);
        tvGiftPoint.setText(String.valueOf(storePoint));
    }

    private void updateRecentlyOrder() {
        if (ticketAdapter == null || ticketAdapter.getCount() <= 0) {
            // 빈화면 처리
            llHomeRecentlyOrder.setVisibility(View.INVISIBLE);
            llHomeMobilePayment.setVisibility(View.VISIBLE);
            tvOtherView.setVisibility(View.INVISIBLE);
        } else {
            // 최대 3개까지 표현
            llHomeRecentlyOrder.setVisibility(View.VISIBLE);
            llHomeMobilePayment.setVisibility(View.INVISIBLE);

            ticketAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                LogEventTracker.NavMenuBtnEvent(GaCategory.HOME);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String storeId = "";
        String userId = "";
        if (KioskPreference.getInstance().getStoreInfo() != null) {
            storeId = KioskPreference.getInstance().getStoreInfo().getStoreId();
            userId = KioskPreference.getInstance().getStoreInfo().getUserId();
        }

        if (KioskPreference.getInstance().getCartInfo() != null) {

            if (KioskPreference.getInstance().getCartInfo().size() != 0) {
                menu_cart_tv.setVisibility(View.VISIBLE);
            } else {
                menu_cart_tv.setVisibility(View.INVISIBLE);
            }
        }

        OrderReq orderReq = new OrderReq();
        orderReq.setStoreId(storeId);
        orderReq.setUserId(userId);
        orderReq.setStartNo(0);
        orderReq.setCnt(3);
        orderReq.setOrderBy("createDate");
        orderReq.setOrderByWith("desc");
        getRecentOrderlist(context, orderReq);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return false;
            } else {
                final KioskPopup popup = new KioskPopup(context);
                popup.show();
                TextView tvTitle = (TextView) popup.getContentView().findViewById(R.id.tvTitle);
                ImageView ivImage = (ImageView) popup.getContentView().findViewById(R.id.ivImage);
                TextView tvMessage = (TextView) popup.getContentView().findViewById(R.id.tvMessage);
                TextView tvSubMessage
                        = (TextView) popup.getContentView().findViewById(R.id.tvSubMessage);
                final TextView tvLeftButton
                        = (TextView) popup.getContentView().findViewById(R.id.tvLeftButton);
                final TextView tvRightButton
                        = (TextView) popup.getContentView().findViewById(R.id.tvRightButton);

                ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_exit));
                tvLeftButton.setText(context.getResources().getString(R.string.popup_quit_left_btn));
                tvRightButton.setText(context.getResources().getString(R.string.popup_quit_right_btn));
                tvTitle.setText(context.getResources().getString(R.string.popup_quit_title));
                tvMessage.setText(context.getResources().getString(R.string.popup_quit_message));

                String appName = "Digital Checkouts";
                String sub = context.getResources().getString(R.string.popup_quit_sub);
                String subMessage = appName + sub;
                Spannable sp = new SpannableString(subMessage);
                sp.setSpan(new ForegroundColorSpan(Color.RED), 0, appName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSubMessage.setText(sp);

                LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                        context.getResources().getString(R.string.popup_quit_title));
                tvLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_quit_title),
                                tvLeftButton.getText().toString());
                        popup.dismiss();
                    }
                });
                tvRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_quit_title),
                                tvRightButton.getText().toString());
                        popup.dismiss();
                        finish();
                    }
                });
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
