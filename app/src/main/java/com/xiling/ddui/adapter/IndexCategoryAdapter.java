package com.xiling.ddui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.bean.IndexBrandBean;
import com.xiling.ddui.bean.IndexCategoryBean;
import com.xiling.image.GlideUtils;

import java.util.List;

/**
 * @author 逄涛
 * 首页-首页分类
 */
public class IndexCategoryAdapter extends BaseQuickAdapter<IndexCategoryBean, BaseViewHolder> {
    IndexBrandShopAdapter indexBrandShopAdapter;

    public IndexCategoryAdapter() {
        super(R.layout.item_index_brand);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IndexCategoryBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_brand), item.getIndexUrl());
        RecyclerView recyclerView = helper.getView(R.id.recycler_shop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<IndexBrandBean.IndexBrandBeanListBean> brandList = item.getIndexCategoryBeanList();
        brandList.add(null);
        indexBrandShopAdapter = new IndexBrandShopAdapter(brandList);
        recyclerView.setAdapter(indexBrandShopAdapter);

        indexBrandShopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IndexBrandBean.IndexBrandBeanListBean bean = (IndexBrandBean.IndexBrandBeanListBean) adapter.getData().get(position);
                if (bean != null) {
                    DDProductDetailActivity.start(mContext, bean.getProductId());
                } else {
                    BrandActivity.jumpCategoryActivity(mContext, item.getCategoryId(),item.getCategoryName(), item.getCategoryUrl());
                }
            }
        });

        helper.setOnClickListener(R.id.iv_brand, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrandActivity.jumpCategoryActivity(mContext, item.getCategoryId(),item.getCategoryName(), item.getCategoryUrl());
            }
        });


    }
}
