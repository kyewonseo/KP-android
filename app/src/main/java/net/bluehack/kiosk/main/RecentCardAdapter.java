package net.bluehack.kiosk.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import net.bluehack.kiosk.R;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class RecentCardAdapter extends RecyclerView.Adapter<RecentCardViewHolder>{

    private static final String TAG = makeLogTag(RecentCardAdapter.class);
    private Context context;
    private final int img = R.drawable.btn_recent_menu_sample_01_nor;

    public RecentCardAdapter(){
    }

    /*public RecentCardAdapter(Context context) {
        this.context = context;
    }*/

    @Override
    public RecentCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_menu_list_item, parent, false);
        return new RecentCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecentCardViewHolder holder, int position) {

        /*LOGE(TAG, "menuItems count :"+ arrayList.get(position));

        MenuDataItem menuItem = arrayList.get(position);
        holder.imageView.setBackground(ContextCompat.getDrawable(context, img));
        holder.title.setText(menuItem.getName());
        holder.kind.setText(menuItem.getPrice());*/
    }

    @Override
    public int getItemCount() {
        /*if(this.arrayList != null) {
            return this.arrayList.size();
        }else{
            return 0;
        }*/
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
