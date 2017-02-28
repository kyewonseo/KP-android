package net.bluehack.kiosk.menu.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.model.MenuOptionResDataItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuOption1Adapter extends RecyclerView.Adapter<MenuOption1ViewHolder>{

    private static final String TAG = makeLogTag(MenuOption1Adapter.class);

    private Context context;
    private static List<MenuOptionResDataItem> list = new ArrayList<>();

    public MenuOption1Adapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuOption1ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option1_item, parent, false);
        return new MenuOption1ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuOption1ViewHolder holder, int position) {

        final MenuOptionResDataItem menuOption1ResDataItem = list.get(position);

        String mItem = menuOption1ResDataItem.getMItem();
        if (mItem == null) {
            mItem = "";
        }
        holder.menu_option1_tv.setText(mItem);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.radioButton.isChecked()) {
                    holder.radioButton.setChecked(false);
                }
                holder.radioButton.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void clean() {
        list.clear();
    }

    public void addItem(List<MenuOptionResDataItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }

}
