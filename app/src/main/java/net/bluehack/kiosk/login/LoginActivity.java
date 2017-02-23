package net.bluehack.kiosk.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.main.MainActivity;
import net.bluehack.kiosk.model.LoginReq;
import net.bluehack.kiosk.model.LoginRes;
import net.bluehack.kiosk.model.LoginResDataItem;
import net.bluehack.kiosk.store.StoreActivity;
import net.bluehack.kiosk.util.KioskPreference;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class LoginActivity extends Activity {

    private static final String TAG = makeLogTag(LoginActivity.class);
    private Button login_btn;
    private EditText login_text_id;
    private EditText login_text_pw;
    private TextView login_underline_text_id;
    private TextView login_underline_text_pw;
    private LoginReq loginReq;
    private static String loginIdText;
    private static String loginPwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (chackAutoLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }

        login_btn               = (Button) findViewById(R.id.login_btn);
        login_text_id           = (EditText) findViewById(R.id.login_text_id);
        login_text_pw           = (EditText) findViewById(R.id.login_text_pw);
        login_underline_text_id = (TextView) findViewById(R.id.login_underline_text_id);
        login_underline_text_pw = (TextView) findViewById(R.id.login_underline_text_pw);

        login_text_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_underline_text_id.setBackgroundColor(getColor(getApplicationContext(),R.color.color_09));
                    login_underline_text_pw.setBackgroundColor(getColor(getApplicationContext(),R.color.color_04));
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
            }
        });

        login_text_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_underline_text_pw.setBackgroundColor(getColor(getApplicationContext(),R.color.color_09));
                    login_underline_text_id.setBackgroundColor(getColor(getApplicationContext(),R.color.color_04));
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
                login_btn.setBackgroundColor(getColor(getApplicationContext(),R.color.color_09));
                loginPwText = s.toString();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGE(TAG, "loginReq info : " + loginIdText + "," + loginPwText);

                loginReq = new LoginReq();
                loginReq.setLoginId(loginIdText);
                loginReq.setLoginPasswd(loginPwText);

                doLogin(loginReq);
            }
        });
    }

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    private void doLogin(final LoginReq loginReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().usersLoginPost(loginReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoginRes loginRes = (LoginRes) result;

                                    if (loginRes.getResponseStatus() != null && loginRes.getResponseStatus().equals("200")) {

                                        LOGD(TAG, "login success!");

                                        //set preference
                                        Gson gson = new Gson();
                                        String loginResDataItem = gson.toJson(loginRes.getData());
                                        KioskPreference.getInstance().setLoginInfo(loginResDataItem);

                                        Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "아이디 / 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(),"check network status!",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean chackAutoLogin() {
        if (KioskPreference.getInstance().getLoginInfo() != null) {
            return true;
        } else {
            return false;
        }
    }
}
