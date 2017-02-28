package net.bluehack.kiosk.menu.option;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuOption2ViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    CheckBox checkBox;
    TextView menu_option2_tv;

    public MenuOption2ViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.menu_option2_card_view);
        this.checkBox = (CheckBox) itemView.findViewById(R.id.menu_option2_cb);
        this.menu_option2_tv = (TextView) itemView.findViewById(R.id.menu_option2_tv);
    }
}
