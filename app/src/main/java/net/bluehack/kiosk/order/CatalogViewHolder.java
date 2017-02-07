package net.bluehack.kiosk.order;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class CatalogViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView name;

    public CatalogViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.catalog_list);
        this.name = (TextView) itemView.findViewById(R.id.txt_order_catalog_name);
    }
}
