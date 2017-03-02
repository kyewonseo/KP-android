package net.bluehack.kiosk.cart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CartMenuActivity extends Activity {

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

    private static CartMenuItem cartMenuItem = null;
    private static ArrayList<CartMenuItem> getCartMenuList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_menu);

        context = this;

        //get preference menu info
        getCartMenuList = KioskPreference.getInstance().getCartInfo();
        getCartMenuList = KioskPreference.getInstance().getCartInfo();
        Gson gson = new Gson();
        LOGE(TAG, "getCartMenuList: " + gson.toJson(getCartMenuList));

        cart_menu_rv_list           = (RecyclerView) findViewById(R.id.cart_menu_rv_list);
        cart_empty_layout           = (View) findViewById(R.id.cart_empty_layout);
        cart_menu_layout            = (View) findViewById(R.id.cart_menu_layout);
        cart_menu_ll_bottom         = (LinearLayout) findViewById(R.id.cart_menu_ll_bottom);
        cart_menu_tv_point          = (TextView) findViewById(R.id.cart_menu_tv_point);
        cart_menu_tv_payment_price  = (TextView) findViewById(R.id.cart_menu_tv_payment_price);
        cart_menu_tv_payment_count  = (TextView) findViewById(R.id.cart_menu_tv_payment_count);

        if (getCartMenuList != null && getCartMenuList.size() != 0) {
            //view content_cart_menu
            cart_menu_layout.setVisibility(View.VISIBLE);
            cart_menu_ll_bottom.setVisibility(View.VISIBLE);
            cart_empty_layout.setVisibility(View.GONE);


            layoutManager       = new LinearLayoutManager(this);
            cartMenuAdapter  = new CartMenuAdapter(context);

            cart_menu_rv_list.setHasFixedSize(true);
            cart_menu_rv_list.setLayoutManager(layoutManager);
            cart_menu_rv_list.setAdapter(cartMenuAdapter);

            cartMenuAdapter.clean();
            cartMenuAdapter.addItem(getCartMenuList);
            cartMenuAdapter.notifyDataSetChanged();

            double pointSum = 0.0;
            double priceSum = 0.0;
            int orderCountSum = 1;

            for (int i = 0; i < getCartMenuList.size(); i ++) {
                pointSum = pointSum + Double.valueOf(getCartMenuList.get(i).getMenu_point());
                priceSum = priceSum + Double.valueOf(getCartMenuList.get(i).getMenu_price());
                orderCountSum = orderCountSum + i;
            }

            LOGE(TAG, "pointSum :" + pointSum);
            LOGE(TAG, "orderCountSum :" + orderCountSum);
            LOGE(TAG, "priceSum :" + priceSum);
            cart_menu_tv_point.setText(String.valueOf(pointSum) + "P");
            cart_menu_tv_payment_price.setText(String.valueOf(priceSum) + "$");
            cart_menu_tv_payment_count.setText("Count :" + String.valueOf(orderCountSum));

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
}
