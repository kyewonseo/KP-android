package net.bluehack.kiosk.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import static net.bluehack.kiosk.util.Logger.makeLogTag;


public class StoreActivity extends Activity {

    private static final String TAG = makeLogTag(StoreActivity.class);
    private Context context;
    private StoreAdapter storeAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.bluehack.kiosk.R.layout.activity_store);

        context = getApplicationContext();
    }

}
