package net.bluehack.kiosk.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.home.model.Ticket;
import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.swagger.client.model.OrderResData;

public class HistoryTicketAdapter extends BaseAdapter {

    Context context;
    List<OrderResData> tickets = new ArrayList<>();

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        context = parent.getContext();
        final OrderResData ticket = tickets.get(position);

        ViewHolder viewHolder;
        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llTicketMenu = (LinearLayout) convertView.findViewById(R.id.llTicketMenu);
            viewHolder.tvDish = (TextView) convertView.findViewById(R.id.tvDish);
            viewHolder.lvDishes = (ListView) convertView.findViewById(R.id.lvDishes);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvDish_img = (CircleImageView) convertView.findViewById(R.id.tvDish_img);
            viewHolder.tvDish_color = (CircleImageView) convertView.findViewById(R.id.tvDish_color);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.llTicketMenu.setBackgroundColor(UiUtil.getColor(context, R.color.color_03));

        Glide.with(context)
                .load(ticket.getDetail().get(0).getDetailFileId())
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_b_default)
                .into(viewHolder.tvDish_img);

        int cookStatus = 0;
        String cookStatusStr = "ing...";
        String date;

        if (ticket.getCookStatus() != null) {
            cookStatus = ticket.getCookStatus();

            if (cookStatus == Ticket.COMPLETE) {
                cookStatusStr = "Completed";
                viewHolder.tvDish.setTextSize(11);
                viewHolder.tvDish_color.setImageResource(R.color.color_48);
            } else {
                viewHolder.tvDish.setTextSize(13);
                viewHolder.tvDish_color.setImageResource(R.color.color_47);
            }
        }

        if (ticket.getRegidate() != null) {
            date = ticket.getRegidate();
        } else {
            date = "";
        }

        viewHolder.tvDish.setText(cookStatusStr);
        viewHolder.tvDate.setText(date);
        HistoryTicketDishAdapter adapter = new HistoryTicketDishAdapter();
        adapter.setItem(ticket.getDetail());
        viewHolder.lvDishes.setAdapter(adapter);
        UiUtil.drawListViewFitHeight(viewHolder.lvDishes, adapter);

        viewHolder.llTicketMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);

                LogEventTracker.OrderHistoryEvent(GaCategory.ORDERHISTORY, ticket.getOrderId());
                Intent intent = new Intent(context, TicketActivity.class);
                String orderInfo = UiUtil.toStringGson(tickets.get(position));
                intent.putExtra("clicked_order", orderInfo);

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addItem(OrderResData ticket) {
        tickets.add(ticket);
    }

    public void clean() {
        if (tickets != null) {
            tickets.clear();
        }
    }

    public class ViewHolder {

        LinearLayout llTicketMenu;
        TextView tvDish;
        ListView lvDishes;
        TextView tvDate;
        CircleImageView tvDish_img;
        CircleImageView tvDish_color;
    }
}
