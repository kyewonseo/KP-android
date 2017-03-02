package net.bluehack.kiosk.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.util.KioskPreference;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CartMenuAdapter extends RecyclerView.Adapter<CartMenuViewHolder>{

    private static final String TAG = makeLogTag(CartMenuAdapter.class);

    private Context context;
    private static List<CartMenuItem> list = new ArrayList<>();

    public CartMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CartMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_menu_item, parent, false);
        return new CartMenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartMenuViewHolder holder, final int position) {

        final CartMenuItem cartMenuItem = list.get(position);
        final List<CartMenuOptionItem> cartMenuOptionItem = cartMenuItem.getCartMenuOptionItems();

        //cart menu option view
        RecyclerView.LayoutManager optionLayoutManager = new LinearLayoutManager(context);
        CartMenuOptionAdapter cartMenuOptionAdapter = new CartMenuOptionAdapter(context);
        holder.cart_option_rv_list.setHasFixedSize(true);
        holder.cart_option_rv_list.setLayoutManager(optionLayoutManager);
        holder.cart_option_rv_list.setAdapter(cartMenuOptionAdapter);

        cartMenuOptionAdapter.clean();
        cartMenuOptionAdapter.addItem(cartMenuOptionItem);
        cartMenuOptionAdapter.notifyDataSetChanged();

        Glide.with(context)
                .load(cartMenuItem.getMenu_image())
                .override(64,64)
                .placeholder(R.drawable.img_storelogo_default)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .into(holder.cart_menu_card_iv_img);

        holder.cart_menu_card_tv_title.setText(cartMenuItem.getMenu_name());
        holder.cart_menu_card_tv_price.setText(cartMenuItem.getMenu_price() + "$" + "(" + cartMenuItem.getMenu_point() + "P" + ")");
        holder.cart_menu_card_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                KioskPreference.getInstance().deleteCartInfo(position);
                addItem(KioskPreference.getInstance().getCartInfo());
                notifyDataSetChanged();

                ((Activity) context).finish();
                Intent intent = new Intent(context, CartMenuActivity.class);
                context.startActivity(intent);

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

    public void addItem(List<CartMenuItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
