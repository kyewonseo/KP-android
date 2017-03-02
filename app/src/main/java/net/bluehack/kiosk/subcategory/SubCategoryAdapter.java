package net.bluehack.kiosk.subcategory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.menu.MenuActivity;
import net.bluehack.kiosk.model.SubcategoryResDataItem;

import java.util.ArrayList;
import java.util.List;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryViewHolder>{

    private static final String TAG = makeLogTag(SubCategoryAdapter.class);

    private Context context;
    private static List<SubcategoryResDataItem> list = new ArrayList<>();

    public SubCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SubCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_item, parent, false);
        return new SubCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubCategoryViewHolder holder, int position) {

        final SubcategoryResDataItem subcategoryResDataItem = list.get(position);

        holder.name.setText(subcategoryResDataItem.getSubCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("sub_category_id", subcategoryResDataItem.getSubCategoryId());
                intent.putExtra("sub_category_name", subcategoryResDataItem.getSubCategoryName());
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

    public void addItem(List<SubcategoryResDataItem> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
