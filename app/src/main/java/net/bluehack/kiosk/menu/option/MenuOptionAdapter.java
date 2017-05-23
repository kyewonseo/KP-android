package net.bluehack.kiosk.menu.option;

import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.menu.option.detail.MenuChoiceDetailOptionAdapter;
import net.bluehack.kiosk.menu.option.detail.MenuRequiredDetailOptionAdapter;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.MenuOptionRes;
import io.swagger.client.model.MenuOptionResData;
import io.swagger.client.model.MenuOptionResData1;
import io.swagger.client.model.MenuOptionResOptions;

public class MenuOptionAdapter extends RecyclerView.Adapter<MenuOptionViewHolder> {

    private static final String TAG = makeLogTag(MenuOptionAdapter.class);

    private Context context;
    private static List<MenuOptionResOptions> list = new ArrayList<>();

    public MenuOptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_option_item, parent, false);
        return new MenuOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MenuOptionViewHolder holder, final int position) {

        final MenuOptionResOptions menuOptionItems = list.get(position);
        final ArrayList<MenuOptionResData> requiredOptions = new ArrayList<MenuOptionResData>();
        final ArrayList<MenuOptionResData> choiceOptions = new ArrayList<MenuOptionResData>();

        final String menuRequiredOptionCategory = menuOptionItems.getCategoryName();

        RecyclerView.LayoutManager detailOptionLayoutManager = new LinearLayoutManager(context);
        MenuRequiredDetailOptionAdapter requiredAdapter = new MenuRequiredDetailOptionAdapter(context);
        MenuChoiceDetailOptionAdapter choiceAdapter = new MenuChoiceDetailOptionAdapter(context);


        /**
         * option Type
         * 1 = 단일 필수 (라디오 그룹중 오직 하나만)
         * 2 = 단일 선택 (체크박스 그룹중 하나는 필수 선택)
         * 3 = 다중 필수 (라디오 그룹중 적어도 하나는 필수)
         * 4 = 다중 선택 (체크박스 그룹중 선택 옵션)*/

        if (menuOptionItems.getOptionType().equals(1) || menuOptionItems.getOptionType().equals(2)) {

            for (int i = 0; i < menuOptionItems.getData().size(); i ++) {
                requiredOptions.add(menuOptionItems.getData().get(i));
            }
        } else if (menuOptionItems.getOptionType().equals(3) || menuOptionItems.getOptionType().equals(4)) {

            for (int i = 0; i < menuOptionItems.getData().size(); i ++) {
                choiceOptions.add(menuOptionItems.getData().get(i));
            }

        }

        if (requiredOptions.size() != 0) {
            holder.option_tv_title.setText(menuRequiredOptionCategory);
            holder.option_tv_title.setVisibility(View.VISIBLE);
            holder.option_rv.setLayoutManager(detailOptionLayoutManager);
            holder.option_rv.setAdapter(requiredAdapter);

            requiredAdapter.clean();
            requiredAdapter.addItem(requiredOptions);
            requiredAdapter.notifyDataSetChanged();
        }

        if (choiceOptions.size() != 0) {
            holder.option_tv_title.setText(menuRequiredOptionCategory);
            holder.option_tv_title.setVisibility(View.VISIBLE);
            holder.option_rv.setLayoutManager(detailOptionLayoutManager);
            holder.option_rv.setAdapter(choiceAdapter);

            choiceAdapter.clean();
            choiceAdapter.addItem(choiceOptions);
            choiceAdapter.notifyDataSetChanged();
        }


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

    public void addItem(List<MenuOptionResOptions> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }

    public List<MenuOptionResOptions> getList() {
        return list;
    }

}
