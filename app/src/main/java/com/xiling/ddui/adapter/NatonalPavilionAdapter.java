package com.xiling.ddui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.image.GlideUtils;

/**
 * created by Jigsaw at 2018/12/10
 * 右侧分类 国家馆
 */
public class NatonalPavilionAdapter extends BaseQuickAdapter<NationalPavilionBean, BaseViewHolder> {

    public NatonalPavilionAdapter() {
        super(R.layout.item_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NationalPavilionBean item) {
        helper.setText(R.id.tv_title,item.getCountryName());
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getCountryListBanner());
    }



}
