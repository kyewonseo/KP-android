package net.bluehack.kiosk.menu;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView name;
    TextView price;
    ImageView menuImage;
    ImageView imageBtn;

    public MenuViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.menu_card_view);
        this.name = (TextView) itemView.findViewById(R.id.menu_cv_text_name);
        this.price = (TextView) itemView.findViewById(R.id.menu_cv_text_price);
        this.menuImage = (ImageView) itemView.findViewById(R.id.menu_iv_img);
        this.imageBtn = (ImageView) itemView.findViewById(R.id.menu_cv_iv_btn);
    }
}
