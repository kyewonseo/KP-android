package net.bluehack.kiosk.menu.option.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;
import net.bluehack.kiosk.menu.option.MenuOptionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.swagger.client.model.MenuOptionResData;
import io.swagger.client.model.MenuOptionResData1;
import io.swagger.client.model.MenuOptionResOptions;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuRequiredDetailOptionAdapter extends RecyclerView.Adapter<MenuRequiredDetailOptionViewHolder> {

    private static final String TAG = makeLogTag(MenuRequiredDetailOptionAdapter.class);
    private Context context;
    private MenuOptionActivity menuOptionActivity = new MenuOptionActivity();
    private List<MenuOptionResData> list = new ArrayList<>();
    //private static HashMap<Integer, CartMenuRequireOptionItem> requiredMap = new HashMap<Integer, CartMenuRequireOptionItem>();

    private RadioButton lastChecked = null;

    public MenuRequiredDetailOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuRequiredDetailOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option_required_item, parent, false);
        return new MenuRequiredDetailOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuRequiredDetailOptionViewHolder holder, final int position) {

        MenuOptionResData menuOptionRequiredItem = list.get(position);

        final String detailOptionId = menuOptionRequiredItem.getId();
        final String detailOptionName = menuOptionRequiredItem.getOption();
        final Number detailOptionPrice = menuOptionRequiredItem.getPrice();
        final CartMenuRequireOptionItem cartMenuRequireOptionItem = new CartMenuRequireOptionItem();

        cartMenuRequireOptionItem.setMenu_rq_option_id(detailOptionId);
        cartMenuRequireOptionItem.setMenu_rq_option_name(detailOptionName);
        cartMenuRequireOptionItem.setMenu_rq_option_price(String.valueOf(detailOptionPrice));
        cartMenuRequireOptionItem.setChecked(false);
        cartMenuRequireOptionItem.setRequiredChecked(false);


        holder.option_required_tv_title.setText(detailOptionName);
        holder.option_required_tv_price.setText("$ " + String.valueOf(detailOptionPrice));

        LOGE(TAG, "menuRequiredDetailOptionName:" + detailOptionPrice);

        if (position == 0) {
            lastChecked = holder.option_required_rb;
            lastChecked.setChecked(true);
            cartMenuRequireOptionItem.setChecked(true);
            menuOptionActivity.setMenuRequiredOptionMap(cartMenuRequireOptionItem);
        }

        holder.option_required_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                RadioButton rb = (RadioButton) v;

                if (rb.isChecked()) {
                    if (lastChecked.isChecked()) {
                        lastChecked.setChecked(false);
                    }

                    lastChecked = rb;
                    lastChecked.setChecked(true);
                    cartMenuRequireOptionItem.setChecked(true);
                    menuOptionActivity.setMenuRequiredOptionMap(cartMenuRequireOptionItem);

                } else {
                    lastChecked.setChecked(false);
                    cartMenuRequireOptionItem.setChecked(false);
                    menuOptionActivity.setMenuRequiredOptionMap(cartMenuRequireOptionItem);
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
        list.clear();
    }

    public void addItem(List<MenuOptionResData> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
