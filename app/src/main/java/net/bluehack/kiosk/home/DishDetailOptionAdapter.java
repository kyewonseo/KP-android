package net.bluehack.kiosk.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.bluehack.kiosk.R;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.OrderResOption;

public class DishDetailOptionAdapter extends BaseAdapter {

    Context context;
    private List<OrderResOption> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        OrderResOption option = list.get(position);

        ViewHolder viewHolder;

        String optionPrice = "0";

        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.dish_detail_option_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvOption = (TextView) convertView.findViewById(R.id.tvOption);
            viewHolder.tvOption_price = (TextView) convertView.findViewById(R.id.tvOption_price);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (option.getOptionPrice() != null) {
            optionPrice = String.valueOf(option.getOptionPrice());
        }
        viewHolder.tvOption.setText(option.getOptionMenuName());
        viewHolder.tvOption_price.setText("+ $" + optionPrice);

        return convertView;
    }

    public void setList(List<OrderResOption> list) {
        this.list.clear();
        if (list == null) {
            return;
        }
        this.list = list;
    }

    public class ViewHolder {
        TextView tvOption;
        TextView tvOption_price;
    }
}
