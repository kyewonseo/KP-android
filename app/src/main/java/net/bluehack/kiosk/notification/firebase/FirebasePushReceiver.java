package net.bluehack.kiosk.notification.firebase;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.home.HomeActivity;
import net.bluehack.kiosk.home.model.Ticket;
import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.popup.CompletedStatusPopup;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.PushWakeLock;
import net.bluehack.kiosk.util.UiUtil;

import java.util.Map;

import io.swagger.client.ApiClient;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;

public class FirebasePushReceiver extends FirebaseMessagingService {

    private static final String TAG = makeLogTag(FirebasePushReceiver.class);
    private Context context;
    private String order_id = "";

    public FirebasePushReceiver() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = ApplicationLoader.getContext();

        Map<String, String> message = remoteMessage.getData();
        for (String key : message.keySet()) {

            LOGD(TAG, String.format("키 : %s, 값 : %s", key, message));
        }
        LOGD(TAG, "COOK_STATUS:" + message.get("type"));

        notificationStatusBar(message);
    }

    private void notificationStatusBar(Map<String, String> message) {

        int cook_status = 0;
        String order_num = "";
        String contentTitle = "";
        String contentText = "";

        if (message.get("type") != null) {
            cook_status = Integer.valueOf(message.get("type"));
        }
        if (message.get("order_num") != null) {
            order_num = message.get("order_num");
        }

        if (message.get("order_id") != null) {
            order_id = message.get("order_id");
        }

        switch (cook_status) {

            case Ticket.RECEIVE:

                contentTitle = String.valueOf(Ticket.State.Prepare);
                contentText = getResources().getString(R.string.push_prepare_01) + "(" + order_num + ")"
                        + getResources().getString(R.string.push_prepare_02);

                getOrderHistory(context, createOrderReq(), contentTitle, contentText);
                break;

            case Ticket.MAKING:

                contentTitle = String.valueOf(Ticket.State.InCook);
                contentText = getResources().getString(R.string.push_in_cook) + "(" + order_num + ")";

                getOrderHistory(context, createOrderReq(), contentTitle, contentText);
                break;

            case Ticket.COMPLETE:

                contentTitle = String.valueOf(Ticket.State.Ready);
                contentText = getResources().getString(R.string.push_ready_01) + "(" + order_num + ")"
                        + getResources().getString(R.string.push_ready_02);

                getOrderHistory(context, createOrderReq(), contentTitle, contentText);
                completedStatusPopup(order_num);
                break;

            default:
                break;
        }
    }

    private OrderReq createOrderReq() {

        String store_id = "";
        String user_id = "";

        if (KioskPreference.getInstance().getStoreInfo() != null) {
            store_id = KioskPreference.getInstance().getStoreInfo().getStoreId();
            user_id = KioskPreference.getInstance().getStoreInfo().getUserId();
        }

        OrderReq orderReq = new OrderReq();
        orderReq.setStoreId(store_id);
        orderReq.setUserId(user_id);
        orderReq.setOrderId(order_id);

        return orderReq;
    }

    private void getOrderHistory(final Context context, final OrderReq orderReq, final String contentTitle, final String contentText) {

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

                                makeNotification(ticketIntent, contentTitle, contentText);

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


    private void makeNotification (Intent ticketIntent, final String contentTitle, final String contentText) {

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ticketIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder compatBuilder = new NotificationCompat.Builder(this);
        compatBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        compatBuilder.setSmallIcon(R.drawable.img_notice_loogo_mini);
        compatBuilder.setTicker(contentText);
        compatBuilder.setWhen(System.currentTimeMillis());
        compatBuilder.setContentTitle(contentTitle);
        compatBuilder.setContentText(contentText);
        compatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        compatBuilder.setContentIntent(pendingIntent);
        compatBuilder.setAutoCancel(true);

        nm.notify(1, compatBuilder.build());
    }

    private void completedStatusPopup(String order_num) {

        Intent intent = new Intent(context, CompletedStatusPopup.class);
        intent.putExtra("order_num", order_num);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
