/*
package net.bluehack.kiosk.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.main.RecentCardAdapter;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView catalogRecyclerView;
    private RecyclerView menuItemRecyclerView;
    private RecyclerView.LayoutManager catalogLayoutManager;
    private RecyclerView.LayoutManager menuItemLayoutManager;
    private CatalogAdapter catalogAdapter;
    private MenuItemAdapter menuItemAdapter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.btn_main_menu_nor);
        actionBar.setDisplayHomeAsUpEnabled(true);

        catalogRecyclerView = (RecyclerView) findViewById(R.id.catalog_list);
        menuItemRecyclerView = (RecyclerView) findViewById(R.id.order_menu_list);

        catalogLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        menuItemLayoutManager = new LinearLayoutManager(this);
        menuItemAdapter = new MenuItemAdapter();
        catalogAdapter = new CatalogAdapter(context, menuItemAdapter);

        catalogRecyclerView.setHasFixedSize(true);
        catalogRecyclerView.setLayoutManager(catalogLayoutManager);
        catalogRecyclerView.setAdapter(catalogAdapter);

        menuItemRecyclerView.setHasFixedSize(true);
        menuItemRecyclerView.setLayoutManager(menuItemLayoutManager);
        menuItemRecyclerView.setAdapter(menuItemAdapter);

    }

}
*/
