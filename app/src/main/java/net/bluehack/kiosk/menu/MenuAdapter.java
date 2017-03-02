package net.bluehack.kiosk.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.menu.option.MenuOptionActivity;
import net.bluehack.kiosk.model.MenuResDataItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder>{

    private static final String TAG = makeLogTag(MenuAdapter.class);

    private Context context;
    private static List<MenuResDataItem> list = new ArrayList<>();

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {

        final MenuResDataItem menuResDataItem = list.get(position);

        //TODO: fixme => file server check : http://122.199.152.194:8080/var/kiosk/files/images/ice.jpg
        final String url = menuResDataItem.getServer()+menuResDataItem.getVolume()+menuResDataItem.getPath()+menuResDataItem.getName();
        Glide.with(context)
                .load(url)
                .override(64,64)
                .placeholder(R.drawable.img_storelogo_default)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .into(holder.menuImage);

        holder.name.setText(menuResDataItem.getMItem());
        holder.price.setText(menuResDataItem.getPrice() + "$");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MenuOptionActivity.class);
                intent.putExtra("menu_id", menuResDataItem.getMenuId());
                intent.putExtra("menu_name", menuResDataItem.getMItem());
                intent.putExtra("menu_price", menuResDataItem.getPrice());
                intent.putExtra("menu_point", menuResDataItem.getPoints());
                intent.putExtra("menu_image", url);
                intent.putExtra("menu_description", menuResDataItem.getDescription());
                intent.putExtra("menu_calory", menuResDataItem.getCalory());

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

    public void addItem(List<MenuResDataItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
