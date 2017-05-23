package net.bluehack.kiosk.subcategory;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.BaseActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;

import io.swagger.client.ApiClient;
import io.swagger.client.model.SubcategoryReq;
import io.swagger.client.model.SubcategoryRes;
import io.swagger.client.model.SubcategoryResData;

public class SubCategoryActivity extends BaseActivity {

    private static final String TAG = makeLogTag(SubCategoryActivity.class);
    private Context context;
    private ImageView subcategory_cart;
    private TextView menu_cart_tv;
    private SubCategoryAdapter subCategoryAdapter;
    private RecyclerView subCategoryRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        context = this;

        subCategoryRecyclerView = (RecyclerView) findViewById(R.id.subcategory_rv_list);
        subcategory_cart = (ImageView) findViewById(R.id.subcategory_cart);
        menu_cart_tv = (TextView) findViewById(R.id.menu_cart_tv);

        layoutManager = new LinearLayoutManager(this);
        subCategoryAdapter = new SubCategoryAdapter(context);

        subCategoryRecyclerView.setHasFixedSize(true);
        subCategoryRecyclerView.setLayoutManager(layoutManager);
        subCategoryRecyclerView.setAdapter(subCategoryAdapter);

        if (KioskPreference.getInstance().getStoreInfo() != null) {

            String storeId = KioskPreference.getInstance().getStoreInfo().getStoreId();
            SubcategoryReq subcategoryReq = new SubcategoryReq();
            subcategoryReq.setStoreId(storeId);
            getSubCategoryList(context, subcategoryReq);

        } else {
            //error
            Toast.makeText(context, getResources().getString(R.string.subcategory_not_found_list), Toast.LENGTH_SHORT).show();
        }

        subcategory_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEventTracker.CartBtnEvent(GaCategory.CATEGORY);
                Intent intent = new Intent(context, CartMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getSubCategoryList(final Context context, final SubcategoryReq subcategoryReq) {

        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().categoryListMenuPost(context, subcategoryReq, new ApiClient.ApiResponseListener() {
                @Override
                public void onResponse(final Object result) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SubcategoryRes subcategoryRes = (SubcategoryRes) result;

                                    Gson gson = new Gson();
                                    LOGE(TAG, "subcategoryRes result : " + gson.toJson(result));

                                    if (subcategoryRes.getResponseStatus() != null && subcategoryRes.getResponseStatus().equals(200)) {

                                        LOGD(TAG, "login success!");

                                        subCategoryAdapter.clean();

                                        ArrayList<SubcategoryResData> subcategoryResDataItems = new ArrayList<>();

                                        for (SubcategoryResData item : subcategoryRes.getData()) {

                                            SubcategoryResData subcategoryResDataItem = new SubcategoryResData();
                                            subcategoryResDataItem.setSubCategoryId(item.getSubCategoryId());
                                            subcategoryResDataItem.setSubCategoryName(item.getSubCategoryName());

                                            subcategoryResDataItems.add(subcategoryResDataItem);
                                        }
                                        subCategoryAdapter.addItem(subcategoryResDataItems);
                                        subCategoryAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ApplicationLoader.getContext(), "can't get subcategory list.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            //offline
            Toast.makeText(ApplicationLoader.getContext(), getResources().getString(R.string.network_status), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (KioskPreference.getInstance().getCartInfo() != null) {

            if (KioskPreference.getInstance().getCartInfo().size() != 0) {
                menu_cart_tv.setVisibility(View.VISIBLE);
            } else {
                menu_cart_tv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
