package net.bluehack.kiosk.popup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.UiUtil;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CompletedStatusPopup extends BaseActivity {

    private static final String TAG = makeLogTag(CompletedStatusPopup.class);
    private Context context;
    private LinearLayout llPopup;
    private TextView tvTitle;
    private ImageView ivImage;
    private TextView tvMessage;
    private TextView tvSubMessage;
    private TextView tvLeftButton;
    private TextView tvRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_two_button);

        context = this;

        llPopup = (LinearLayout) findViewById(R.id.llPopup);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvSubMessage = (TextView) findViewById(R.id.tvSubMessage);
        tvLeftButton = (TextView) findViewById(R.id.tvLeftButton);
        tvRightButton = (TextView) findViewById(R.id.tvRightButton);

        Intent intent = getIntent();
        StringBuffer order_num = new StringBuffer();
        order_num.append("(");
        if (intent.getExtras().getString("order_num") != null) {
            order_num.append(intent.getExtras().getString("order_num"));
        }
        order_num.append(")");

        llPopup.setBackground(UiUtil.getDrawable(context, R.color.color_03));
        tvTitle.setText(getResources().getString(R.string.popup_complete_title));
        ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_complete_menu));

        String message_01 = getResources().getString(R.string.popup_complete_message_01);
        String message_02 = getResources().getString(R.string.popup_complete_message_02);

        Spannable sp = new SpannableString(order_num.toString());
        sp.setSpan(new ForegroundColorSpan(Color.RED), 0, order_num.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvMessage.setText(message_01 + sp.toString() + message_02);
        tvSubMessage.setVisibility(View.GONE);

        tvLeftButton.setText(getResources().getString(R.string.popup_complete_left_btn));

        LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                context.getResources().getString(R.string.popup_complete_title));

        tvLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                        context.getResources().getString(R.string.popup_complete_title),
                        tvLeftButton.getText().toString());
                onBackPressed();
            }
        });
        tvRightButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
