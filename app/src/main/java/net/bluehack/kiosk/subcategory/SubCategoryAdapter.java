package net.bluehack.kiosk.subcategory;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.menu.MenuActivity;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.SubcategoryResData;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryViewHolder> {

    private static final String TAG = makeLogTag(SubCategoryAdapter.class);

    private Context context;
    private static List<SubcategoryResData> list = new ArrayList<>();

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

        final SubcategoryResData subcategoryResDataItem = list.get(position);

        holder.name.setText(subcategoryResDataItem.getSubCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogEventTracker.TapCategoryEvent(GaCategory.CATEGORY, subcategoryResDataItem.getSubCategoryName());
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("sub_category_id", subcategoryResDataItem.getSubCategoryId());
                intent.putExtra("sub_category_name", subcategoryResDataItem.getSubCategoryName());
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

    public void addItem(List<SubcategoryResData> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
