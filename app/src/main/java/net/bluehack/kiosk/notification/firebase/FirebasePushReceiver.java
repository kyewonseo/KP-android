package net.bluehack.kiosk.notification.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class FirebasePushReceiver extends FirebaseMessagingService {

    private static final String TAG = makeLogTag(FirebasePushReceiver.class);

    public FirebasePushReceiver() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Map<String, String > message = remoteMessage.getData();
        for( String key : message.keySet() ){

            LOGD( TAG, String.format("키 : %s, 값 : %s", key, message) );
        }
    }
}
