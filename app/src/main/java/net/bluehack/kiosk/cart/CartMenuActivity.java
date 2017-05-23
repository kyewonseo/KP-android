package net.bluehack.kiosk.cart;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.payment.PaymentActivity;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiClient;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.AddOrderReqDetail;
import io.swagger.client.model.AddOrderReqOption;
import io.swagger.client.model.AddOrderRes;

public class CartMenuActivity extends BaseActivity {

    private static final String TAG = makeLogTag(CartMenuActivity.class);
    private Context context;
    private CartMenuAdapter cartMenuAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cart_menu_rv_list;
    private TextView cart_menu_tv_point;
    private TextView cart_menu_tv_payment_count;
    private TextView cart_menu_tv_payment_price;
    private ImageView cart_menu_btn_payment;
    private View cart_empty_layout;
    private View cart_menu_layout;
    private LinearLayout cart_menu_ll_bottom;

    private LinearLayout progress_ll;
    private ImageView progress_iv;
    private ArrayList<CartMenuItem> getCartMenuList = null;
    private double total = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_menu);

        context = this;

        //get preference menu info
        if (KioskPreference.getInstance().getCartInfo() != null) {
            getCartMenuList = new ArrayList<>();
            getCartMenuList = KioskPreference.getInstance().getCartInfo();
        }

        Gson gson = new Gson();
        LOGE(TAG, "getCartMenuList: " + gson.toJson(getCartMenuList));

        cart_menu_rv_list = (RecyclerView) findViewById(R.id.cart_menu_rv_list);
        cart_empty_layout = (View) findViewById(R.id.cart_empty_layout);
        cart_menu_layout = (View) findViewById(R.id.cart_menu_layout);
        cart_menu_ll_bottom = (LinearLayout) findViewById(R.id.cart_menu_ll_bottom);
        cart_menu_tv_point = (TextView) findViewById(R.id.cart_menu_tv_point);
        cart_menu_tv_payment_price = (TextView) findViewById(R.id.cart_menu_tv_payment_price);
        cart_menu_tv_payment_count = (TextView) findViewById(R.id.cart_menu_tv_payment_count);

        cart_menu_btn_payment = (ImageView) findViewById(R.id.cart_menu_btn_payment);

        cart_menu_btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.OrderBtnEvent(GaCategory.CART, getCartMenuList);
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("cartMenuList", getCartMenuList);
                startActivity(intent);
            }
        });

        if (getCartMenuList != null && getCartMenuList.size() != 0) {
            //view content_cart_menu
            cart_menu_layout.setVisibility(View.VISIBLE);
            cart_menu_layout.setBackground(UiUtil.getDrawable(context, R.color.color_04));
            cart_menu_ll_bottom.setVisibility(View.VISIBLE);
            cart_empty_layout.setVisibility(View.GONE);

            layoutManager = new LinearLayoutManager(this);
            cartMenuAdapter = new CartMenuAdapter(context);

            cart_menu_rv_list.setHasFixedSize(true);
            //cart_menu_rv_list.setNestedScrollingEnabled(false);
            cart_menu_rv_list.setLayoutManager(layoutManager);
            cart_menu_rv_list.setAdapter(cartMenuAdapter);

            cartMenuAdapter.clean();
            cartMenuAdapter.addItem(getCartMenuList);
            cartMenuAdapter.notifyDataSetChanged();

            int orderCountSum = 0;
            int pointSum = 0;
            double priceSum = 0.0;
            double optionPriceSum = 0;

            for (int i = 0; i < getCartMenuList.size(); i++) {

                //TODO: fixme => order menu에서 cart를 누를경우 현재 포인트정보가 없음 test code
                if (getCartMenuList.get(i).getMenu_point() == null || getCartMenuList.get(i).getMenu_point().equals("null")) {
                    pointSum = 0;
                } else {
                    pointSum = pointSum + Integer.valueOf(getCartMenuList.get(i).getMenu_point());
                }
                if (getCartMenuList.get(i).getMenu_price() == null || getCartMenuList.get(i).getMenu_price().equals("null")) {
                    priceSum = 0.0;
                } else {
                    priceSum = priceSum + Double.valueOf(getCartMenuList.get(i).getMenu_price());
                }
                orderCountSum = i;

                if (getCartMenuList.get(i).getCartMenuOptionItems() != null) {
                    for (int j = 0; j < getCartMenuList.get(i).getCartMenuOptionItems().size(); j ++) {
                        if (getCartMenuList.get(i).getCartMenuOptionItems().get(j).getMenu_option_price() == null) {
                            optionPriceSum = 0.0;
                        }else {
                            optionPriceSum = optionPriceSum + Double.valueOf(getCartMenuList.get(i).getCartMenuOptionItems().get(j).getMenu_option_price());
                        }
                    }
                }

                if (getCartMenuList.get(i).getCartMenuRequireOptionItems() != null) {
                    for (int j = 0; j < getCartMenuList.get(i).getCartMenuRequireOptionItems().size(); j ++) {
                        if (getCartMenuList.get(i).getCartMenuRequireOptionItems().get(j).getMenu_rq_option_price() == null) {
                            optionPriceSum = 0.0;
                        }else {
                            optionPriceSum = optionPriceSum + Double.valueOf(getCartMenuList.get(i).getCartMenuRequireOptionItems().get(j).getMenu_rq_option_price());
                        }
                    }
                }

            }

            orderCountSum = orderCountSum + 1;
            LOGE(TAG, "pointSum :" + pointSum);
            LOGE(TAG, "orderCountSum :" + orderCountSum);
            LOGE(TAG, "priceSum :" + priceSum + optionPriceSum);

            //total
            total = priceSum + optionPriceSum;
            total = (int) (total * 100) / 100.0;

            //TODO: pointSum이 "null"로 오는거에 대한 처리

            cart_menu_tv_point.setText(String.valueOf(pointSum) + "P");
            cart_menu_tv_payment_price.setText("$ "+ String.valueOf(total));
            //cart_menu_tv_payment_count.setText("Count :" + String.valueOf(orderCountSum));
            cart_menu_tv_payment_count.setText("Total");

        } else {
            //view content_cart_empty
            cart_empty_layout.setVisibility(View.VISIBLE);
            cart_menu_layout.setVisibility(View.GONE);
            cart_menu_ll_bottom.setVisibility(View.GONE);
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

    public void clearCartList() {
        if (getCartMenuList != null) {
            getCartMenuList.clear();
        }
    }
}
