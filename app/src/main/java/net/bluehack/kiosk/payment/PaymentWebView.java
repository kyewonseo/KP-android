package net.bluehack.kiosk.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.home.HomeActivity;

import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.UiUtil;

import io.swagger.client.ApiClient;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.OrderCompleteReq;
import io.swagger.client.model.OrderCompleteRes;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class PaymentWebView extends BaseActivity {

    private final Handler handler = new Handler();
    private static final String TAG = makeLogTag(PaymentWebView.class);
    private Context context;
    private WebView webView;
    private LinearLayout progress_ll;
    private ImageView progress_iv;
    private AddOrderReq addOrderReq = new AddOrderReq();
    private String amount = "";
    private String order_id = "";
    private String orderCompleteTax = "0";
    private String orderCompletePoints = "0";
    private boolean isLoadingChecked = false;
    private static final int TIMEOUT = 30000;
    private final String pk = UiUtil.getClearentPK();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_webview);

        context = this;

        setLayout();

        Intent intent = getIntent();
        if (intent.getSerializableExtra("paymentInfo") != null) {
            String paymentInfo = intent.getExtras().getString("paymentInfo");
            Gson gson = new Gson();
            addOrderReq = gson.fromJson(paymentInfo, AddOrderReq.class);
            intent.removeExtra("paymentInfo");
        }
        if (intent != null) {
            amount = intent.getExtras().getString("amount");
            amount = String.format("%.2f", Double.valueOf(amount));
            order_id = intent.getExtras().getString("order_id");
            orderCompleteTax = intent.getExtras().getString("tax");
            orderCompletePoints = intent.getExtras().getString("points");

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        webView.loadUrl("http://kiosk-payment-ui.s3-website.ap-northeast-2.amazonaws.com?pk="+pk+"&amount="+amount);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            LogEventTracker.CloseWebViewEvent(GaCategory.PAYMENT_WEB);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setLayout(){

        progress_ll = (LinearLayout) findViewById(R.id.progress_ll);
        progress_iv = (ImageView) findViewById(R.id.progress_iv);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progress_iv);
        Glide.with(this).load(R.raw.img_loading_gif).into(imageViewTarget);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                progress_ll.setVisibility(View.VISIBLE);

                //mix 30sec timeout
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLoadingChecked) {
                            //Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
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

                            ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_payment_error));
                            tvLeftButton.setText(getResources().getString(R.string.popup_payment_error_left_btn));
                            tvRightButton.setText(getResources().getString(R.string.popup_payment_error_right_btn));
                            tvTitle.setText(getResources().getString(R.string.popup_payment_error_title));
                            tvMessage.setText(getResources().getString(R.string.popup_payment_error_message));
                            tvSubMessage.setText(getResources().getString(R.string.popup_payment_error_sub_message));

                            LogEventTracker.OpenPopupEvent(GaCategory.POP_UP, context.getResources().getString(R.string.popup_payment_error_title));
                            tvLeftButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                            context.getResources().getString(R.string.popup_payment_error_title),
                                            tvLeftButton.getText().toString());
                                    popup.dismiss();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    });
                                }
                            });
                            tvRightButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                            context.getResources().getString(R.string.popup_payment_error_title),
                                            tvRightButton.getText().toString());
                                    popup.dismiss();
                                    webView.loadUrl("http://kiosk-payment-ui.s3-website.ap-northeast-2.amazonaws.com?pk="+pk+"&amount="+amount);
                                }
                            });
                        }
                    }
                }, TIMEOUT);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                isLoadingChecked = true;
                progress_ll.setVisibility(View.GONE);
                LogEventTracker.OpenWebViewEvent(GaCategory.PAYMENT_WEB);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                //Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
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
                        popup.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        });
                    }
                });
                tvRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_payment_error_title),
                                tvRightButton.getText().toString());
                        popup.dismiss();
                        webView.loadUrl("http://kiosk-payment-ui.s3-website.ap-northeast-2.amazonaws.com?pk="+pk+"&amount="+amount);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.clearHistory();
        webView.clearCache(true);

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void clearentCancel() {
            LOGE(TAG, "close webview");
            LogEventTracker.CloseWebViewEvent(GaCategory.PAYMENT_WEB);
            finish();

        }

        @JavascriptInterface
        public void showResponse(String response) {
            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            LOGE(TAG, "response =>" + response);

            if (response.equals("200")) {

                String orderCompletePrice = "0";
                String orderCompleteCard = "0";
                if (addOrderReq != null) {

                    if (addOrderReq.getPrice() != null) {
                        orderCompletePrice = addOrderReq.getPrice();
                        orderCompleteCard = addOrderReq.getPrice();
                    }
                }
                OrderCompleteReq orderCompleteReq = new OrderCompleteReq();
                orderCompleteReq.setOrderId(order_id);
                orderCompleteReq.setStoreId(addOrderReq.getStoreId());
                orderCompleteReq.setUserId(addOrderReq.getUserId());
                orderCompleteReq.setPrice(orderCompletePrice);
                orderCompleteReq.setCard(orderCompleteCard);
                orderCompleteReq.setPoints(orderCompletePoints);
                orderCompleteReq.setTax(orderCompleteTax);
                completedOrder(context, orderCompleteReq);

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
                        popup.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        });
                    }
                });
                tvRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_payment_error_title),
                                tvRightButton.getText().toString());
                        popup.dismiss();
                    }
                });
            }

        }
    }

    private void completedOrder(final Context context, final OrderCompleteReq orderCompleteReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().ordersPaymentCompletePost(context, orderCompleteReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OrderCompleteRes orderCompleteRes = (OrderCompleteRes) result;

                                    if (orderCompleteRes != null && orderCompleteRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "registerOrder success!");

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

                                        ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_payment_complete));
                                        tvLeftButton.setText(getResources().getString(R.string.popup_payment_success_left_btn));
                                        tvTitle.setText(getResources().getString(R.string.popup_payment_success_title));
                                        tvMessage.setText(getResources().getString(R.string.popup_payment_success_message));
                                        tvSubMessage.setText(getResources().getString(R.string.popup_payment_success_sub_message_01)
                                                + " " + addOrderReq.getPoints() + " " +
                                                getResources().getString(R.string.popup_payment_success_sub_message_02));
                                        tvSubMessage.setTextColor(Color.RED);

                                        LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                                context.getResources().getString(R.string.popup_payment_success_title));

                                        tvLeftButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                                        context.getResources().getString(R.string.popup_payment_success_title),
                                                        tvLeftButton.getText().toString());
                                                popup.dismiss();
                                                OrderReq orderReq = new OrderReq();
                                                orderReq.setStoreId(addOrderReq.getStoreId());
                                                orderReq.setUserId(addOrderReq.getUserId());
                                                orderReq.setOrderId(order_id);

                                                getOrderHistory(context, orderReq);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
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

    private void getOrderHistory(final Context context, final OrderReq orderReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().orderPaymentListPost(context, orderReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OrderRes orderRes = (OrderRes) result;

                            if (orderRes.getResponseStatus() != null && orderRes.getResponseStatus().equals(200)) {

                                String orderInfo = UiUtil.toStringGson(orderRes.getData().get(0));

                                Intent ticketIntent = new Intent(context, TicketActivity.class);
                                ticketIntent.putExtra("clicked_order", orderInfo);
                                ticketIntent.putExtra("isCheckedGoHome", "true");
                                ticketIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ticketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ticketIntent);

                            } else {
                                LOGE(TAG, "getOrderHistory error !");
                            }
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
        }
    }
}