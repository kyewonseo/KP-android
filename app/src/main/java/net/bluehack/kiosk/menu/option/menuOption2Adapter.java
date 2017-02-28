package net.bluehack.kiosk.menu.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.model.MenuOptionResDataItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuOption2Adapter extends RecyclerView.Adapter<MenuOption2ViewHolder>{

    private static final String TAG = makeLogTag(MenuOption2Adapter.class);

    private Context context;
    private static List<MenuOptionResDataItem> list = new ArrayList<>();

    public MenuOption2Adapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuOption2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option2_item, parent, false);
        return new MenuOption2ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuOption2ViewHolder holder, int position) {

        final MenuOptionResDataItem menuOption2ResDataItem = list.get(position);

        String mItem = menuOption2ResDataItem.getMItem();
        if (mItem == null) {
            mItem = "";
        }
        holder.menu_option2_tv.setText(mItem);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkBox.setChecked(true);
                }else {
                    holder.checkBox.setChecked(false);
                }
            }
        });

        /*holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                }else {
                    holder.checkBox.setChecked(true);
                }
            }
        });*/
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