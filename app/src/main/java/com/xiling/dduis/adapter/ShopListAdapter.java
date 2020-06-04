package com.xiling.dduis.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.manager.ShopCardManager;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ShopUtils;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.ddui.view.AutoLayoutManager;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.MainActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Key;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends BaseQuickAdapter<HomeRecommendDataBean.DatasBean, BaseViewHolder> {

    ShopListTagsAdapter tagsAdapter;

    public ShopListAdapter(int layoutResId, @Nullable List<HomeRecommendDataBean.DatasBean> data) {
        super(layoutResId, data);
        EventBus.getDefault().register(this);
    }


    private void setImageView(Bitmap myBitmap, ImageView imageView) {
        int imageWidth = myBitmap.getWidth();
        int imageHeight = myBitmap.getHeight();
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int height = ScreenUtils.dip2px(mContext, 14);
        int width = (int) (imageWidth / (Double.valueOf(imageHeight) / Double.valueOf(height)));
        para.height = height;
        para.width = width;
        imageView.setLayoutParams(para);
        imageView.setImageBitmap(myBitmap);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeRecommendDataBean.DatasBean item) {
        if (!TextUtils.isEmpty(item.getBadgeImg())) {
            helper.setVisible(R.id.iv_bgdge,true);
            GlideUtils.getBitmap(mContext, item.getBadgeImg(), new GlideUtils.OnBitmapGet() {

                @Override
                public void getBitmap(Bitmap bitmap) {
                    if (bitmap != null) {
                        setImageView(bitmap, (ImageView) helper.getView(R.id.iv_bgdge));
                    }
                }
            });
        }else{
            helper.setVisible(R.id.iv_bgdge,false);
        }

        if (!TextUtils.isEmpty(item.getThumbUrl())) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_thumb), item.getThumbUrl());
        }

        if (!TextUtils.isEmpty(item.getProductName())) {
            helper.setText(R.id.tv_title, item.getProductName());
        }
        List<String> tags = new ArrayList<>();
        tags.add(item.getTradeType());

        RecyclerView recyclerView = helper.getView(R.id.recycler_tags);
        AutoLayoutManager autoLayoutManager = new AutoLayoutManager();
        autoLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(autoLayoutManager);
        tagsAdapter = new ShopListTagsAdapter(R.layout.item_shop_list_tag, tags);
        recyclerView.setAdapter(tagsAdapter);

        //优惠价，需要根据用户等级展示不同价格
        NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(item), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
        String mStatus = ShopUtils.checkShopStatus(item.getStatus(), item.getStock());
        helper.getView(R.id.iv_go_card).setEnabled(TextUtils.isEmpty(mStatus));
        if (!TextUtils.isEmpty(mStatus)) {
            helper.setText(R.id.tv_status, mStatus);
            helper.setVisible(R.id.tv_status, true);
            helper.setBackgroundRes(R.id.iv_rate,UserManager.getInstance().getDiscountIconForUser(false));
            helper.setTextColor(R.id.tv_rmb, Color.parseColor("#999999"));
            helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#999999"));
            helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#999999"));
            //   helper.setTextColor(R.id.tv_minPrice, Color.parseColor("#999999"));
        } else {
            helper.setVisible(R.id.tv_status, false);
            helper.setBackgroundRes(R.id.iv_rate,UserManager.getInstance().getDiscountIconForUser(true));
            helper.setTextColor(R.id.tv_rmb, Color.parseColor("#a6251a"));
            helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#a6251a"));
            helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#a6251a"));
            // helper.setTextColor(R.id.tv_minPrice, Color.parseColor("#202020"));
        }

        //售价
        helper.setText(R.id.tv_minPrice, "¥" + NumberHandler.reservedDecimalFor2(item.getMinPrice()));
        //划线价
        TextView minMarketPriceView = helper.getView(R.id.tv_minMarketPrice);
        minMarketPriceView.setText("¥" + NumberHandler.reservedDecimalFor2(item.getMinMarketPrice()));
        minMarketPriceView.getPaint().setAntiAlias(true);//抗锯齿
        minMarketPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtil.setViewClickedDelay(view);
                Intent intent = new Intent(mContext, DDProductDetailActivity.class);
                intent.putExtra(Key.SPU_ID, item.getProductId());
                mContext.startActivity(intent);
            }
        });

        helper.setOnClickListener(R.id.btn_go_card, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserManager.getInstance().isLogin(mContext)) {
                    if (item.getStock() > 0) {
                        ShopCardManager.getInstance().requestAddCart(mContext, item.getSkuId(), item.getStep(), false, new BaseRequestListener() {
                            @Override
                            public void onSuccess(Object result) {
                                item.setStock(item.getStock() - 1);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
                    } else {
                        ToastUtil.error("该商品已售罄");
                    }
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(EventMessage message) {
        switch (message.getEvent()) {
            case LOGIN_SUCCESS:
            case LOGIN_OUT:
                this.notifyDataSetChanged();
                break;

        }
    }

}
