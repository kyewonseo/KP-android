package net.bluehack.kiosk.menu.option.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.menu.option.MenuOptionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.swagger.client.model.MenuOptionResData;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuChoiceDetailOptionAdapter extends RecyclerView.Adapter<MenuChoiceDetailOptionViewHolder> {

    private static final String TAG = makeLogTag(MenuChoiceDetailOptionAdapter.class);
    private Context context;
    private MenuOptionActivity menuOptionActivity = new MenuOptionActivity();
    private List<MenuOptionResData> list = new ArrayList<>();

    public MenuChoiceDetailOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuChoiceDetailOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option_choice_item, parent, false);
        return new MenuChoiceDetailOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuChoiceDetailOptionViewHolder holder, final int position) {

        MenuOptionResData menuOptionDetailItem = list.get(position);
        final String menuChoiceDetailOptionId = menuOptionDetailItem.getId();
        final String menuChoiceDetailOptionName = menuOptionDetailItem.getOption();
        final Number menuChoiceDetailOptionPrice = menuOptionDetailItem.getPrice();

        final CartMenuOptionItem cartMenuOptionItem = new CartMenuOptionItem();
        cartMenuOptionItem.setMenu_option_id(menuChoiceDetailOptionId);
        cartMenuOptionItem.setMenu_option_name(menuChoiceDetailOptionName);
        cartMenuOptionItem.setMenu_option_price(String.valueOf(menuChoiceDetailOptionPrice));
        cartMenuOptionItem.setChecked(false);

        holder.option_choice_tv_title.setText(menuChoiceDetailOptionName);
        holder.option_choice_tv_price.setText("$ " + String.valueOf(menuChoiceDetailOptionPrice));

        holder.option_choice_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox cb = (CheckBox) buttonView;
                if (cb.isChecked()) {
                    holder.option_choice_cb.setChecked(true);
                    cartMenuOptionItem.setChecked(true);

                    menuOptionActivity.setMenuOptionList(cartMenuOptionItem);
                    notifyDataSetChanged();
                } else {
                    holder.option_choice_cb.setChecked(false);
                    cartMenuOptionItem.setChecked(false);

                    menuOptionActivity.setMenuOptionList(cartMenuOptionItem);
                    notifyDataSetChanged();
                }

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
        if (list != null) {
            list.clear();
        }
    }

    public void addItem(List<MenuOptionResData> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }

    /*public List<CartMenuOptionItem> getMenuOptionList() {
        return optionList;
    }

    public void setMenuOptionList(CartMenuOptionItem optionItems) {
        if (optionItems.isChecked()) {
            optionList.add(optionItems);
        } else {
            optionList.remove(optionItems);
        }
    }*/

}
