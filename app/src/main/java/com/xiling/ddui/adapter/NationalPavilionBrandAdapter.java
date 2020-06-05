package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.BrandListBean;
import com.xiling.image.GlideUtils;

import java.util.List;

public class NationalPavilionBrandAdapter extends BaseQuickAdapter<BrandListBean.GroupsBean.BrandsBean, BaseViewHolder> {

    public NationalPavilionBrandAdapter( @Nullable List<BrandListBean.GroupsBean.BrandsBean> data) {
        super(R.layout.item_national_pavilion_brand, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandListBean.GroupsBean.BrandsBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_brand),item.getCategoryUrl());
    }
}
