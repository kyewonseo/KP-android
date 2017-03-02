package net.bluehack.kiosk.cart;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class CartMenuViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    ImageView cart_menu_card_iv_img;
    ImageView cart_menu_card_iv_delete;
    TextView cart_menu_card_tv_title;
    TextView cart_menu_card_tv_price;
    RecyclerView cart_option_rv_list;

    public CartMenuViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.cart_menu_card_view);
        this.cart_menu_card_iv_img = (ImageView) itemView.findViewById(R.id.cart_menu_card_iv_img);
        this.cart_menu_card_iv_delete = (ImageView) itemView.findViewById(R.id.cart_menu_card_iv_delete);
        this.cart_menu_card_tv_title = (TextView) itemView.findViewById(R.id.cart_menu_card_tv_title);
        this.cart_menu_card_tv_price = (TextView) itemView.findViewById(R.id.cart_menu_card_tv_price);
        this.cart_option_rv_list = (RecyclerView) itemView.findViewById(R.id.cart_option_rv_list);
    }
}
