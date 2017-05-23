package net.bluehack.kiosk.payment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    CircleImageView payment_menu_card_iv_img;
    TextView payment_menu_card_tv_title;
    TextView payment_menu_card_tv_price;
    RecyclerView payment_required_option_rv_list;
    RecyclerView payment_option_rv_list;

    public PaymentViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.payment_menu_card_view);
        this.payment_menu_card_iv_img = (CircleImageView) itemView.findViewById(R.id.payment_menu_card_iv_img);
        this.payment_menu_card_tv_title = (TextView) itemView.findViewById(R.id.payment_menu_card_tv_title);
        this.payment_menu_card_tv_price = (TextView) itemView.findViewById(R.id.payment_menu_card_tv_price);
        this.payment_required_option_rv_list = (RecyclerView) itemView.findViewById(R.id.payment_required_option_rv_list);
        this.payment_option_rv_list = (RecyclerView) itemView.findViewById(R.id.payment_option_rv_list);
    }
}
