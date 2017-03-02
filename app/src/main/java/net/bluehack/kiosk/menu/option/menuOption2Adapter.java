package net.bluehack.kiosk.menu.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.CartMenuOptionItem;
import net.bluehack.kiosk.menu.MenuActivity;
import net.bluehack.kiosk.model.MenuOptionResDataItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuOption2Adapter extends RecyclerView.Adapter<MenuOption2ViewHolder>{

    private static final String TAG = makeLogTag(MenuOption2Adapter.class);

    private Context context;
    private static List<MenuOptionResDataItem> list = new ArrayList<>();
    private MenuOptionActivity menuOptionActivity = new MenuOptionActivity();

    public MenuOption2Adapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuOption2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option2_item, parent, false);
        return new MenuOption2ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuOption2ViewHolder holder, final int position) {

        final HashMap<Integer, ArrayList<CartMenuOptionItem>> map = new HashMap<Integer, ArrayList<CartMenuOptionItem>>();
        final ArrayList<CartMenuOptionItem> cartMenuOptionList = new ArrayList<CartMenuOptionItem>();
        final CartMenuOptionItem cartMenuOptionItem = new CartMenuOptionItem();

        final MenuOptionResDataItem menuOption2ResDataItem = list.get(position);
        final String menuOptionId = menuOption2ResDataItem.getMenuId();
        final String menuOptionName = menuOption2ResDataItem.getMItem();
        final String menuOptionPrice = menuOption2ResDataItem.getPrice();

        holder.menu_option2_tv.setText(menuOptionName);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkBox.setChecked(true);
                    cartMenuOptionItem.setMenu_option_id(menuOptionId);
                    cartMenuOptionItem.setMenu_option_name(menuOptionName);
                    cartMenuOptionItem.setMenu_option_price(menuOptionPrice);

                    cartMenuOptionList.add(cartMenuOptionItem);
                    map.put(position, cartMenuOptionList);

                }else {
                    holder.checkBox.setChecked(false);
                    map.remove(position);
                }

                menuOptionActivity.setMenuOptionMap(map);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
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