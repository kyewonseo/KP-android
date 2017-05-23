package net.bluehack.kiosk.notification.firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class FirebaseTokenService extends FirebaseInstanceIdService {

    private static final String TAG = makeLogTag(FirebaseTokenService.class);

    @Override
    public void onTokenRefresh() {

        String push_token = FirebaseInstanceId.getInstance().getToken();
        LOGD(TAG, "init push_token : " + push_token);

    }
}