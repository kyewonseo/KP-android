package net.bluehack.kiosk.history;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.home.model.Menu;
import net.bluehack.kiosk.home.model.Store;
import net.bluehack.kiosk.home.model.Ticket;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiClient;
import io.swagger.client.model.OrderReq;
import io.swagger.client.model.OrderRes;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class HistoryActivity extends BaseActivity {

    private static final String TAG = makeLogTag(HistoryActivity.class);
    Context context;
    TextView tvTitle;
    ListView lvHistory;
    LinearLayout llNoneHistory;
    HistoryTicketAdapter adapter;
    View history_exist;
    View history_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        context = this;

        lvHistory = (ListView) findViewById(R.id.lvHistory);
        llNoneHistory = (LinearLayout) findViewById(R.id.llNoneHistory);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        history_exist = (View) findViewById(R.id.history_exist);
        history_empty = (View) findViewById(R.id.history_empty);

        adapter = new HistoryTicketAdapter();
        lvHistory.setAdapter(adapter);

        //TODO: fixme => test user_id 사용 및 정렬 옵션 api 처리 x
        String storeId = "";
        String userId = "";
        if (KioskPreference.getInstance().getStoreInfo() != null) {
            storeId = KioskPreference.getInstance().getStoreInfo().getStoreId();
            userId = KioskPreference.getInstance().getStoreInfo().getUserId();
        }
        OrderReq orderReq = new OrderReq();
        orderReq.setStoreId(storeId);
        orderReq.setUserId(userId);
        orderReq.setOrderBy("createDate");
        orderReq.setOrderByWith("desc");
        getRecentOrderlist(context, orderReq);
    }

    private void getRecentOrderlist(final Context context, final OrderReq orderReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().orderPaymentListPost(context, orderReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OrderRes orderRes = (OrderRes) result;

                                    Gson gson = new Gson();
                                    LOGE(TAG, "orderPaymentListPost result : " + gson.toJson(result));

                                    if (orderRes.getResponseStatus() != null && orderRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");
                                        history_exist.setVisibility(View.VISIBLE);
                                        history_empty.setVisibility(View.GONE);

                                        adapter.clean();

                                        for (int i = 0; i < orderRes.getData().size(); i++) {
                                            adapter.addItem(orderRes.getData().get(i));
                                        }

                                        if (adapter.getCount() == 0) {
                                            lvHistory.setVisibility(View.GONE);
                                            llNoneHistory.setVisibility(View.VISIBLE);
                                        } else {
                                            lvHistory.setVisibility(View.VISIBLE);
                                            llNoneHistory.setVisibility(View.GONE);
                                        }
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        //Toast.makeText(ApplicationLoader.getContext(), "can't get menu option list.", Toast.LENGTH_SHORT).show();
                                        history_exist.setVisibility(View.GONE);
                                        history_empty.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.network_status) , Toast.LENGTH_SHORT).show();
        }
    }
}
