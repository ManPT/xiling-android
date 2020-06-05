package com.xiling.ddui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.az.lettersort.ItemAdapter;
import com.az.lettersort.azList.AZBaseAdapter;
import com.az.lettersort.azList.BaseSortBean;
import com.xiling.R;
import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.bean.BrandListBean;
import com.xiling.image.GlideUtils;

import java.util.ArrayList;
import java.util.List;


public class BrandAdapter extends AZBaseAdapter<BrandListBean.GroupsBean.BrandsBean, BrandAdapter.ItemHolder> {

	List<BrandListBean.GroupsBean.BrandsBean> mDatas;
	Context mContext;
	public BrandAdapter(Context context,List<BrandListBean.GroupsBean.BrandsBean> dataList) {
		super(dataList);
		mDatas = dataList;
		mContext = context;
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_brand, parent, false));
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, final int position) {
		holder.mTextName.setText(mDatas.get(position).getName());
		GlideUtils.loadImage(mContext,holder.icon,mDatas.get(position).getCategoryUrl());

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BrandActivity.jumpBrandActivity(mContext,"",mDatas.get(position).getBrandId());
			}
		});
	}

	static class ItemHolder extends RecyclerView.ViewHolder {

		TextView mTextName;
		ImageView icon;
		ItemHolder(View itemView) {
			super(itemView);
			mTextName = itemView.findViewById(R.id.tv_title);
			icon = itemView.findViewById(R.id.iv_icon);
		}
	}
}
