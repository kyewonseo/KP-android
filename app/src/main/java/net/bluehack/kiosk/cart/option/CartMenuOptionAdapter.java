package net.bluehack.kiosk.cart.option;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;

import java.util.ArrayList;
import java.util.List;

public class CartMenuOptionAdapter extends RecyclerView.Adapter<CartMenuOptionViewHolder> {

    private static final String TAG = makeLogTag(CartMenuOptionAdapter.class);

    private Context context;
    private List<CartMenuOptionItem> list = new ArrayList<>();

    public CartMenuOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CartMenuOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_option_item, parent, false);
        return new CartMenuOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartMenuOptionViewHolder holder, final int position) {

        final CartMenuOptionItem cartMenuOptionItem = list.get(position);

        holder.cart_menu_option_tv_title.setText(cartMenuOptionItem.getMenu_option_name());
        holder.cart_menu_option_price.setText("+ $" + cartMenuOptionItem.getMenu_option_price());
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

    public void addItem(List<CartMenuOptionItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}