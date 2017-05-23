package net.bluehack.kiosk.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.ApplicationLoader;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.cart.vo.CartMenuOptionItem;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.swagger.client.ApiClient;
import io.swagger.client.model.AddOrderReq;
import io.swagger.client.model.AddOrderReqDetail;
import io.swagger.client.model.AddOrderReqOption;
import io.swagger.client.model.AddOrderRes;
import io.swagger.client.model.OrderResDetail;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class DishDetailAdapter extends BaseAdapter {

    Context context;
    List<OrderResDetail> menus = new ArrayList<>();

    private static final String TAG = makeLogTag(DishDetailAdapter.class);
    private List<AddOrderReqDetail> orderList = null;

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

        ViewHolder viewHolder;
        final OrderResDetail menu = menus.get(position);

        context = parent.getContext();

        if (!(convertView instanceof View)) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dish_detail_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.ivDish = (CircleImageView) convertView.findViewById(R.id.tvDish);
            viewHolder.tvDishName = (TextView) convertView.findViewById(R.id.tvDishName);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.lvOptions = (ListView) convertView.findViewById(R.id.lvOptions);
            viewHolder.ivAddCart = (ImageView) convertView.findViewById(R.id.ivAddCart);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fixme : order에서 menu option type 꼭 필요함.
        /*viewHolder.ivDish.setBackground(
                UiUtil.getDrawable(context, R.drawable.img_menu_sample_a_05));*/
        final String url = UiUtil.getFileImgUrl();
        Glide.with(context)
                .load(url + menu.getDetailFileId())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_menu_sample_a_default)
                .into(viewHolder.ivDish);

        viewHolder.tvDishName.setText(menu.getDetailMenuName());
        viewHolder.tvPrice.setText(String.valueOf("$ " + String.valueOf(menu.getDetailPrice())));
        viewHolder.ivAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartMenuItem cartMenuItem = new CartMenuItem();
                List<CartMenuOptionItem> cartMenuOptionList = new ArrayList<CartMenuOptionItem>();
                cartMenuItem.setMenu_id(menu.getDetailMenuId());
                cartMenuItem.setMenu_image(menu.getDetailFileId());
                cartMenuItem.setMenu_name(menu.getDetailMenuName());
                cartMenuItem.setMenu_price(String.valueOf(menu.getDetailPrice()));
                //cartMenuItem.setCartMenuOptionItems(menu.getOption());
                //TODO: fixme => 차후 /order/detail api에서 calory정보까지 받아오기
                //cartMenuItem.setMenu_calory();
                //cartMenuItem.setMenu_point();
                //cartMenuItem.setMenu_description();
                //menu.getOption().get(0).option_type
                //order num 받아오기

                //TODO: fixme => api가 현재 menu option type을 주지않아 테스트 코드
                for (int i = 0; i < menu.getOption().size(); i ++) {
                    CartMenuOptionItem cartMenuOptionItem = new CartMenuOptionItem();
                    cartMenuOptionItem.setMenu_option_id(menu.getOption().get(i).getOptionMenuId());
                    cartMenuOptionItem.setMenu_option_name(menu.getOption().get(i).getOptionMenuName());
                    cartMenuOptionItem.setMenu_option_price(String.valueOf(menu.getOption().get(i).getOptionPrice()));
                    cartMenuOptionList.add(cartMenuOptionItem);
                }
                cartMenuItem.setCartMenuOptionItems(cartMenuOptionList);

                KioskPreference.getInstance().setCartInfo(cartMenuItem);
                Toast.makeText(context, context.getResources().getString(R.string.add_cart) , Toast.LENGTH_SHORT).show();
                View menu_cart_tv = ((Activity)context).getWindow().findViewById(R.id.menu_cart_tv);
                menu_cart_tv.setVisibility(View.VISIBLE);

                LogEventTracker.TapAddCartEvent(GaCategory.ORDERHISTORYDETAIL, cartMenuItem);
            }
        });

        DishDetailOptionAdapter adapter = new DishDetailOptionAdapter();
        adapter.setList(menu.getOption());

        UiUtil.drawListViewFitHeight(viewHolder.lvOptions, adapter);
        viewHolder.lvOptions.setSelector(R.drawable.listview_non_selector);

        viewHolder.lvOptions.setAdapter(adapter);

        return convertView;
    }

    public void addList(List<OrderResDetail> menus) {
        this.menus.clear();
        if (menus == null) {
            return;
        }
        this.menus = menus;
    }

    public class ViewHolder {

        public ImageView ivDish;
        public ImageView ivAddCart;
        public TextView tvDishName;
        public TextView tvPrice;

        ListView lvOptions;

    }
}
