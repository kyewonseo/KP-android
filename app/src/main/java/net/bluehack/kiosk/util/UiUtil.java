package net.bluehack.kiosk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class UiUtil {

    public static Drawable getDrawable(Context context, int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static String toStringGson(Object object) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return gson.toJson(object);
    }

    public static void drawListViewFitHeight(ListView lv, BaseAdapter adapter) {
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight()
                * (lv.getCount() - 1));
        lv.setLayoutParams(params);
        lv.requestLayout();
    }

    public static int dpToPx(Context context, double dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getFileImgUrl() {
        String fileUrl = "http://175.207.13.212:8080/files/down?files_id=";
        return fileUrl;
    }

    public static String getClearentPK() {
        String pk = "307a301406072a8648ce3d020106092b240303020801010c036200048724788baf6bdfd84d4b397" +
                "c48b9c646effb8a1e710bc83c6b0c63b0610ff8c3624d8058031bb8002214ca89baf66a9101d0ff8d97" +
                "b07e04ca272bc4196185b9cf614d78b2b0ad0de9ca686e07c80cc9022ad835d5f8f4f90ef0202af7ac6ba4";
        return pk;
    }
}