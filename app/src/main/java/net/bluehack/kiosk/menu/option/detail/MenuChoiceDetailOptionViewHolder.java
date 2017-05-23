package net.bluehack.kiosk.menu.option.detail;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuChoiceDetailOptionViewHolder extends RecyclerView.ViewHolder {

    CardView option_choice_card_view;
    CheckBox option_choice_cb;
    TextView option_choice_tv_title;
    TextView option_choice_tv_price;

    public MenuChoiceDetailOptionViewHolder(View itemView) {
        super(itemView);

        this.option_choice_card_view = (CardView) itemView.findViewById(R.id.option_choice_card_view);
        this.option_choice_cb = (CheckBox) itemView.findViewById(R.id.option_choice_cb);
        this.option_choice_tv_title = (TextView) itemView.findViewById(R.id.option_choice_tv_title);
        this.option_choice_tv_price = (TextView) itemView.findViewById(R.id.option_choice_tv_price);
    }
}