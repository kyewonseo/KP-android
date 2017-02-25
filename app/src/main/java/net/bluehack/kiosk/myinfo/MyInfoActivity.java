package net.bluehack.kiosk.myinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.login.LoginActivity;
import net.bluehack.kiosk.model.LoginRes;
import net.bluehack.kiosk.model.LoginResDataItem;
import net.bluehack.kiosk.util.KioskPreference;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MyInfoActivity extends Activity {

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

        myinfo_text_id      = (TextView) findViewById(R.id.myinfo_text_id);
        myinfo_text_name    = (TextView) findViewById(R.id.myinfo_text_name);
        myinfo_text_phone   = (TextView) findViewById(R.id.myinfo_text_phone);
        myinfo_text_point   = (TextView) findViewById(R.id.myinfo_text_point);
        myinfo_iv_btn       = (ImageView) findViewById(R.id.myinfo_iv_btn);

        LoginRes myInfoList = getMyinfoList();
        LoginResDataItem myInfo = new LoginResDataItem();

        if (KioskPreference.getInstance().getStoreId() != null) {
            String storeId = KioskPreference.getInstance().getStoreId();

            for (LoginResDataItem loginInfo : myInfoList.getData()) {
                if (loginInfo.getStoreId().equals(storeId)) {
                    myInfo = loginInfo;
                    break;
                }
            }

            myinfo_text_id.setText(myInfo.getAccountId());
            myinfo_text_name.setText(myInfo.getName());
            myinfo_text_phone.setText(myInfo.getPhone());
            myinfo_text_point.setText(myInfo.getUPoints() + "(" + KioskPreference.getInstance().getStoreName() + ")");
        }

        //log out
        myinfo_iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //init My Info
                KioskPreference.getInstance().clearLoginInfo();
                KioskPreference.getInstance().clearStoreId();
                KioskPreference.getInstance().clearStoreName();

                Intent intent = new Intent(MyInfoActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

    private LoginRes getMyinfoList() {

        LoginRes myinfo = null;

        if (KioskPreference.getInstance().getLoginInfo() == null) {
            Toast.makeText(context, "can't get myinfo list!",Toast.LENGTH_SHORT).show();
            return myinfo;
        }else {
            String loginInfo = KioskPreference.getInstance().getLoginInfo();

            Gson gson = new Gson();
            myinfo = gson.fromJson(loginInfo, LoginRes.class);
            return myinfo;
        }
    }
}
