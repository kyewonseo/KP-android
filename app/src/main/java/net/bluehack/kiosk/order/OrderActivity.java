package net.bluehack.kiosk.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.main.RecentCardAdapter;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderKindMenuRecyclerView;
    private RecyclerView orderMenuRecyclerView;
    private RecentCardAdapter orderKindMenuCardAdapter;
    private RecentCardAdapter orderMenuCardAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.btn_main_menu_nor);
        actionBar.setDisplayHomeAsUpEnabled(true);

        orderKindMenuRecyclerView = (RecyclerView) findViewById(R.id.order_kind_menu_list);
        orderMenuRecyclerView = (RecyclerView) findViewById(R.id.order_menu_list);

        orderKindMenuRecyclerView.setHasFixedSize(true);
        orderKindMenuRecyclerView.setLayoutManager(layoutManager);
        orderKindMenuRecyclerView.setAdapter(orderKindMenuCardAdapter);
        orderMenuRecyclerView.setHasFixedSize(true);
        orderMenuRecyclerView.setLayoutManager(layoutManager);
        orderMenuRecyclerView.setAdapter(orderKindMenuCardAdapter);


    }

}
