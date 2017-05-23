package net.bluehack.kiosk.cart.option;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class CartMenuRequiredOptionViewHolder extends RecyclerView.ViewHolder {

    CardView cart_menu_required_option_card_view;
    TextView cart_menu_required_option_tv_title;
    TextView cart_menu_required_option_price;

    public CartMenuRequiredOptionViewHolder(View itemView) {
        super(itemView);

        this.cart_menu_required_option_card_view = (CardView) itemView.findViewById(R.id.cart_menu_required_option_card_view);
        this.cart_menu_required_option_tv_title = (TextView) itemView.findViewById(R.id.cart_menu_required_option_tv_title);
        this.cart_menu_required_option_price = (TextView) itemView.findViewById(R.id.cart_menu_required_option_price);
    }
}