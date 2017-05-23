package net.bluehack.kiosk.menu.option;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import net.bluehack.kiosk.R;

public class MenuOptionViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView option_tv_title;
    RecyclerView option_rv;

    public MenuOptionViewHolder(View itemView) {
        super(itemView);

        this.cardView = (CardView) itemView.findViewById(R.id.cardView);
        this.option_tv_title = (TextView) itemView.findViewById(R.id.option_tv_title);
        this.option_rv = (RecyclerView) itemView.findViewById(R.id.option_rv);
    }
}
