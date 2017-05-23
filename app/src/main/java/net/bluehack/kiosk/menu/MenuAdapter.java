package net.bluehack.kiosk.menu;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.menu.option.MenuOptionActivity;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.MenuResData;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private static final String TAG = makeLogTag(MenuAdapter.class);

    private Context context;
    private List<MenuResData> list = new ArrayList<>();

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

        final MenuResData menuResDataItem = list.get(position);
        final String url = UiUtil.getFileImgUrl();
        //final String url = "http://lorempixel.com/600/400/city/";

        Glide.with(context)
                .load(url + menuResDataItem.getFileId())
                .override(64, 64)
                .centerCrop()
                //.placeholder(R.drawable.img_storelogo_default)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_b_default)
                .into(holder.menuImage);
        holder.name.setText(menuResDataItem.getMItem());
        holder.price.setText("$ " + String.valueOf(menuResDataItem.getPrice()));
        holder.calory.setText(String.valueOf(menuResDataItem.getCalory()) + "kcal");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.TapMenuEvent(GaCategory.MENU, menuResDataItem.getMItem());
                Intent intent = new Intent(context, MenuOptionActivity.class);
                Gson gson = new Gson();
                String menuInfo = gson.toJson(menuResDataItem);
                intent.putExtra("menu_info", menuInfo);

                context.startActivity(intent);
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
        if (list != null) {
            list.clear();
        }
    }

    public void addItem(List<MenuResData> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
