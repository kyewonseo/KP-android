package net.bluehack.kiosk.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.subcategory.SubCategoryActivity;
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
import io.swagger.client.model.AddOrderResData;
import io.swagger.client.model.AddOrderResDataSoldOut;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;
import io.swagger.client.model.OrderResData;
import io.swagger.client.model.StoresResData;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class PaymentActivity extends BaseActivity {

    private static final String TAG = makeLogTag(PaymentActivity.class);
    private Context context;
    private RecyclerView payment_menu_rv_list;
    private ImageView payment_btn_payment;
    private CircleImageView payment_store_iv_img;
    private TextView payment_store_tv_name;
    private TextView payment_store_tv_address;
    private EditText payment_usepoint_et;
    private TextView payment_details_tv_subtotal_price;
    private TextView payment_details_tv_usepoint_price;
    private TextView payment_details_tv_tax_price;
    private TextView payment_detatils_tv_total_count;
    private TextView payment_details_tv_total_price;
    private RadioButton payment_rb_card;
    private CheckBox payment_cb_usepoint;
    private LinearLayout payment_ll_point;
    private LinearLayoutManager layoutManager;
    private PaymentAdapter paymentAdapter;
    private LinearLayout progress_ll;
    private ImageView progress_iv;
    private RelativeLayout payment_rl;
    private String usepointText = "0";
    private ArrayList<CartMenuItem> selectMenuList = new ArrayList<CartMenuItem>();
    private TextView payment_usepoint_underline;
    private InputMethodManager inputMethodManager;

    //TODO: fixme => 계산 정보
    private int orderCountSum = 0;
    private double pointSum = 0.00;
    private double priceSum = 0.00;
    private double optionPriceSum = 0.00;
    private double tax = 0.00;
    private double sub_total = 0.00;
    private double total = 0.00;
    //TODO: fixme => point값 설정.
    private int storePoint = 0;
    private List<AddOrderReqDetail> orderList = null;
    private String userId = "";
    private String storeId = "";
    private String order_id = "";
    private int checkedIntentFlag = 0; // 1 = optionActivity, 2 = cartActivity, 3 = historyDetailActivity

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        context = this;

        payment_menu_rv_list = (RecyclerView) findViewById(R.id.payment_menu_rv_list);
        payment_btn_payment = (ImageView) findViewById(R.id.payment_btn_payment);
        payment_store_iv_img = (CircleImageView) findViewById(R.id.payment_store_iv_img);
        payment_store_tv_name = (TextView) findViewById(R.id.payment_store_tv_name);
        payment_store_tv_address = (TextView) findViewById(R.id.payment_store_tv_address);
        payment_usepoint_et = (EditText) findViewById(R.id.payment_usepoint_et);
        payment_details_tv_subtotal_price = (TextView) findViewById(R.id.payment_details_tv_subtotal_price);
        payment_details_tv_usepoint_price = (TextView) findViewById(R.id.payment_details_tv_usepoint_price);
        payment_details_tv_tax_price = (TextView) findViewById(R.id.payment_details_tv_tax_price);
        payment_detatils_tv_total_count = (TextView) findViewById(R.id.payment_detatils_tv_total_count);
        payment_details_tv_total_price = (TextView) findViewById(R.id.payment_details_tv_total_price);
        payment_rb_card = (RadioButton) findViewById(R.id.payment_rb_card);
        payment_cb_usepoint = (CheckBox) findViewById(R.id.payment_cb_usepoint);
        payment_ll_point = (LinearLayout) findViewById(R.id.payment_ll_point);
        payment_usepoint_underline = (TextView) findViewById(R.id.payment_usepoint_underline);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        payment_usepoint_et.setTextColor(Color.BLACK);
        //TODO: fixme => menu info (cart list or order list)
        Intent intent = getIntent();
        if (intent.getSerializableExtra("selectMenuList") != null) {
            if (selectMenuList != null) {
                selectMenuList.clear();
            }
            selectMenuList = (ArrayList<CartMenuItem>) intent.getSerializableExtra("selectMenuList");
            intent.removeExtra("selectMenuList");

            checkedIntentFlag = 1;
        }

        if (intent.getSerializableExtra("cartMenuList") != null) {
            if (selectMenuList != null) {
                selectMenuList.clear();
            }
            selectMenuList = (ArrayList<CartMenuItem>) intent.getSerializableExtra("cartMenuList");
            intent.removeExtra("cartMenuList");

            checkedIntentFlag = 2;
        }

        if (intent.getSerializableExtra("reorderMenuList") != null) {
            if (selectMenuList != null) {
                selectMenuList.clear();
            }
            selectMenuList = (ArrayList<CartMenuItem>) intent.getSerializableExtra("reorderMenuList");
            intent.removeExtra("reorderMenuList");

            checkedIntentFlag = 3;
        }

        layoutManager = new LinearLayoutManager(this);
        paymentAdapter = new PaymentAdapter(context);

        payment_menu_rv_list.setHasFixedSize(true);
        //payment_menu_rv_list.setNestedScrollingEnabled(false);
        payment_menu_rv_list.setLayoutManager(layoutManager);
        payment_menu_rv_list.setAdapter(paymentAdapter);

        paymentAdapter.clean();
        paymentAdapter.addItem(selectMenuList);
        paymentAdapter.notifyDataSetChanged();

        final StoresResData storeInfo = KioskPreference.getInstance().getStoreInfo();
        userId = storeInfo.getUserId();
        storeId = storeInfo.getStoreId();
        String storeLogo = storeInfo.getSLogo();
        final String storeName = storeInfo.getStore();
        String storeAddr = storeInfo.getSAddress();

        //TODO: TESTCODE
        //storePoint = 10;
        if (storeInfo.getUPoints() != null) {
            storePoint = storeInfo.getUPoints();
        }

        Glide.with(context)
                .load(storeLogo)
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .into(payment_store_iv_img);
        payment_store_tv_name.setText(storeName);
        payment_store_tv_address.setText(storeAddr);


        orderList = new ArrayList<>();
        AddOrderReqOption orderChoiceOptionItem = null;
        AddOrderReqOption orderRequiredOptionItem = null;

        for (int i = 0; i < selectMenuList.size(); i ++) {
            AddOrderReqDetail orderItem = new AddOrderReqDetail();
            List<AddOrderReqOption> orderOptionList = new ArrayList<>();
            String menuId = selectMenuList.get(i).getMenu_id();
            orderItem.setMenuId(menuId);
            orderItem.setQuantity("1");

            if (selectMenuList.get(i).getMenu_point() == null) {
                pointSum = 0.00;
            } else {
                pointSum = pointSum + Double.valueOf(selectMenuList.get(i).getMenu_point());
            }
            if (selectMenuList.get(i).getMenu_price() == null) {
                priceSum = 0.00;
            } else {
                priceSum = priceSum + Double.valueOf(selectMenuList.get(i).getMenu_price());
            }
            orderCountSum = i;

            if (selectMenuList.get(i).getCartMenuOptionItems() != null) {
                for (int c = 0; c < selectMenuList.get(i).getCartMenuOptionItems().size(); c++) {
                    String choiceOptionId = selectMenuList.get(i).getCartMenuOptionItems().get(c).getMenu_option_id();

                    orderChoiceOptionItem = new AddOrderReqOption();
                    orderChoiceOptionItem.setMenuId(choiceOptionId);
                    orderChoiceOptionItem.setQuantity("1");
                    orderOptionList.add(orderChoiceOptionItem);

                    if (selectMenuList.get(i).getCartMenuOptionItems().get(c).getMenu_option_price() == null) {
                        optionPriceSum = 0.00;
                    } else {
                        optionPriceSum = optionPriceSum + Double.valueOf(selectMenuList.get(i).getCartMenuOptionItems().get(c).getMenu_option_price());
                    }
                }


            }

            if (selectMenuList.get(i).getCartMenuRequireOptionItems() != null) {
                for (int r = 0; r < selectMenuList.get(i).getCartMenuRequireOptionItems().size(); r++) {
                    String requiredOptionId = selectMenuList.get(i).getCartMenuRequireOptionItems().get(r).getMenu_rq_option_id();

                    orderRequiredOptionItem = new AddOrderReqOption();
                    orderRequiredOptionItem.setMenuId(requiredOptionId);
                    orderRequiredOptionItem.setQuantity("1");
                    orderOptionList.add(orderRequiredOptionItem);

                    if (selectMenuList.get(i).getCartMenuRequireOptionItems().get(r).getMenu_rq_option_price() == null) {
                        optionPriceSum = 0.00;
                    } else {
                        optionPriceSum = optionPriceSum + Double.valueOf(selectMenuList.get(i).getCartMenuRequireOptionItems().get(r).getMenu_rq_option_price());
                    }
                }

            }

            orderItem.setOption(orderOptionList);
            orderList.add(i, orderItem);
        }
        LOGE(TAG, "orderList => " + UiUtil.toStringGson(orderList));

        //tax
        if (storeInfo.getSTax() != null) {
            Double rate = Double.valueOf(String.valueOf(storeInfo.getSTax()));
            tax = (priceSum + optionPriceSum) * rate;
            tax = (int)(tax * 100) / 100.0;
        }

        //sub total
        sub_total = priceSum + optionPriceSum;
        sub_total = (int) (sub_total * 100) / 100.0;

        //total
        total = sub_total + tax;
        total = (int) (total * 100) / 100.0;

        orderCountSum = orderCountSum + 1;
        LOGD(TAG, "pointSum :" + pointSum);
        LOGD(TAG, "orderCountSum :" + orderCountSum);
        LOGD(TAG, "priceSum :" + priceSum);
        LOGD(TAG, "optionPriceSum :" + optionPriceSum);
        LOGD(TAG, "TAX :" + tax);

        payment_details_tv_subtotal_price.setText("$ " + String.valueOf(sub_total));
        payment_details_tv_usepoint_price.setText("0" + "P");
        payment_details_tv_tax_price.setText("$ " + String.valueOf(tax));
        //payment_detatils_tv_total_count.setText("Count : "+String.valueOf(orderCountSum));
        payment_detatils_tv_total_count.setText("Total");
        payment_details_tv_total_price.setText("$ " + String.valueOf(total));

        payment_usepoint_et.setHint(String.valueOf(storePoint));
        payment_usepoint_et.setRawInputType(Configuration.KEYBOARD_QWERTY);
        payment_cb_usepoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    LogEventTracker.OnUsePointsBtnEvent(GaCategory.PAYMENT);
                    payment_usepoint_et.requestFocus();
                    inputMethodManager.showSoftInput(payment_usepoint_et, InputMethodManager.SHOW_IMPLICIT);
                    payment_ll_point.setVisibility(View.VISIBLE);
                    payment_usepoint_underline.setBackgroundColor(UiUtil.getColor(context, R.color.color_37));
                    payment_usepoint_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            usepointText = s.toString();

                            if (usepointText.equals("") || usepointText == null) {
                                usepointText = "0";
                            }

                            payment_details_tv_usepoint_price.setText(String.valueOf(usepointText) + "P");

                            if (Integer.parseInt(usepointText) > storePoint) {
                                Toast.makeText(context, context.getResources().getString(R.string.payment_point_over),Toast.LENGTH_SHORT).show();
                                s.clear();
                                usepointText = String.valueOf(storePoint);
                                payment_details_tv_usepoint_price.setText(usepointText + "P");
                                if (sub_total - Integer.parseInt(usepointText) < 0) {
                                    payment_details_tv_total_price.setText("$ " + "0");
                                } else {
                                    payment_details_tv_total_price.setText("$ " + String.valueOf(total - Integer.parseInt(usepointText)));
                                }
                            } else {

                                if (sub_total - Integer.parseInt(usepointText) < 0) {
                                    payment_details_tv_total_price.setText("$ " + "0");
                                } else {
                                    payment_details_tv_total_price.setText("$ " + String.valueOf(total - Integer.parseInt(usepointText)));
                                }
                            }

                        }
                    });
                } else {
                    LogEventTracker.OffUsePointsBtnEvent(GaCategory.PAYMENT);
                    payment_ll_point.setVisibility(View.GONE);
                    inputMethodManager.hideSoftInputFromWindow(payment_usepoint_et.getWindowToken(), 0);
                    payment_usepoint_underline.setBackgroundColor(UiUtil.getColor(context, R.color.color_04));
                    payment_usepoint_et.clearComposingText(); // init
                    usepointText = "0";
                    payment_details_tv_total_price.setText("$ " + String.valueOf(total));
                }
            }
        });

        payment_btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결제 모듈
                //create addOrderInfo
                final AddOrderReq makingAddOrderReq = new AddOrderReq();
                makingAddOrderReq.setStoreId(storeId);
                makingAddOrderReq.setUserId(userId);
                makingAddOrderReq.setTax(String.valueOf(tax));
                makingAddOrderReq.setPaymethod("0,1,0");//카드 결제
                makingAddOrderReq.setDetail(orderList);

                //add price & usepoint
                double amount = total - Integer.parseInt(usepointText);
                amount = (int) (amount * 100)/ 100.0;
                String amount_total = String.valueOf(amount);

                makingAddOrderReq.setPrice(amount_total);
                makingAddOrderReq.setPoints(usepointText);

                LogEventTracker.PaymentBtnEvent(GaCategory.PAYMENT, makingAddOrderReq);

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

                tvTitle.setText(getResources().getString(R.string.popup_payment_confirm_title));
                tvMessage.setText(getResources().getString(R.string.popup_payment_confirm_message));

                Spannable sp = new SpannableString(storeName + "?");
                sp.setSpan(new ForegroundColorSpan(Color.RED), 0, storeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSubMessage.setText(sp);

                LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                        context.getResources().getString(R.string.popup_payment_confirm_title));
                tvLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_payment_confirm_title),
                                tvLeftButton.getText().toString());
                        popup.dismiss();
                    }
                });
                tvRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_payment_confirm_title),
                                tvRightButton.getText().toString());
                        popup.dismiss();

                        //progress execute
                        progress_ll = (LinearLayout) findViewById(R.id.progress_ll);
                        progress_iv = (ImageView) findViewById(R.id.progress_iv);
                        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progress_iv);
                        Glide.with(context).load(R.raw.img_loading_gif).into(imageViewTarget);
                        progress_ll.setVisibility(View.VISIBLE);

                        //send order/add API
                        registerOrder(context, popup, makingAddOrderReq);


                    }
                });

                tvRightButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (selectMenuList != null) {
            selectMenuList.clear();
        }
    }

    private void registerOrder(final Context context, final KioskPopup popup, final AddOrderReq addOrderReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().ordersAddPost(context, addOrderReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AddOrderRes addOrderRes = (AddOrderRes) result;

                                    LOGE(TAG, "addOrderReq =>" + UiUtil.toStringGson(addOrderReq));
                                    if (addOrderRes != null && addOrderRes.getResponseStatus().equals(200)
                                            && addOrderRes.getResponseMsg().equals("Success")) {

                                        LOGD(TAG, "registerOrder success!");

                                        //stop progress
                                        progress_ll.setVisibility(View.GONE);

                                        if (addOrderRes.getData() != null && addOrderRes.getData().getOrderId() != null) {
                                            order_id = addOrderRes.getData().getOrderId();
                                        }

                                        checkedStatusPopupRightButton(popup, addOrderReq);

                                    }
                                    //case sold out
                                    else if (addOrderRes != null && addOrderRes.getResponseStatus().equals(200)
                                            && addOrderRes.getResponseMsg().equals("Not exist contents")) { //TODO: 서버 미구현임으로  TEST할때 Success 바꿔서

                                        if (addOrderRes.getData().getSoldOut() != null) {
                                            final List<AddOrderResDataSoldOut> soldOutList = addOrderRes.getData().getSoldOut();

                                            //sold out popup
                                            final KioskPopup soldout_popup = new KioskPopup(context);
                                            soldout_popup.show();
                                            TextView tvTitle = (TextView) soldout_popup.getContentView().findViewById(R.id.tvTitle);
                                            ImageView ivImage = (ImageView) soldout_popup.getContentView().findViewById(R.id.ivImage);
                                            TextView tvMessage = (TextView) soldout_popup.getContentView().findViewById(R.id.tvMessage);
                                            TextView tvSubMessage
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvSubMessage);
                                            final TextView tvLeftButton
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvLeftButton);
                                            final TextView tvRightButton
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvRightButton);
                                            tvRightButton.setVisibility(View.GONE);

                                            ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_sold_out));
                                            tvLeftButton.setText(getResources().getString(R.string.popup_sold_out_left_btn));
                                            tvTitle.setText(getResources().getString(R.string.popup_sold_out_title));

                                            StringBuffer soldOutBuffer = new StringBuffer();
                                            for (AddOrderResDataSoldOut soldOut : soldOutList) {
                                                soldOutBuffer.append(soldOut.getMenuName() + "\n");
                                            }
                                            tvMessage.setText(soldOutBuffer.toString());
                                            tvMessage.setTextColor(Color.RED);

                                            LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                                    context.getResources().getString(R.string.popup_sold_out_title));
                                            tvLeftButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                                            context.getResources().getString(R.string.popup_sold_out_title),
                                                            tvLeftButton.getText().toString());
                                                    popup.dismiss();
                                                    soldout_popup.dismiss();

                                                    //Delete fail menu item
                                                    for(int i = 0; i < soldOutList.size(); i ++) {
                                                        for (int j = 0; j < selectMenuList.size(); j ++) {

                                                            if (selectMenuList.get(j).getMenu_id().equals(soldOutList.get(i).getMenuId())) {
                                                                selectMenuList.remove(j);
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    checkedSoldoutType(selectMenuList);
                                                }
                                            });
                                        } else {
                                            //TODO: TEST CODE
                                            /**
                                             * option activity (dessert)
                                             * menu_id => 6cf9a37b-fde9-4832-8532-2e134cb68c40, menu_name => COOKIE
                                             *
                                             * history detail activity (2017-04-28 12:02:24)
                                             * menu_id => 6072c7ce-c0e8-4bca-8992-c4d6240d0cd2, menu_name => Classic Reuben
                                             *
                                             * cart activity (fruit)
                                             * menu_id => 0243d5f6-3e81-48fc-819c-6dabd72555cc, menu_name => ORANGE
                                             */

                                            AddOrderResDataSoldOut testSoldOut = new AddOrderResDataSoldOut();
                                            testSoldOut.setMenuId("6cf9a37b-fde9-4832-8532-2e134cb68c40");
                                            testSoldOut.setMenuName("COOKIE");

                                            List<AddOrderResDataSoldOut> testSoldOutList = new ArrayList<AddOrderResDataSoldOut>();
                                            testSoldOutList.add(testSoldOut);

                                            AddOrderResData testOrderData = new AddOrderResData();
                                            testOrderData.setSoldOut(testSoldOutList);

                                            addOrderRes.setData(testOrderData);
                                            final List<AddOrderResDataSoldOut> soldOutList = addOrderRes.getData().getSoldOut();

                                            //sold out popup
                                            final KioskPopup soldout_popup = new KioskPopup(context);
                                            soldout_popup.show();
                                            TextView tvTitle = (TextView) soldout_popup.getContentView().findViewById(R.id.tvTitle);
                                            ImageView ivImage = (ImageView) soldout_popup.getContentView().findViewById(R.id.ivImage);
                                            TextView tvMessage = (TextView) soldout_popup.getContentView().findViewById(R.id.tvMessage);
                                            TextView tvSubMessage
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvSubMessage);
                                            final TextView tvLeftButton
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvLeftButton);
                                            final TextView tvRightButton
                                                    = (TextView) soldout_popup.getContentView().findViewById(R.id.tvRightButton);
                                            tvRightButton.setVisibility(View.GONE);

                                            ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_sold_out));
                                            tvLeftButton.setText(getResources().getString(R.string.popup_sold_out_left_btn));
                                            tvTitle.setText(getResources().getString(R.string.popup_sold_out_title));

                                            StringBuffer soldOutBuffer = new StringBuffer();
                                            for (AddOrderResDataSoldOut soldOut : soldOutList) {
                                                soldOutBuffer.append(soldOut.getMenuName() + "\n");
                                            }
                                            tvMessage.setText(soldOutBuffer.toString());
                                            tvMessage.setTextColor(Color.RED);

                                            LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                                    context.getResources().getString(R.string.popup_sold_out_title));
                                            tvLeftButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                                            context.getResources().getString(R.string.popup_sold_out_title),
                                                            tvLeftButton.getText().toString());
                                                    popup.dismiss();

                                                    //Delete fail menu item
                                                    for(int i = 0; i < soldOutList.size(); i ++) {
                                                        for (int j = 0; j < selectMenuList.size(); j ++) {

                                                            if (selectMenuList.get(j).getMenu_id().equals(soldOutList.get(i).getMenuId())) {
                                                                selectMenuList.remove(j);
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    checkedSoldoutType(selectMenuList);
                                                }
                                            });

                                        }
                                    }else {

                                        //500 error
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
                                        tvRightButton.setVisibility(View.GONE);

                                        ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_cantorder));
                                        tvLeftButton.setText(getResources().getString(R.string.popup_can_not_order_left_btn));
                                        tvTitle.setText(getResources().getString(R.string.popup_can_not_order_title));
                                        tvMessage.setText(getResources().getString(R.string.popup_can_not_order_message));
                                        tvSubMessage.setText(getResources().getString(R.string.popup_can_not_order_sub_message));

                                        LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                                context.getResources().getString(R.string.popup_can_not_order_title));
                                        tvLeftButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                                        context.getResources().getString(R.string.popup_can_not_order_title),
                                                        tvLeftButton.getText().toString());
                                                popup.dismiss();
                                                onBackPressed();
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
            Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkedStatusPopupRightButton(final KioskPopup popup, final AddOrderReq addOrderReq) {

        if (NetworkManager.isNetworkOnline()) {

            Intent paymentIntent = new Intent(context, PaymentWebView.class);
            paymentIntent.putExtra("amount", addOrderReq.getPrice());
            paymentIntent.putExtra("paymentInfo", UiUtil.toStringGson(addOrderReq));
            paymentIntent.putExtra("order_id", order_id);
            paymentIntent.putExtra("tax", addOrderReq.getTax());
            paymentIntent.putExtra("points", addOrderReq.getPoints());
            startActivity(paymentIntent);
            popup.dismiss();

        } else {
            Toast.makeText(context, getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
            //popup
            final KioskPopup pyPopup = new KioskPopup(context);
            pyPopup.show();
            TextView tvTitle = (TextView) pyPopup.getContentView().findViewById(R.id.tvTitle);
            ImageView ivImage = (ImageView) pyPopup.getContentView().findViewById(R.id.ivImage);
            TextView tvMessage = (TextView) pyPopup.getContentView().findViewById(R.id.tvMessage);
            TextView tvSubMessage
                    = (TextView) pyPopup.getContentView().findViewById(R.id.tvSubMessage);
            final TextView tvLeftButton
                    = (TextView) pyPopup.getContentView().findViewById(R.id.tvLeftButton);
            final TextView tvRightButton
                    = (TextView) pyPopup.getContentView().findViewById(R.id.tvRightButton);

            ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_payment_error));
            tvLeftButton.setText(getResources().getString(R.string.popup_payment_error_left_btn));
            tvRightButton.setText(getResources().getString(R.string.popup_payment_error_right_btn));
            tvTitle.setText(getResources().getString(R.string.popup_payment_error_title));
            tvMessage.setText(getResources().getString(R.string.popup_payment_error_message));
            tvSubMessage.setText(getResources().getString(R.string.popup_payment_error_sub_message));

            LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                    context.getResources().getString(R.string.popup_payment_error_title));
            tvLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                            context.getResources().getString(R.string.popup_payment_error_title),
                            tvLeftButton.getText().toString());
                    pyPopup.dismiss();
                    popup.dismiss();
                }
            });
            tvRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                            context.getResources().getString(R.string.popup_payment_error_title),
                            tvRightButton.getText().toString());

                    if (NetworkManager.isNetworkOnline()) {

                        pyPopup.dismiss();
                        popup.dismiss();

                        Intent paymentIntent = new Intent(context, PaymentWebView.class);
                        paymentIntent.putExtra("amount", addOrderReq.getPrice());
                        paymentIntent.putExtra("paymentInfo", UiUtil.toStringGson(addOrderReq));
                        paymentIntent.putExtra("order_id", order_id);
                        paymentIntent.putExtra("tax", addOrderReq.getTax());
                        paymentIntent.putExtra("points", addOrderReq.getPoints());
                        startActivity(paymentIntent);

                    } else {

                        pyPopup.dismiss();
                        popup.dismiss();

                        pyPopup.show();
                        Toast.makeText(context, getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void getOrderHistory(final Context context, final OrderReq orderReq) {

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

                                    if (orderRes.getResponseStatus() != null && orderRes.getResponseStatus().equals(200)) {

                                        String orderInfo = UiUtil.toStringGson(orderRes.getData().get(0));
                                        String impossible_order = "true";

                                        Intent reorderIntent = new Intent(context, TicketActivity.class);
                                        reorderIntent.putExtra("clicked_order", orderInfo);
                                        reorderIntent.putExtra("impossible_order", impossible_order);
                                        reorderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        reorderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(reorderIntent);
                                        Toast.makeText(context, getResources().getString(R.string.soldout_choose_other_menus), Toast.LENGTH_SHORT).show();
                                        //finish();

                                    } else {
                                        LOGE(TAG, "getOrderHistory error !");
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

    private void checkedSoldoutType(ArrayList<CartMenuItem> selectMenuList) {
        /**
         * checkedIntentFlag
         * type 1 : from menu option Activity
         * type 2 : from cart Activity
         * type 3 : from order history detail Activity
         */
        if (selectMenuList.size() > 0) {

            switch (checkedIntentFlag) {

                case 1:
                    Intent optionIntent = getIntent();
                    finish();
                    optionIntent.putExtra("selectMenuList", selectMenuList);
                    startActivity(optionIntent);
                    Toast.makeText(context, getResources().getString(R.string.soldout_menu_removed), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (KioskPreference.getInstance().getCartInfo() != null) {
                        KioskPreference.getInstance().clearCartInfo();

                        for (CartMenuItem cartMenuItem : selectMenuList) {
                            KioskPreference.getInstance().setCartInfo(cartMenuItem);
                        }
                    }
                    Intent cartIntent = new Intent(context, CartMenuActivity.class);
                    cartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    cartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cartIntent);
                    Toast.makeText(context, getResources().getString(R.string.soldout_menu_removed), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Intent historyDetailIntent = getIntent();
                    finish();
                    historyDetailIntent.putExtra("selectMenuList", selectMenuList);
                    startActivity(historyDetailIntent);
                    Toast.makeText(context, getResources().getString(R.string.soldout_menu_removed), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    LOGE(TAG, "ERROR");
                    break;
            }
        } else {

            switch (checkedIntentFlag) {

                case 1:
                    Intent categoryIntent = new Intent(context, SubCategoryActivity.class);
                    categoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    categoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(categoryIntent);
                    Toast.makeText(context, getResources().getString(R.string.soldout_choose_other_menus), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (KioskPreference.getInstance().getCartInfo() != null) {
                        KioskPreference.getInstance().clearCartInfo();
                    }
                    Intent cartIntent = new Intent(context, CartMenuActivity.class);
                    cartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    cartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cartIntent);
                    Toast.makeText(context, getResources().getString(R.string.soldout_menu_removed), Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    OrderReq orderReq = new OrderReq();
                    orderReq.setUserId(userId);
                    orderReq.setStoreId(storeId);
                    orderReq.setOrderId(order_id);

                    getOrderHistory(context, orderReq);
                    break;

                default:
                    LOGE(TAG, "ERROR");
                    break;
            }
        }
    }
}
