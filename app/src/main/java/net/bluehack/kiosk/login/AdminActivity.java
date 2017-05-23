package net.bluehack.kiosk.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;

import io.swagger.client.api.BasePath;

public class AdminActivity extends BaseActivity {

    private Context context;
    private EditText admin_url_et;
    private Button admin_url_btn;
    private String url = "";

    public AdminActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        context = this;

        admin_url_btn = (Button) findViewById(R.id.admin_url_btn);
        admin_url_et = (EditText) findViewById(R.id.admin_url_et);
        admin_url_et.setTextColor(Color.BLACK);

        admin_url_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                url = s.toString();
            }
        });
        admin_url_et.setText(url);
        admin_url_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasePath.getInstance().setBasePath(url);
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
