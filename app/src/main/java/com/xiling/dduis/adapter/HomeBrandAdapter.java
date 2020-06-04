package com.xiling.dduis.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.AutoClickBean;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.ddui.view.RLoopRecyclerView;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.image.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeBrandAdapter extends RLoopRecyclerView.LoopAdapter<HomeBrandAdapter.MyViewHolder, NationalPavilionBean> {

    private OnItemClickListener onItemClickListener;
    private Context mContext;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public HomeBrandAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onBindLoopViewHolder(HomeBrandAdapter.MyViewHolder holder, final int position) {
        NationalPavilionBean item = datas.get(position);
        if (!TextUtils.isEmpty(item.getCountryBanner())) {
            GlideUtils.loadImage(mContext, holder.imageView, item.getCountryBanner());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(datas.get(position), position);
                }
            }
        });

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_brand, parent, false);
        return new MyViewHolder(inflate);
    }

  /*  public HomeBrandAdapter(int layoutResId, @Nullable List<HomeDataBean.BrandHotSaleListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean.BrandHotSaleListBean item) {

    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
        }
    }


    public interface OnItemClickListener {
        void onItemClickListener(NationalPavilionBean bean, int position);
    }

}
