package net.bluehack.kiosk.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.bluehack.kiosk.R;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.OrderResDetail;

public class HistoryTicketDishAdapter extends BaseAdapter {

    Context context;
    List<OrderResDetail> menus = new ArrayList<>();

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        OrderResDetail menu = menus.get(position);

        ViewHolder viewHolder;

        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_dishes_name_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tvDishName = (TextView) convertView.findViewById(R.id.tvDishName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDishName.setText(menu.getDetailMenuName());

        return convertView;
    }

    public void setItem(List<OrderResDetail> menus) {

        if (menus != null) {
            this.menus.clear();
            this.menus = menus;
        }
    }

    public class ViewHolder {

        public TextView tvDishName;
    }
}
