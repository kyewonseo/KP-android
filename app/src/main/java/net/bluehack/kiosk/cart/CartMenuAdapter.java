package net.bluehack.kiosk.cart;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.cart.option.CartMenuOptionAdapter;
import net.bluehack.kiosk.cart.option.CartMenuRequiredOptionAdapter;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.cart.vo.CartMenuRequireOptionItem;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

public class CartMenuAdapter extends RecyclerView.Adapter<CartMenuViewHolder> {

    private static final String TAG = makeLogTag(CartMenuAdapter.class);

    private Context context;
    private List<CartMenuItem> list = new ArrayList<>();

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
        final List<CartMenuRequireOptionItem> cartMenuRequireOptionItems = cartMenuItem.getCartMenuRequireOptionItems();

        //cart menu option view
        RecyclerView.LayoutManager optionLayoutManager = new LinearLayoutManager(context);
        RecyclerView.LayoutManager requiredOptionLayoutManager = new LinearLayoutManager(context);
        CartMenuOptionAdapter cartMenuOptionAdapter = new CartMenuOptionAdapter(context);
        CartMenuRequiredOptionAdapter cartMenuRequiredOptionAdapter = new CartMenuRequiredOptionAdapter(context);

        //cart choice option
        holder.cart_option_rv_list.setHasFixedSize(true);
        holder.cart_option_rv_list.setLayoutManager(optionLayoutManager);
        holder.cart_option_rv_list.setAdapter(cartMenuOptionAdapter);

        cartMenuOptionAdapter.clean();
        cartMenuOptionAdapter.addItem(cartMenuOptionItem);
        cartMenuOptionAdapter.notifyDataSetChanged();

        //cart required option
        holder.cart_required_option_rv_list.setHasFixedSize(true);
        holder.cart_required_option_rv_list.setLayoutManager(requiredOptionLayoutManager);
        holder.cart_required_option_rv_list.setAdapter(cartMenuRequiredOptionAdapter);

        cartMenuRequiredOptionAdapter.clean();
        cartMenuRequiredOptionAdapter.addItem(cartMenuRequireOptionItems);
        cartMenuRequiredOptionAdapter.notifyDataSetChanged();

        final String url = UiUtil.getFileImgUrl();
        Glide.with(context)
                .load(url + cartMenuItem.getMenu_image())
                .override(64, 64)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_a_default)
                .into(holder.cart_menu_card_iv_img);

        String point = "0";
        if (cartMenuItem.getMenu_point() != null) {
            point = cartMenuItem.getMenu_point();
        }
        holder.cart_menu_card_tv_title.setText(cartMenuItem.getMenu_name());
        holder.cart_menu_card_tv_price.setText("$ " + cartMenuItem.getMenu_price());
        holder.cart_menu_card_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KioskPreference.getInstance().deleteCartInfo(position);
                Toast.makeText(context, context.getResources().getString(R.string.cart_delete), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();

                LogEventTracker.RemoveBtnEvent(GaCategory.CART, cartMenuItem.getMenu_name());
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
