package net.bluehack.kiosk.order;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuItemViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView name;
    TextView price;

    public MenuItemViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.order_menu_card_view);
        this.name = (TextView) itemView.findViewById(R.id.txt_order_menu_name);
        this.price = (TextView) itemView.findViewById(R.id.txt_order_menu_price);
    }
}
