package net.bluehack.kiosk.contact;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

public class ContactActivity extends BaseActivity {

    private Context context;
    private TextView tvPhone;
    private LinearLayout llContact;
    private String store_phone;
    private boolean ischeckedLink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        context = this;
        tvPhone = (TextView) findViewById(R.id.tvPhone);

        store_phone = "000-0000-0000";
        if (KioskPreference.getInstance().getStoreInfo() != null) {
            store_phone = KioskPreference.getInstance().getStoreInfo().getSPhone();
        }

        if (!store_phone.contains("-")) {
            String sign = "-";
            StringBuffer sb = new StringBuffer();
            sb.append(store_phone.substring(0,3));
            sb.append(sign);
            sb.append(store_phone.substring(3,store_phone.length()-1));
            store_phone = sb.toString();
        }

        tvPhone.setText(store_phone);
        tvPhone.setLinkTextColor(UiUtil.getColor(context, R.color.color_09));
        tvPhone.setLinksClickable(true);
        tvPhone.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        //tvPhone.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(tvPhone, Linkify.PHONE_NUMBERS);

        if (tvPhone.isFocused()) {
            LogEventTracker.ContactUsEvent(GaCategory.CONTACT_US, store_phone);
        }
    }
}
