package net.bluehack.kiosk.store;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    CircleImageView imageView;
    TextView name;
    TextView address;

    public StoreViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.store_card_view);
        this.imageView = (CircleImageView) itemView.findViewById(R.id.store_iv);
        this.name = (TextView) itemView.findViewById(R.id.store_text_name);
        this.address = (TextView) itemView.findViewById(R.id.store_text_address);
    }
}
