package net.bluehack.kiosk.cart;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class CartMenuOptionViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView cart_menu_option_tv_title;

    public CartMenuOptionViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.cart_menu_card_view);
        this.cart_menu_option_tv_title = (TextView) itemView.findViewById(R.id.cart_menu_option_tv_title);
    }
}