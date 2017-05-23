package net.bluehack.kiosk.menu.option.detail;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuRequiredDetailOptionViewHolder extends RecyclerView.ViewHolder {

    CardView option_required_cardView;
    RadioButton option_required_rb;
    TextView option_required_tv_title;
    TextView option_required_tv_price;

    public MenuRequiredDetailOptionViewHolder(View itemView) {
        super(itemView);

        this.option_required_cardView = (CardView) itemView.findViewById(R.id.option_required_cardView);
        this.option_required_rb = (RadioButton) itemView.findViewById(R.id.option_required_rb);
        this.option_required_tv_title = (TextView) itemView.findViewById(R.id.option_required_tv_title);
        this.option_required_tv_price = (TextView) itemView.findViewById(R.id.option_required_tv_price);
    }
}