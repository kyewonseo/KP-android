package net.bluehack.kiosk.store;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.bluehack.kiosk.R;
import net.bluehack.kiosk.api.net.NetworkManager;
import net.bluehack.kiosk.cart.CartMenuActivity;
import net.bluehack.kiosk.cart.vo.CartMenuItem;
import net.bluehack.kiosk.home.HomeActivity;
import net.bluehack.kiosk.log.GaCategory;
import net.bluehack.kiosk.log.LogEventTracker;
import net.bluehack.kiosk.payment.PaymentWebView;
import net.bluehack.kiosk.popup.KioskPopup;
import net.bluehack.kiosk.util.KioskPreference;
import net.bluehack.kiosk.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.OrderReq;
import io.swagger.client.model.StoresResData;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private static final String TAG = makeLogTag(StoreAdapter.class);

    private Context context;
    private List<StoresResData> list = new ArrayList<>();

    public StoreAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_place_item, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StoreViewHolder holder, int position) {

        final StoresResData storeItem = list.get(position);

        Glide.with(context)
                .load(storeItem.getSLogo())
                .override(64, 64)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.img_storelogo_default)
                .centerCrop()
                .into(holder.imageView);
        holder.name.setText(storeItem.getStore());
        holder.address.setText(storeItem.getSAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LOGE(TAG, "store click!");
                LogEventTracker.TapStoreEvent(GaCategory.STORE, storeItem.getStore());

                final KioskPopup popup = new KioskPopup(context);
                popup.show();
                TextView tvTitle = (TextView) popup.getContentView().findViewById(R.id.tvTitle);
                ImageView ivImage = (ImageView) popup.getContentView().findViewById(R.id.ivImage);
                TextView tvMessage = (TextView) popup.getContentView().findViewById(R.id.tvMessage);
                TextView tvSubMessage
                        = (TextView) popup.getContentView().findViewById(R.id.tvSubMessage);
                final TextView tvLeftButton
                        = (TextView) popup.getContentView().findViewById(R.id.tvLeftButton);
                final TextView tvRightButton
                        = (TextView) popup.getContentView().findViewById(R.id.tvRightButton);

                ivImage.setBackground(UiUtil.getDrawable(context, R.drawable.img_popup_store));
                tvTitle.setText(context.getResources().getString(R.string.popup_select_store_title));
                tvMessage.setText(context.getResources().getString(R.string.popup_select_store_message));

                String sub_message = storeItem.getStore();
                String sub_end = context.getResources().getString(R.string.popup_select_store_sub);
                Spannable sp = new SpannableString(sub_message + sub_end);
                sp.setSpan(new ForegroundColorSpan(Color.RED), 0, sub_message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSubMessage.setText(sp);

                LogEventTracker.OpenPopupEvent(GaCategory.POP_UP,
                        context.getResources().getString(R.string.popup_select_store_title));

                tvLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_select_store_title),
                                tvLeftButton.getText().toString());
                        popup.dismiss();
                    }
                });
                tvRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LogEventTracker.ClosePopupEvent(GaCategory.POP_UP,
                                context.getResources().getString(R.string.popup_select_store_title),
                                tvRightButton.getText().toString());
                        popup.dismiss();

                        //clear cart info
                        if (KioskPreference.getInstance().getCartInfo() != null) {
                            KioskPreference.getInstance().clearCartInfo();
                        }

                        KioskPreference.getInstance().setStoreInfo(storeItem);
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
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

    public void addItem(List<StoresResData> itemList) {
        if (list instanceof ArrayList) {
            list = itemList;
        } else {
            LOGE(TAG, "error handler");
        }
    }
}
