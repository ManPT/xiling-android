package com.xiling.ddui.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.IndexBrandBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ShopUtils;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;

import java.util.List;

public class IndexBrandShopAdapter extends BaseQuickAdapter<IndexBrandBean.IndexBrandBeanListBean, BaseViewHolder> {


    public IndexBrandShopAdapter(@Nullable List<IndexBrandBean.IndexBrandBeanListBean> data) {
        super(R.layout.item_index_brand_shop, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexBrandBean.IndexBrandBeanListBean item) {
        if (item != null) {
            if (!TextUtils.isEmpty(item.getThumbUrl())) {
                GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_thumb), item.getThumbUrl());
            }
            helper.setVisible(R.id.tv_rmb, true);
            if (!TextUtils.isEmpty(item.getShortName())) {
                helper.setText(R.id.tv_title, item.getShortName());
            }else {
                helper.setText(R.id.tv_title, item.getProductName());
            }
            //优惠价，需要根据用户等级展示不同价格
            NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(item), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
            //售价
            helper.setText(R.id.tv_minPrice, "¥" + NumberHandler.reservedDecimalFor2(item.getMinPrice()));
            //划线价
            TextView minMarketPriceView = helper.getView(R.id.tv_minMarketPrice);
            minMarketPriceView.setText("¥" + NumberHandler.reservedDecimalFor2(item.getMinMarketPrice()));
            minMarketPriceView.getPaint().setAntiAlias(true);//抗锯齿
            minMarketPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            String mStatus = ShopUtils.checkShopStatus(item.getStatus(), item.getStock());
            if (!TextUtils.isEmpty(mStatus)) {
                helper.setText(R.id.tv_status, mStatus);
                helper.setVisible(R.id.tv_status, true);
                helper.setTextColor(R.id.tv_rmb, Color.parseColor("#999999"));
                helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#999999"));
                helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#999999"));
                //   helper.setTextColor(R.id.tv_minPrice, Color.parseColor("#999999"));
            } else {
                helper.setVisible(R.id.tv_status, false);
                helper.setTextColor(R.id.tv_rmb, Color.parseColor("#a6251a"));
                helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#a6251a"));
                helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#a6251a"));
                // helper.setTextColor(R.id.tv_minPrice, Color.parseColor("#202020"));
            }


        } else {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_thumb), R.drawable.icon_brand_shop_more);
            helper.setVisible(R.id.tv_rmb, false);
        }
    }
}
