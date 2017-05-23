package net.bluehack.kiosk.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import net.bluehack.kiosk.R;

public class KioskPopup {

    private Context context;
    private AlertDialog alertDialog;
    private KioskPopup.Builder builder;

    private boolean hasShow = false;
    private View contentView;

    public KioskPopup(Context context) {
        this.context = context;
    }

    public void show() {
        if (!hasShow) {
            builder = new Builder();
        } else {
            alertDialog.show();
            alertDialog.getWindow()
                    .clearFlags(
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        hasShow = true;
    }

    public View getContentView() {
        return contentView;
    }

    public void dismiss() {
        try {
            alertDialog.dismiss();
        } catch (IllegalArgumentException e) {
            // Activity 종료 후 dismiss
        }
    }

    public class Builder {

        private Window alertdialogwindow;


        public void setContentView(View contentView) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            contentView.setLayoutParams(layoutParams);

            LinearLayout linearLayout
                    = (LinearLayout) alertdialogwindow.findViewById(R.id.llPopup);
            if (linearLayout != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(contentView);
            }
        }

        public Builder() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.popup_two_button, null);

            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);

            builder.setView(contentView);
            alertDialog = builder.create();

            try {
                alertDialog.show();
            } catch (WindowManager.BadTokenException e) {
                return;
            } catch (Exception e) {
                return;
            }

            // 밖 영역을 선택하는 경우 처리
            alertDialog.setCancelable(false);

        }
    }
}
