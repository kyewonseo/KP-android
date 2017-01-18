package net.bluehack.kiosk;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CarouselView carouselView;
    private int[] carouselImgs = {R.drawable.earth, R.drawable.mars, R.drawable.mercury, R.drawable.neptune};

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView orderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);

        orderBtn = (ImageView) findViewById(R.id.order_btn);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        carouselView = (CarouselView) findViewById(R.id.carousel_view);
        recyclerView = (RecyclerView) findViewById(R.id.recent_menu_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //TODO: 서버에서 받아 올 cardItemList
        String[] cardItemList = {"itemList01","itemList02"};
        cardAdapter = new CardAdapter(context, cardItemList);
        recyclerView.setAdapter(cardAdapter);

        carouselView.setPageCount(carouselImgs.length);
        carouselView.setSlideInterval(2000);
        carouselView.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View carouselListItem = getLayoutInflater().inflate(R.layout.carousel_list_item, null);
                ImageView carouselItemImg = (ImageView) carouselListItem.findViewById(R.id.carousel_item_img);

                carouselItemImg.setImageResource(carouselImgs[position]);
                carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

                return carouselListItem;
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_item_01:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_item_02:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_item_03:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_sub_menu_item_01:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_sub_menu_item_02:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                }

                return true;
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // context.startActivities(new Intent[]{intent});
            }
        });

    }

    /** 알람 메시지 표시 자리 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.push_alarm:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
