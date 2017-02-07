package net.bluehack.kiosk.order;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.model.Menu;
import net.bluehack.kiosk.model.MenuDataItem;

import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogViewHolder> {

    private static final String TAG = makeLogTag(CatalogAdapter.class);
    private Context context;
    private static final String methodQuery = "bmod";
    private static ArrayList<String> list = new ArrayList<String>();
    private MenuItemAdapter menuItemAdapter;

    public CatalogAdapter(Context context, MenuItemAdapter menuItemAdapter) {
        this.context = context;
        this.menuItemAdapter = menuItemAdapter;

        list.add(context.getResources().getString(R.string.txt_order_catalog_name_01));
        list.add(context.getResources().getString(R.string.txt_order_catalog_name_02));
        list.add(context.getResources().getString(R.string.txt_order_catalog_name_03));
        list.add(context.getResources().getString(R.string.txt_order_catalog_name_04));
    }

    @Override
    public CatalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_catalog_item, parent, false);
        return new CatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CatalogViewHolder holder, int position) {

        String item = list.get(position);
        holder.name.setText(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call catalog menu items
                ApiClient.getInstance().menuGet(methodQuery, new ApiClient.ApiResponseListener() {
                    @Override
                    public void onResponse(final Object result) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Menu menu = (Menu) result;

                                        if (menu.getSuccess()) {

                                            menuItemAdapter.clean();

                                            ArrayList<MenuDataItem> menuItems = new ArrayList<>();

                                            for (MenuDataItem item : menu.getData()) {

                                                MenuDataItem menuDataItem = new MenuDataItem();
                                                menuDataItem.setName(item.getName());
                                                menuDataItem.setPrice(item.getPrice());

                                                menuItems.add(menuDataItem);
                                            }
                                            menuItemAdapter.addItem(menuItems);
                                            menuItemAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }).start();

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }
}
