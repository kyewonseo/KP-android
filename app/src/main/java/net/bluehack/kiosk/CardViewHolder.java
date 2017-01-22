package net.bluehack.kiosk;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    ImageView imageView;
    TextView title;
    TextView kind;

    public CardViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.recent_menu_card_view);
        this.imageView = (ImageView) itemView.findViewById(R.id.recent_menu_item_img);
        this.title = (TextView) itemView.findViewById(R.id.recent_menu_title);
        this.kind = (TextView) itemView.findViewById(R.id.recent_menu_kind);
    }
}
