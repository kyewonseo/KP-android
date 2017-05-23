package net.bluehack.kiosk.payment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class PaymentMenuRequiredOptionViewHolder extends RecyclerView.ViewHolder {

    CardView payment_menu_required_option_card_view;
    TextView payment_menu_required_option_tv_title;
    TextView payment_menu_required_option_price;

    public PaymentMenuRequiredOptionViewHolder(View itemView) {
        super(itemView);

        this.payment_menu_required_option_card_view = (CardView) itemView.findViewById(R.id.payment_menu_required_option_card_view);
        this.payment_menu_required_option_tv_title = (TextView) itemView.findViewById(R.id.payment_menu_required_option_tv_title);
        this.payment_menu_required_option_price = (TextView) itemView.findViewById(R.id.payment_menu_required_option_price);
    }
}
