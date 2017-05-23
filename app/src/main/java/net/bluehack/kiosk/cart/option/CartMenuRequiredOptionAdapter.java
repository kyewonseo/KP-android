package net.bluehack.kiosk.cart.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CartMenuRequiredOptionAdapter extends RecyclerView.Adapter<CartMenuRequiredOptionViewHolder> {

    private static final String TAG = makeLogTag(CartMenuRequiredOptionAdapter.class);

    private Context context;
    private List<CartMenuRequireOptionItem> list = new ArrayList<>();

    public CartMenuRequiredOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CartMenuRequiredOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_required_option_item, parent, false);
        return new CartMenuRequiredOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartMenuRequiredOptionViewHolder holder, final int position) {

        final CartMenuRequireOptionItem cartMenuRequireOptionItem = list.get(position);

        holder.cart_menu_required_option_tv_title.setText(cartMenuRequireOptionItem.getMenu_rq_option_name());
        holder.cart_menu_required_option_price.setText("+ $" + cartMenuRequireOptionItem.getMenu_rq_option_price());
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

    public void addItem(List<CartMenuRequireOptionItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}