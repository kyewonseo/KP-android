package net.bluehack.kiosk.subcategory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.model.SubcategoryRes;
import net.bluehack.kiosk.model.SubcategoryResDataItem;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class SubCategoryActivity extends Activity {

    private static final String TAG = makeLogTag(SubCategoryActivity.class);
    private Context context;
    private ImageView subcategory_cart;
    private ImageView subcategory_search;
    private SubCategoryAdapter subCategoryAdapter;
    private RecyclerView subCategoryRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String storeId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        context = this;

        subCategoryRecyclerView = (RecyclerView) findViewById(R.id.subcategory_rv_list);
        subcategory_cart        = (ImageView) findViewById(R.id.subcategory_cart);
        subcategory_search      = (ImageView) findViewById(R.id.subcategory_search);

        layoutManager       = new LinearLayoutManager(this);
        subCategoryAdapter  = new SubCategoryAdapter(context);

        subCategoryRecyclerView.setHasFixedSize(true);
        subCategoryRecyclerView.setLayoutManager(layoutManager);
        subCategoryRecyclerView.setAdapter(subCategoryAdapter);

        if (KioskPreference.getInstance().getStoreId() != null) {

            storeId = KioskPreference.getInstance().getStoreId();
            getSubCategoryList(storeId);

        }else {
            //error
            Toast.makeText(context, "can't get store_id ", Toast.LENGTH_SHORT).show();
        }

        subcategory_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click event
            }
        });

        subcategory_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click event
            }
        });
    }

    private void getSubCategoryList(final String storeId) {

        //LOGE(TAG, "storeid : " + storeId);
        if (NetworkManager.isNetworkOnline()) {
            ApiClient.getInstance().categoriesSubcategoriesStoreIdGet(storeId, new ApiClient.ApiResponseListener() {
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

                                    if (subcategoryRes.getResponseStatus() != null && subcategoryRes.getResponseStatus().equals("200")) {

                                        LOGD(TAG, "login success!");

                                        subCategoryAdapter.clean();

                                        ArrayList<SubcategoryResDataItem> subcategoryResDataItems = new ArrayList<>();

                                        for (SubcategoryResDataItem item : subcategoryRes.getData()) {

                                            SubcategoryResDataItem subcategoryResDataItem = new SubcategoryResDataItem();
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
            Toast.makeText(ApplicationLoader.getContext(),"check network status!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
