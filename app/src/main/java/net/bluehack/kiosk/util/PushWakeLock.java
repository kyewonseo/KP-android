package net.bluehack.kiosk.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class PushWakeLock {
    private static final String TAG = makeLogTag(PushWakeLock.class);
    private static PowerManager.WakeLock wakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static PushWakeLock ourInstance = new PushWakeLock();
    private static boolean isScreenLock;

    public static PushWakeLock getInstance() {
        return ourInstance;
    }

    public void acquireCpuWakeLock(Context context) {
        LOGD(TAG, "acquireCpuWakeLock =>" + wakeLock);

        if (wakeLock != null) {
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hello");

        wakeLock.acquire(3000);
    }

    public void releaseCpuLock() {
        LOGD(TAG, "releaseCpuLock =>" + wakeLock);

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}