package net.bluehack.kiosk.myinfo;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.login.LoginActivity;
import net.bluehack.kiosk.util.KioskPreference;

public class MyInfoActivity extends BaseActivity {

    private static final String TAG = makeLogTag(MyInfoActivity.class);
    private Context context;
    private TextView myinfo_text_id;
    private TextView myinfo_text_name;
    private TextView myinfo_text_phone;
    private TextView myinfo_text_point;
    private ImageView myinfo_iv_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        context = getApplicationContext();

        myinfo_text_id = (TextView) findViewById(R.id.myinfo_text_id);
        myinfo_text_name = (TextView) findViewById(R.id.myinfo_text_name);
        myinfo_text_phone = (TextView) findViewById(R.id.myinfo_text_phone);
        myinfo_text_point = (TextView) findViewById(R.id.myinfo_text_point);
        myinfo_iv_btn = (ImageView) findViewById(R.id.myinfo_iv_btn);

        if (KioskPreference.getInstance().getStoreInfo() != null && KioskPreference.getInstance().getLoginInfo() != null) {
            String storeName = KioskPreference.getInstance().getStoreInfo().getStore();
            String accountId = KioskPreference.getInstance().getLoginInfo().getAccountId();
            String userName;
            String userPhone;
            int userPoint = 0;

            if (KioskPreference.getInstance().getStoreInfo().getUPoints() != null) {
                userPoint = KioskPreference.getInstance().getStoreInfo().getUPoints();
            } else {
                userPoint = 0;
            }

            if (KioskPreference.getInstance().getLoginInfo().getName() != null) {
                userName = KioskPreference.getInstance().getLoginInfo().getName();
            } else {
                userName = "";
            }

            if (KioskPreference.getInstance().getLoginInfo().getPhone() != null) {
                userPhone = KioskPreference.getInstance().getLoginInfo().getPhone();
            } else {
                userPhone = "";
            }

            myinfo_text_id.setText(accountId);
            myinfo_text_name.setText(userName);
            myinfo_text_phone.setText(userPhone);
            myinfo_text_point.setText(String.valueOf(userPoint) + "(" + storeName + ")");
        }

        //log out
        myinfo_iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.LogoutBtnEvent(GaCategory.MYINFO);
                //init My Info
                KioskPreference.getInstance().clearLoginInfo();
                KioskPreference.getInstance().clearStoreInfo();
                KioskPreference.getInstance().clearCartInfo();

                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
