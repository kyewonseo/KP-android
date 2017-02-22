package net.bluehack.kiosk;

import android.app.Application;
import android.content.Context;

import net.bluehack.kiosk.util.KioskPreference;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class ApplicationLoader extends Application {

    private static String TAG = makeLogTag(ApplicationLoader.class);

    private static volatile Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        KioskPreference.getInstance().init(instance);
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
