package net.bluehack.kiosk;

import android.app.Application;
import android.content.Context;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class ApplicationLoader extends Application {

    private static String TAG = makeLogTag(ApplicationLoader.class);

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
