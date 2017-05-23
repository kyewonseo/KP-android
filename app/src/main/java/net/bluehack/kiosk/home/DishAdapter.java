package net.bluehack.kiosk.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.swagger.client.model.OrderResDetail;

import static net.bluehack.kiosk.util.Logger.LOGE;

public class DishAdapter extends BaseAdapter {

    /**
     * home의 recent order에서 보이는 이미지와 이미지 갯수 페이지 */
    Context context;
    //List<Menu> menus = new ArrayList<>();
    List<OrderResDetail> menus = new ArrayList<>();

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MenuViewHolder viewHolder;
        context = parent.getContext();

        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dish_item, parent, false);

            viewHolder = new MenuViewHolder();
            viewHolder.ivMenu = (CircleImageView) convertView.findViewById(R.id.ivMenu);
            viewHolder.tvMenuName = (TextView) convertView.findViewById(R.id.tvMenuName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuViewHolder) convertView.getTag();
        }

        OrderResDetail menu = menus.get(position);

        final String url = UiUtil.getFileImgUrl();
        int menuCount = 0;
        Glide.with(context)
                .load(url + menu.getDetailFileId())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .error(R.drawable.img_menu_sample_a_default)
                .into(viewHolder.ivMenu);

        if (menu.getDetailQuantity() != null) {
            menuCount = menu.getDetailQuantity();
        }
        viewHolder.tvMenuName.setText(String.valueOf(menuCount));

        return convertView;
    }

    public void clean() {
        if (menus != null) {
            menus.clear();
        }
    }

    public void addTicket(OrderResDetail menu) {
        this.menus.add(menu);
    }
    public void add(List<OrderResDetail> menus) {
        this.menus = menus;
    }

    public void addList(List<OrderResDetail> menus) {
        this.menus.clear();
        if (menus == null) {
            return;
        }
        this.menus = menus;
    }

    public class MenuViewHolder {
        public CircleImageView ivMenu;
        public TextView tvMenuName;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}