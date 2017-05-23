package net.bluehack.kiosk.login;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;
import com.google.firebase.iid.FirebaseInstanceId;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.home.HomeActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.store.StoreActivity;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiInvoker;
import io.swagger.client.api.AccountApi;
import io.swagger.client.model.LoginReq;
import io.swagger.client.model.LoginRes;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final String TAG = makeLogTag(LoginActivity.class);
    private Button login_btn;
    private EditText login_text_id;
    private EditText login_text_pw;
    private TextView login_underline_text_id;
    private TextView login_underline_text_pw;
    private LoginReq loginReq;
    private String loginIdText = "";
    private String loginPwText = "";
    private LinearLayout login_ll_admin;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        //auto login
        if (checkedAutoLogin()) {
            if (KioskPreference.getInstance().getStoreInfo() == null) {
                Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        login_btn = (Button) findViewById(R.id.login_btn);
        login_text_id = (EditText) findViewById(R.id.login_text_id);
        login_text_pw = (EditText) findViewById(R.id.login_text_pw);
        login_underline_text_id = (TextView) findViewById(R.id.login_underline_text_id);
        login_underline_text_pw = (TextView) findViewById(R.id.login_underline_text_pw);
        login_ll_admin = (LinearLayout) findViewById(R.id.login_ll_admin);

        login_ll_admin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(adminIntent);
                finish();
                return false;
            }
        });
        login_text_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_underline_text_id.setBackgroundColor(getColor(getApplicationContext(), R.color.color_09));
                    login_underline_text_pw.setBackgroundColor(getColor(getApplicationContext(), R.color.color_04));
                }
            }
        });
        login_text_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                loginIdText = s.toString();

                if (loginPwText.length() <= 0 && loginIdText.length() <= 0) {
                    login_btn.setBackground(UiUtil.getDrawable(context, R.drawable.btn_type1_click_event));
                }
            }
        });

        login_text_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_underline_text_pw.setBackgroundColor(getColor(getApplicationContext(), R.color.color_09));
                    login_underline_text_id.setBackgroundColor(getColor(getApplicationContext(), R.color.color_04));
                }
            }
        });
        login_text_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                login_btn.setBackground(UiUtil.getDrawable(context, R.drawable.btn_type1_click_event));
                loginPwText = s.toString();

                if (loginPwText.length() <= 0) {
                    login_btn.setBackgroundColor(getColor(getApplicationContext(), R.color.color_07));
                }
                if (loginPwText.length() <= 0 && loginIdText.length() <= 0) {
                    login_btn.setBackgroundColor(getColor(getApplicationContext(), R.color.color_07));
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.LoginBtnEvent(GaCategory.LOGIN);
                String push_token = FirebaseInstanceId.getInstance().getToken();

                loginReq = new LoginReq();
                //TODO: fixme => TEST ACCOUNT LOGIN
                if (loginIdText.length() == 0 ) {
                    loginReq.setAccountId("test@gmail.com");
                } else {
                    loginReq.setAccountId(loginIdText);
                }
                if (loginPwText.length() == 0 ) {
                    loginReq.setPasswd("1111");
                } else {
                    loginReq.setPasswd(loginPwText);
                }

                loginReq.setPushToken(push_token);

                LOGD(TAG, "loginReq info : " + loginIdText + "," + loginPwText);
                LOGD(TAG, "push_token : " + push_token);

                doLogin(context, loginReq);
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

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    private void doLogin(final Context context, final LoginReq loginReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().accountLoginPost(context, loginReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoginRes loginRes = (LoginRes) result;

                                    if (loginRes.getResponseStatus() != null && loginRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        //set preference
                                        KioskPreference.getInstance().setLoginInfo(loginRes.getData().get(0));
                                        LogEventTracker.userDeviceInfo(GaCategory.LOGIN);

                                        Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                        if (loginIdText.length() > 0 || loginPwText.length() > 0) {
                                            loginIdText = "";
                                            loginPwText = "";
                                            login_text_id.setText("");
                                            login_text_pw.setText("");
                                            login_text_id.requestFocus();
                                        }
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

    private boolean checkedAutoLogin() {
        if (KioskPreference.getInstance().getLoginInfo() != null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
