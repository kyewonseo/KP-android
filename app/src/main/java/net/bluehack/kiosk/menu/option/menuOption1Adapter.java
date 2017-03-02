package net.bluehack.kiosk.menu.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.CartMenuRequireOptionItem;
import net.bluehack.kiosk.model.MenuOptionResDataItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuOption1Adapter extends RecyclerView.Adapter<MenuOption1ViewHolder>{

    private static final String TAG = makeLogTag(MenuOption1Adapter.class);

    private Context context;
    private static List<MenuOptionResDataItem> list = new ArrayList<>();
    private MenuOptionActivity menuOptionActivity = new MenuOptionActivity();

    public MenuOption1Adapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuOption1ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option1_item, parent, false);
        return new MenuOption1ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuOption1ViewHolder holder, final int position) {

        final HashMap<Integer, ArrayList<CartMenuRequireOptionItem>> requiredMap = new HashMap<Integer, ArrayList<CartMenuRequireOptionItem>>();
        final ArrayList<CartMenuRequireOptionItem> cartMenuRequireOptionList = new ArrayList<CartMenuRequireOptionItem>();
        final CartMenuRequireOptionItem cartMenuRequireOptionItem = new CartMenuRequireOptionItem();
        final MenuOptionResDataItem menuOption1ResDataItem = list.get(position);

        final String menuRequiredOptionId = menuOption1ResDataItem.getMenuId();
        final String menuRequiredOptionName = menuOption1ResDataItem.getMItem();
        final String menuRequiredOptionPrice = menuOption1ResDataItem.getPrice();

        holder.menu_option1_tv.setText(menuRequiredOptionName);

        if (position == 0) {
            holder.radioButton.setChecked(true);
        }
        /*holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    holder.radioButton.setChecked(true);

                    cartMenuRequireOptionItem.setMenu_rq_option_id(menuRequiredOptionId);
                    cartMenuRequireOptionItem.setMenu_rq_option_name(menuRequiredOptionName);
                    cartMenuRequireOptionItem.setMenu_rq_option_price(menuRequiredOptionPrice);

                    cartMenuRequireOptionList.add(cartMenuRequireOptionItem);
                    requiredMap.put(position, cartMenuRequireOptionList);

                }else {
                    holder.radioButton.setChecked(false);
                    requiredMap.remove(position);
                }

                menuOptionActivity.setMenuRequiredOptionMap(requiredMap);
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
