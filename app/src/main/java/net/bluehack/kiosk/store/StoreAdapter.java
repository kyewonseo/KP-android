package net.bluehack.kiosk.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder>{

    private static final String TAG = makeLogTag(StoreAdapter.class);

    private Context context;
    private static List<StoreItem> list = new ArrayList<>();

    public StoreAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_place_item, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {

        StoreItem storeItem = list.get(position);
        //holder.imageView.setBackground(ContextCompat.getDrawable(context, img));
        holder.name.setText(storeItem.getName());
        holder.address.setText(storeItem.getAddress());
        holder.meter.setText(storeItem.getMeter());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**Fixme: @k
                 * flow 추가시 map 선택으로 바뀔 예정 */

                /*Intent intent = new Intent(context, OrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void clean() {
        list.clear();
    }

    public void addItem(List<StoreItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
