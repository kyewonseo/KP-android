/*
package net.bluehack.kiosk.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemViewHolder> {

    private static final String TAG = makeLogTag(MenuItemAdapter.class);
    private Context context;
    private ArrayList<MenuDataItem> list = new ArrayList<MenuDataItem>();


    public MenuItemAdapter() {
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_menu_item, parent, false);
        return new MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder holder, int position) {
        MenuDataItem item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }else {
            return 0;
        }
    }

    public void clean() {
        if (list != null && list.size() != 0) {
            list.clear();
        }
    }

    public void addItem(ArrayList<MenuDataItem> menuList) {
        if (menuList instanceof ArrayList) {
            list = menuList;
        }else {
            LOGE(TAG, "addItem error!");
        }
    }
}
*/
