package net.bluehack.kiosk.login;

import android.app.Activity;
import android.os.Bundle;

import net.bluehack.kiosk.R;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class LoginActivity extends Activity {

    private static final String TAG = makeLogTag(LoginActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}
