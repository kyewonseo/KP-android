package net.bluehack.kiosk.store;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    //ImageView imageView;
    TextView name;
    TextView address;
    TextView meter;

    public StoreViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.store_card_view);
        //this.imageView = (ImageView) itemView.findViewById(R.id.store_);

        this.name = (TextView) itemView.findViewById(R.id.store_txt_name);
        this.address = (TextView) itemView.findViewById(R.id.store_txt_address);
        this.meter = (TextView) itemView.findViewById(R.id.store_txt_meter);
    }
}
