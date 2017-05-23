package net.bluehack.kiosk;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tsengvn.typekit.Typekit;

import io.fabric.sdk.android.Fabric;

import net.bluehack.kiosk.util.KioskPreference;

public class ApplicationLoader extends Application {

    private static String TAG = makeLogTag(ApplicationLoader.class);

    private static volatile Context instance;
    private static FirebaseAnalytics firebaseAnalytics;
    private static GoogleAnalytics gaAnalytics;
    private static Tracker gaTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = getApplicationContext();
        KioskPreference.getInstance().init(instance);

        Fabric.with(this, new Crashlytics());

        Typekit.getInstance()
                .addCustom1(Typekit.createFromAsset(this, "Roboto-Black.ttf"));
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static synchronized FirebaseAnalytics getFirebaseAnalytics() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(instance);
            firebaseAnalytics.setUserId(getUserId());
        }
        return firebaseAnalytics;
    }

    public static synchronized Tracker getGaTracker() {

        if (gaTracker == null) {
            gaAnalytics = GoogleAnalytics.getInstance(instance);
            gaTracker = gaAnalytics.newTracker(R.xml.global_tracker);
            gaTracker.enableAutoActivityTracking(true);
            gaTracker.enableExceptionReporting(true);
            //gaTracker.enableAdvertisingIdCollection(true);
        }
        return gaTracker;
    }

    public static String getUserId() {
        String user_id = "init_id";
        if (KioskPreference.getInstance().getLoginInfo() != null) {
            user_id = KioskPreference.getInstance().getLoginInfo().getAccountId();
        }
        return user_id;
    }

}
