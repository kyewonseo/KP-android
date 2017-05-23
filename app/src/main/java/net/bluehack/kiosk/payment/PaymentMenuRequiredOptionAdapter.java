package net.bluehack.kiosk.payment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class PaymentMenuRequiredOptionAdapter extends RecyclerView.Adapter<PaymentMenuRequiredOptionViewHolder> {

    private static final String TAG = makeLogTag(PaymentMenuRequiredOptionAdapter.class);

    private Context context;
    private List<CartMenuRequireOptionItem> list = new ArrayList<>();

    public PaymentMenuRequiredOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PaymentMenuRequiredOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_required_option_item, parent, false);
        return new PaymentMenuRequiredOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PaymentMenuRequiredOptionViewHolder holder, final int position) {

        final CartMenuRequireOptionItem cartMenuOptionItem = list.get(position);

        holder.payment_menu_required_option_tv_title.setText(cartMenuOptionItem.getMenu_rq_option_name());
        holder.payment_menu_required_option_price.setText("+ $" + cartMenuOptionItem.getMenu_rq_option_price());
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
