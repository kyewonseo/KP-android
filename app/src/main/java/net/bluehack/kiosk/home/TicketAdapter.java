package net.bluehack.kiosk.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.home.model.Ticket;
import net.bluehack.kiosk.home.ticket.TicketActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.OrderResData;

public class TicketAdapter extends BaseAdapter {

    Context context;
    //List<Ticket> tickets = new ArrayList<>();
    List<OrderResData> tickets = new ArrayList<>();

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= tickets.size()) {
            return null;
        }
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final TicketViewHolder viewHolder;
        final OrderResData ticket = tickets.get(position);

        context = parent.getContext();

        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ticket_item, parent, false);

            viewHolder = new TicketViewHolder();

            // mapping viewHolder
            viewHolder.flTicket = (FrameLayout) convertView.findViewById(R.id.flTicket);
            viewHolder.tvOrderDate = (TextView) convertView.findViewById(R.id.tvOrderDate);
            viewHolder.tvTicketPrice = (TextView) convertView.findViewById(R.id.tvTicketPrice);
            viewHolder.gvMenu = (GridView) convertView.findViewById(R.id.gvMenu);

            viewHolder.llProcessState = (LinearLayout) convertView.findViewById(R.id.llProcessState);

            viewHolder.tvOrderNo = (TextView) convertView.findViewById(R.id.tvOrderNo);

            viewHolder.ivPayment = (ImageView) convertView.findViewById(R.id.ivPayment);
            viewHolder.tvPayment = (TextView) convertView.findViewById(R.id.tvPayment);

            viewHolder.ivReceive = (ImageView) convertView.findViewById(R.id.ivReceive);
            viewHolder.tvReceive = (TextView) convertView.findViewById(R.id.tvReceive);

            viewHolder.ivMaking = (ImageView) convertView.findViewById(R.id.ivMaking);
            viewHolder.tvMaking = (TextView) convertView.findViewById(R.id.tvMaking);

            viewHolder.ivComplete = (ImageView) convertView.findViewById(R.id.ivComplete);
            viewHolder.tvComplete = (TextView) convertView.findViewById(R.id.tvComplete);

            viewHolder.viewStateLineToInCook = (View) convertView.findViewById(R.id.viewStateLineToInCook);
            viewHolder.viewStateLineToReady = (View) convertView.findViewById(R.id.viewStateLineToReady);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (TicketViewHolder) convertView.getTag();
        }

        viewHolder.tvOrderDate.setText(ticket.getRegidate());
        viewHolder.tvTicketPrice.setText("$ " + String.valueOf(ticket.getPrice()));

        //TODO: fixme => 현재 test api에서 cookstatus가 null로 들어옴 (홈화면에서 보이는 곳)
        // gridview 처리하기
        //if (ticket.getCookStatus() < Ticket.EXPIRE) {
        if (ticket.getCookStatus() == null) {
            ticket.setCookStatus(1);
        }

        viewHolder.tvPayment.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        viewHolder.tvReceive.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        viewHolder.tvMaking.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        viewHolder.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.color_34));
        viewHolder.viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.color.color_49));
        viewHolder.viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.color.color_49));

        if (ticket.getCookStatus() != null) {
            // showing process
            viewHolder.llProcessState.setVisibility(View.VISIBLE);
            switch (ticket.getCookStatus()) {
                case Ticket.PAYMENT:
                    viewHolder.ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    viewHolder.ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    viewHolder.ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    viewHolder.ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    viewHolder.tvPayment.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    break;
                case Ticket.RECEIVE:
                    viewHolder.ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    viewHolder.ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    viewHolder.ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    viewHolder.tvReceive.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    viewHolder.viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    viewHolder.viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    break;
                case Ticket.MAKING:
                    viewHolder.ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    viewHolder.ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));

                    viewHolder.tvMaking.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    viewHolder.viewStateLineToInCook.setBackground(UiUtil.getDrawable(context, R.color.color_49));
                    viewHolder.viewStateLineToReady.setBackground(UiUtil.getDrawable(context, R.drawable.img_state_line_dot));
                    break;
                case Ticket.COMPLETE:
                    viewHolder.ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_finish));
                    viewHolder.ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));

                    viewHolder.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.color_37));
                    break;

                default:
                    viewHolder.ivPayment.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_ing));
                    viewHolder.ivReceive.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    viewHolder.ivMaking.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    viewHolder.ivComplete.setBackground(
                            UiUtil.getDrawable(context, R.drawable.img_process_yet));
                    break;
            }
        } else {
            viewHolder.llProcessState.setVisibility(View.INVISIBLE);
        }

        int ticketOrderNum = 0;
        if (ticket.getOrderNum() != null) {
            ticketOrderNum = ticket.getOrderNum();
        }
        viewHolder.tvOrderNo.setText(String.valueOf(ticketOrderNum));

        DishAdapter adapter = new DishAdapter();
        /*List<OrderResDataItemDetailItem> dishList = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i ++) {
            for (int j = 0; j < tickets.get(i).getDetail().size(); j ++) {
                dishList.add(tickets.get(i).getDetail().get(j));
            }
        }*/


        adapter.clean();
        adapter.add(ticket.getDetail());
        //adapter.add(dishList);
        viewHolder.gvMenu.setAdapter(adapter);

        viewHolder.flTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 0);

                LogEventTracker.TapRecentOrderEvent(GaCategory.HOME, ticket.getOrderId());
                Intent intent = new Intent(context, TicketActivity.class);
                String orderInfo = UiUtil.toStringGson(tickets.get(position));
                intent.putExtra("clicked_order", orderInfo);

                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void add(List<OrderResData> tickets) {
        this.tickets = tickets;
    }
    public void addItem(List<OrderResData> tickets, TextView tvOtherView) {
        this.tickets = tickets;
        // Todo : 3개 초과의 경우 처리하기
        if (tickets.size() <= 3) {
            hideViewAll(tvOtherView);
        } else {
            showViewAll(tvOtherView);
        }
    }

    public void showViewAll(TextView tvOtherView) {
        tvOtherView.setVisibility(View.VISIBLE);
    }

    public void hideViewAll(TextView tvOtherView) {
        tvOtherView.setVisibility(View.INVISIBLE);
    }

    public void addItemTop(OrderResData ticket) {
        // Todo : 3개 초과의 경우 처리하기
        if (tickets.size() < 3) {
            tickets.add(0, ticket);
        } else {
            tickets.clear();
            tickets.add(ticket);
        }
    }

    public void clean() {
        if (tickets != null) {
            tickets.clear();
        }
    }


    public class TicketViewHolder {

        public FrameLayout flTicket;
        public TextView tvOrderDate;
        public TextView tvTicketPrice;
        public GridView gvMenu;
        // adapter

        public LinearLayout llProcessState;
        public TextView tvOrderNo;

        public ImageView ivPayment;
        public TextView tvPayment;

        public ImageView ivReceive;
        public TextView tvReceive;

        public ImageView ivMaking;
        public TextView tvMaking;

        public ImageView ivComplete;
        public TextView tvComplete;

        public View viewStateLineToInCook;
        public View viewStateLineToReady;
    }
}