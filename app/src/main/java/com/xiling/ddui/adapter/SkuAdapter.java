package com.xiling.ddui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;

public class SkuAdapter extends BaseQuickAdapter<OrderDetailBean.StoresBean.ProductsBean, BaseViewHolder> {


    public SkuAdapter() {
        super(R.layout.item_sku);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean.StoresBean.ProductsBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getProductImage());
        helper.setText(R.id.tv_product_name, item.getSkuName());
        helper.setText(R.id.tv_product_des, item.getProperties());
        helper.setText(R.id.tv_quantity, "x " + item.getQuantity());
        helper.setText(R.id.tv_price, "¥" + NumberHandler.reservedDecimalFor2(item.getRetailPrice()));
        NumberHandler.setPriceText(item.getPrice(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
       /* if (item.getIsCross() == 1) {
            helper.setVisible(R.id.tv_taxation, true);
            if (item.getTotalTaxes() > 0) {
                helper.setText(R.id.tv_taxation, "进口税¥" + NumberHandler.reservedDecimalFor2(item.getTotalTaxes()));
            } else {
                helper.setText(R.id.tv_taxation, "已包税");
            }
        } else {
            helper.setVisible(R.id.tv_taxation, false);
        }*/


    }
}
