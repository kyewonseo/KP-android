package net.bluehack.kiosk;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.model.Menu;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder>{

    private static final String TAG = makeLogTag(CardAdapter.class);
    private Context context;

    //TODO: 서버에서 받아 올 recentMenuItem
    private ArrayList<RecentMenuItem> itemList;

    public CardAdapter(Context context) {
        this.context = context;

        itemList = new ArrayList<RecentMenuItem>();
        itemList.add(new RecentMenuItem("Dark Mocha\nFrappuccino","ice",R.drawable.btn_recent_menu_sample_01_nor));
        itemList.add(new RecentMenuItem("Cappuccino","ice",R.drawable.btn_recent_menu_sample_01_nor));
        itemList.add(new RecentMenuItem("Cold Brew","ice",R.drawable.btn_recent_menu_sample_01_nor));
        itemList.add(new RecentMenuItem("Mango\nBanana","ice",R.drawable.btn_recent_menu_sample_01_nor));
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_menu_list_item, null);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        //TODO: 서버에서 받아 올 ItemList
        final RecentMenuItem recentMenuItem = itemList.get(position);

        //holder.imageView.setBackground(ContextCompat.getDrawable(context,recentMenuItem.getImg()));
        holder.title.setText(recentMenuItem.getTitle());
        holder.kind.setText(recentMenuItem.getKind());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
        //return 0;
    }
}
