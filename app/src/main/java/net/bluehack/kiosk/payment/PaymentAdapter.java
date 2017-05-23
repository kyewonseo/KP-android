package net.bluehack.kiosk.payment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {

    private static final String TAG = makeLogTag(PaymentAdapter.class);

    private Context context;
    private List<CartMenuItem> list = new ArrayList<>();

    public PaymentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_menu_item, parent, false);
        return new PaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, final int position) {

        final CartMenuItem cartMenuItem = list.get(position);
        final List<CartMenuRequireOptionItem> requireOptionItems = cartMenuItem.getCartMenuRequireOptionItems();
        final List<CartMenuOptionItem> cartMenuOptionItem = cartMenuItem.getCartMenuOptionItems();

        //cart menu option view

        //required option
        RecyclerView.LayoutManager requiredOptionLayoutManager = new LinearLayoutManager(context);
        PaymentMenuRequiredOptionAdapter paymentMenuRequiredOptionAdapter = new PaymentMenuRequiredOptionAdapter(context);
        holder.payment_required_option_rv_list.setHasFixedSize(true);
        holder.payment_required_option_rv_list.setLayoutManager(requiredOptionLayoutManager);
        holder.payment_required_option_rv_list.setAdapter(paymentMenuRequiredOptionAdapter);

        paymentMenuRequiredOptionAdapter.clean();
        paymentMenuRequiredOptionAdapter.addItem(requireOptionItems);
        paymentMenuRequiredOptionAdapter.notifyDataSetChanged();

        //choice option
        RecyclerView.LayoutManager optionLayoutManager = new LinearLayoutManager(context);
        PaymentMenuOptionAdapter paymentMenuOptionAdapter = new PaymentMenuOptionAdapter(context);
        holder.payment_option_rv_list.setHasFixedSize(true);
        holder.payment_option_rv_list.setLayoutManager(optionLayoutManager);
        holder.payment_option_rv_list.setAdapter(paymentMenuOptionAdapter);

        paymentMenuOptionAdapter.clean();
        paymentMenuOptionAdapter.addItem(cartMenuOptionItem);
        paymentMenuOptionAdapter.notifyDataSetChanged();

        final String url = UiUtil.getFileImgUrl();
        Glide.with(context)
                .load(url + cartMenuItem.getMenu_image())
                .override(56, 56)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_a_default)
                .into(holder.payment_menu_card_iv_img);

        String point = "0";
        if (cartMenuItem.getMenu_point() != null) {
            point = cartMenuItem.getMenu_point();
        }
        holder.payment_menu_card_tv_title.setText(cartMenuItem.getMenu_name());
        holder.payment_menu_card_tv_price.setText("$ " + cartMenuItem.getMenu_price());

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

    public void addItem(List<CartMenuItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
