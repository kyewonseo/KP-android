package net.bluehack.kiosk.home.ticket;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.home.DishDetailAdapter;
import net.bluehack.kiosk.home.HomeActivity;
import net.bluehack.kiosk.home.model.Ticket;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.payment.PaymentActivity;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiClient;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.AddOrderReqDetail;
import io.swagger.client.model.AddOrderReqOption;
import io.swagger.client.model.AddOrderRes;
import io.swagger.client.model.OrderResData;

public class TicketActivity extends BaseActivity {

    private static final String TAG = makeLogTag(TicketActivity.class);

    Context context;
    // status
    FrameLayout flStatus;
    TextView tvOrderNo;
    LinearLayout lvTicket;
    GridView gvMenu;

    ImageView ivPayment;
    TextView tvPayment;
    ImageView ivReceive;
    TextView tvReceive;
    ImageView ivMaking;
    TextView tvMaking;
    ImageView ivComplete;
    TextView tvComplete;

    TextView tvTitle;
    ImageView ivCart;
    TextView tvDate;
    TextView tvStore;
    TextView tvPoint;
    TextView tvMethodPayment;
    TextView tvTotal;
    TextView tv_reorder;
    TextView menu_cart_tv;

    ListView lvMenu;
    DishDetailAdapter adapter;

    TextView tv_impossible_reorder;
    FrameLayout fl_sold_out;

    TextView ticket_tv_subtotal_price;
    TextView ticket_tv_tax_price;
    TextView ticket_tv_usepoint_price;
    View viewStateLineToInCook;
    View viewStateLineToReady;


    private ArrayList<CartMenuItem> reorderMenuList = new ArrayList<>();

    private LinearLayout progress_ll;
    private ImageView progress_iv;
    private boolean isCheckedGoHome = false;

    int userGetPoints = 0;
    //TODO: FIXME =>testcode: OrderResData의 response 파라미터에 tax를 추가해야함
    double totalTax = 0.00;
    double totalPrice = 0.00;
    double subTotal = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_ticket);

        flStatus = (FrameLayout) findViewById(R.id.flStatus);

        ivPayment = (ImageView) findViewById(R.id.ivPayment);
        tvPayment = (TextView) findViewById(R.id.tvPayment);

        ivReceive = (ImageView) findViewById(R.id.ivReceive);
        tvReceive = (TextView) findViewById(R.id.tvReceive);

        ivMaking = (ImageView) findViewById(R.id.ivMaking);
        tvMaking = (TextView) findViewById(R.id.tvMaking);

        ivComplete = (ImageView) findViewById(R.id.ivComplete);
        tvComplete = (TextView) findViewById(R.id.tvComplete);


        tvOrderNo = (TextView) findViewById(R.id.tvOrderNo);
        lvTicket = (LinearLayout) findViewById(R.id.lvTicket);
        gvMenu = (GridView) findViewById(R.id.gvMenu);
        gvMenu.setVisibility(View.GONE);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivCart = (ImageView) findViewById(R.id.ivCart);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvStore = (TextView) findViewById(R.id.tvStore);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvMethodPayment = (TextView) findViewById(R.id.tvMethodPayment);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tv_reorder = (TextView) findViewById(R.id.tv_reorder);
        menu_cart_tv = (TextView) findViewById(R.id.menu_cart_tv);

        lvMenu = (ListView) findViewById(R.id.lvDishDetail);
        adapter = new DishDetailAdapter();
        lvMenu.setAdapter(adapter);
        lvMenu.setSelector(R.drawable.listview_non_selector);

        progress_ll = (LinearLayout) findViewById(R.id.progress_ll);
        progress_iv = (ImageView) findViewById(R.id.progress_iv);

        tv_impossible_reorder = (TextView) findViewById(R.id.tv_impossible_reorder);
        fl_sold_out = (FrameLayout) findViewById(R.id.fl_sold_out);

        ticket_tv_subtotal_price = (TextView) findViewById(R.id.ticket_tv_subtotal_price);
        ticket_tv_tax_price = (TextView) findViewById(R.id.ticket_tv_tax_price);
        ticket_tv_usepoint_price = (TextView) findViewById(R.id.ticket_tv_usepoint_price);

        viewStateLineToInCook = (View) findViewById(R.id.viewStateLineToInCook);
        viewStateLineToReady = (View) findViewById(R.id.viewStateLineToReady);

        final Intent intent = getIntent();

        if (intent.getExtras() != null) {

            fl_sold_out.setVisibility(View.GONE);
            tv_impossible_reorder.setVisibility(View.GONE);
            tv_reorder.setVisibility(View.VISIBLE);

            String impossible_order = "false";
            if (intent.getExtras().getString("impossible_order") != null) {

                impossible_order = intent.getExtras().getString("impossible_order");

                if (impossible_order.equals("true")) {
                    fl_sold_out.setVisibility(View.VISIBLE);
                    tv_reorder.setVisibility(View.GONE);
                    tv_impossible_reorder.setVisibility(View.VISIBLE);
                }
            }

            if (intent.getExtras().getString("isCheckedGoHome") != null) {

                if (intent.getExtras().getString("isCheckedGoHome").equals("true")) {
                    isCheckedGoHome = true;
                }
            }

            String order_info = intent.getExtras().getString("clicked_order");
            //LOGD(TAG, UiUtil.toStringGson(order_info));

            Gson gson = new Gson();
            OrderResData orderItem = gson.fromJson(order_info, OrderResData.class);
            LOGD(TAG, UiUtil.toStringGson(orderItem));

            if (orderItem != null) {

                if (orderItem.getAddPoints() != null) {
                    userGetPoints = orderItem.getAddPoints();
                }

                // update UI
                updateTicket(orderItem);

                //TODO : fixme => orderItem 중에 서버에서 받아오는 데이터 필드가 부족하여 추가해야함(ex. 특히 option_type)
                for (int i = 0; i < orderItem.getDetail().size(); i ++) {
                    CartMenuItem cartMenuItem = new CartMenuItem();
                    List<CartMenuOptionItem> cartMenuOptionlist = new ArrayList<>();

                    cartMenuItem.setMenu_id(orderItem.getDetail().get(i).getDetailMenuId());
                    cartMenuItem.setMenu_image(orderItem.getDetail().get(i).getDetailFileId());
                    cartMenuItem.setMenu_name(orderItem.getDetail().get(i).getDetailMenuName());
                    cartMenuItem.setMenu_price(String.valueOf(orderItem.getDetail().get(i).getDetailPrice()));

                    for (int j = 0; j < orderItem.getDetail().get(i).getOption().size(); j ++) {
                        CartMenuOptionItem cartMenuOptionItem = new CartMenuOptionItem();
                        cartMenuOptionItem.setMenu_option_id(orderItem.getDetail().get(i).getOption().get(j).getOptionMenuId());
                        cartMenuOptionItem.setMenu_option_name(orderItem.getDetail().get(i).getOption().get(j).getOptionMenuName());
                        cartMenuOptionItem.setMenu_option_price(String.valueOf(orderItem.getDetail().get(i).getOption().get(j).getOptionPrice()));

                        cartMenuOptionlist.add(cartMenuOptionItem);
                    }
                    cartMenuItem.setCartMenuOptionItems(cartMenuOptionlist);

                    reorderMenuList.add(cartMenuItem);
                }

            } else {
                // 종료
            }
        }

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.CartBtnEvent(GaCategory.ORDERHISTORYDETAIL);
                Intent intent = new Intent(context, CartMenuActivity.class);
                startActivity(intent);
            }
        });

        tv_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.OrderBtnEvent(GaCategory.ORDERHISTORYDETAIL, reorderMenuList);
                Intent paymentIntent = new Intent(context, PaymentActivity.class);
                paymentIntent.putExtra("reorderMenuList", reorderMenuList);
                startActivity(paymentIntent);
            }
        });
    }

    private void updateTicket(OrderResData ticket) {
        LOGD(TAG, UiUtil.toStringGson(ticket));

        if (ticket == null) {
            return;
        }

        //TODO: fixme => getorderstatus
        int ticketStatus = 0;
        int ticketOrderNum = 0;

        if (ticket.getOrderStatus() != null) {
            ticketStatus = ticket.getOrderStatus();

            ticketStatusTextColorInit();
            switch (ticket.getCookStatus()) {
                case Ticket.PAYMENT:
                    ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    tvPayment.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    break;
                case Ticket.RECEIVE:
                    ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    tvReceive.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    break;
                case Ticket.MAKING:
                    ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    tvMaking.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.color.color_49));
                    viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    break;
                case Ticket.COMPLETE:
                    ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));

                    tvComplete.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    break;

                default:
                    ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    break;
            }
        }

        if (ticket.getOrderNum() != null) {
            ticketOrderNum = ticket.getOrderNum();
        }

        if (ticketStatus == Ticket.EXPIRE) {
            flStatus.setVisibility(View.GONE);
        } else {
            lvTicket.setVisibility(View.INVISIBLE);
            flStatus.setVisibility(View.VISIBLE);

            tvOrderNo.setText(String.valueOf(ticketOrderNum));
        }

        tvTitle.setText(String.valueOf(ticketOrderNum));
        tvDate.setText(ticket.getRegidate());
        tvStore.setText(KioskPreference.getInstance().getStoreInfo().getStore());
        tvPoint.setText("+ " + String.valueOf(userGetPoints) + "P");


        totalPrice = Double.valueOf(String.valueOf(ticket.getPrice()));
        totalPrice = (int) (totalPrice * 100) / 100.0;
        totalTax = Double.valueOf(String.valueOf(ticket.getTax()));
        totalTax = (int) (totalTax * 100) / 100.0;

        BigDecimal dTotalPrice = new BigDecimal(String.valueOf(totalPrice));
        BigDecimal dTotalTax = new BigDecimal(String.valueOf(totalTax));
        BigDecimal dSubTotal = dTotalPrice.subtract(dTotalTax);
        subTotal = Double.parseDouble(String.valueOf(dSubTotal));

        ticket_tv_subtotal_price.setText("$" + String.valueOf(subTotal));
        ticket_tv_tax_price.setText("$" + String.valueOf(totalTax));

        if (ticket.getPoints() == null || ticket.getPoints() == 0) {
            ticket_tv_usepoint_price.setText(String.valueOf(ticket.getPoints()) + "P");
        } else {
            ticket_tv_usepoint_price.setText("-" + String.valueOf(ticket.getPoints()) + "P");
        }

        String paymethod = getResources().getString(R.string.payment_methods_card);
        if (ticket.getPaymethod() != null) {
            if (ticket.getPaymethod().equals("0,1,0")) {
                paymethod = getResources().getString(R.string.payment_methods_card);
            } else if (ticket.getPaymethod().equals("0,0,1")) {
                paymethod = getResources().getString(R.string.payment_methods_point);
            } else if (ticket.getPaymethod().equals("0,1,1")) {
                paymethod = getResources().getString(R.string.payment_methods_card_and_point);
            }


        }
        tvMethodPayment.setText(paymethod);
        tvTotal.setText("$" + String.valueOf(ticket.getPrice()));

        adapter.addList(ticket.getDetail());

        UiUtil.drawListViewFitHeight(lvMenu, adapter);
    }

    private void ticketStatusTextColorInit() {
        tvPayment.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        tvReceive.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        tvMaking.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        tvComplete.setTextColor(ContextCompat.getColor(context, R.color.color_34));

        viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.color.color_49));
        viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.color.color_49));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (KioskPreference.getInstance().getCartInfo() != null) {

            if (KioskPreference.getInstance().getCartInfo().size() != 0) {
                menu_cart_tv.setVisibility(View.VISIBLE);
            } else {
                menu_cart_tv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (reorderMenuList != null) {
            reorderMenuList.clear();
        }

        if (isCheckedGoHome) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
