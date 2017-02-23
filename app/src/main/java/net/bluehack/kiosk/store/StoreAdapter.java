package net.bluehack.kiosk.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.main.MainActivity;
import net.bluehack.kiosk.model.StoresResDataItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder>{

    private static final String TAG = makeLogTag(StoreAdapter.class);

    private Context context;
    private static List<StoresResDataItem> list = new ArrayList<>();

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

        StoresResDataItem storeItem = list.get(position);

        Glide.with(context)
                .load(storeItem.getSLogo())
                .override(64,64)
                .placeholder(R.drawable.img_storelogo_default)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .into(holder.imageView);
        holder.name.setText(storeItem.getStore());
        holder.address.setText(storeItem.getSAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
                ((Activity) context).finish();
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

    public void addItem(List<StoresResDataItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
