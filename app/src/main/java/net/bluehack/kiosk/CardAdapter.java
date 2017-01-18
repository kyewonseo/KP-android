package net.bluehack.kiosk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder>{

    private Context context;
    private String[] itemList;

    public CardAdapter(Context context, String[] itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_menu_list_item, null);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        //TODO: 서버에서 받아 올 ItemList
        //final String[] itemList = ItemList.get(position);

        //holder.img.setText(item.getName());
        //holder.title.setText(item.getTitle());
        //holder.kind.setText(item.getKind());
    }

    @Override
    public int getItemCount() {
        //return this.itemList.size();
        return 0;
    }
}
