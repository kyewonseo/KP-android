package net.bluehack.kiosk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class MobileOrderActivity extends AppCompatActivity {

    private Context context;

    public MobileOrderActivity(Context context) {
        this.context = context;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_order);
    }
}
