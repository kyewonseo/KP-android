package net.bluehack.kiosk.menu.option;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuOption1ViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    RadioButton radioButton;
    TextView menu_option1_tv;

    public MenuOption1ViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.menu_option1_card_view);
        this.radioButton = (RadioButton) itemView.findViewById(R.id.menu_option1_rb);
        this.menu_option1_tv = (TextView) itemView.findViewById(R.id.menu_option1_tv);

    }
}
