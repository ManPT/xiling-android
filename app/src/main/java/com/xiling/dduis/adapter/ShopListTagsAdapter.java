package com.xiling.dduis.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;

import java.util.List;

/**
 * pt
 * 商品标签
 */
public class ShopListTagsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShopListTagsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        switch (item){
            case "1":
                helper.setText(R.id.tv_title,"国内品牌");
                helper.setTextColor(R.id.tv_title, Color.parseColor("#E42B3A"));
                helper.setBackgroundRes(R.id.tv_title,R.drawable.bg_trade_type1);
                break;
            case "2":
                helper.setText(R.id.tv_title,"跨境保税");
                helper.setTextColor(R.id.tv_title, Color.parseColor("#A337DF"));
                helper.setBackgroundRes(R.id.tv_title,R.drawable.bg_trade_type2);
                break;
            case "3":
                helper.setText(R.id.tv_title,"一般贸易");
                helper.setTextColor(R.id.tv_title, Color.parseColor("#7DAB2B"));
                helper.setBackgroundRes(R.id.tv_title,R.drawable.bg_trade_type3);
                break;
            case "4":
                helper.setText(R.id.tv_title,"海外直邮");
                helper.setTextColor(R.id.tv_title, Color.parseColor("#43A7DD"));
                helper.setBackgroundRes(R.id.tv_title,R.drawable.bg_trade_type4);
                break;
        }

    }

}
