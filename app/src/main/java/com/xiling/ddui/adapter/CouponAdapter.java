package com.xiling.ddui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CouponBean;
import com.xiling.module.community.DateUtils;

public class CouponAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    public void setSelectId(String selectId) {
        this.selectId = selectId;
        notifyDataSetChanged();
    }

    private String selectId = "";

    public CouponAdapter() {
        super(R.layout.item_coupon_new);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        helper.setText(R.id.tv_price, item.getReducedPrice() + "");
        helper.setText(R.id.tv_conditions, "全场满¥" + item.getConditions());
        helper.setText(R.id.tv_name, item.getName());
        if (!TextUtils.isEmpty(item.getEnd())) {
            String data = DateUtils.date2TimeStamp(item.getEnd(), null);
            String endTime = DateUtils.timeStamp2Date(Long.valueOf(data), "yyyy.MM.dd");
            helper.setText(R.id.tv_time, "有效期至" + endTime);
        }
        helper.getView(R.id.iv_select).setSelected(item.getId().equals(selectId));
    }
}