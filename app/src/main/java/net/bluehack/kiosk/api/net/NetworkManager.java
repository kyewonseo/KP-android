package net.bluehack.kiosk.api.net;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.UiUtil;

public class NetworkManager {

    private static final String TAG = makeLogTag(NetworkManager.class);
    private static NetworkManager ourInstance = new NetworkManager();
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private NetworkManager() {
    }

    public static boolean isNetworkOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) ApplicationLoader.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable())) {
                return true;
            }

            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGE(TAG, String.valueOf(e));
            return true;
        }
        return false;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkManager.getConnectivityStatus(context);
        String status = null;

        if (conn == NetworkManager.TYPE_NOT_CONNECTED) {
            status = context.getResources().getString(R.string.network_status);
        }
        /*if (conn == NetworkManager.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkManager.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkManager.TYPE_NOT_CONNECTED) {
            status = context.getResources().getString(R.string.network_status);
        }*/
        return status;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static void showServerErrorPopup(final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
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

                        FrameLayout flLine = (FrameLayout) popup.getContentView().findViewById(R.id.flLine);

                        ivImage.setVisibility(View.GONE);
                        flLine.setVisibility(View.VISIBLE);

                        tvLeftButton.setText(context.getResources().getString(R.string.popup_server_error_left_btn));
                        tvTitle.setText(context.getResources().getString(R.string.popup_server_error_title));
                        tvMessage.setText(context.getResources().getString(R.string.popup_server_error_message));
                        tvSubMessage.setText(context.getResources().getString(R.string.popup_server_error_sub));

                        LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_server_error_title));

                        tvRightButton.setVisibility(View.GONE);
                        tvLeftButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                        context.getResources().getString(R.string.popup_server_error_title),
                                        tvLeftButton.getText().toString());
                                popup.dismiss();
                            }
                        });
                    }
                });
            }
        }).start();
    }
}