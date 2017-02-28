package net.bluehack.kiosk.subcategory;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class SubCategoryViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView name;
    ImageView imageView;

    public SubCategoryViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.store_card_view);
        this.name = (TextView) itemView.findViewById(R.id.subcategory_cv_text_name);
        this.imageView = (ImageView) itemView.findViewById(R.id.subcategory_cv_iv_btn);
    }
}
