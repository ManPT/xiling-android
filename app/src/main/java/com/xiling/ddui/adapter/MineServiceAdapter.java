package com.xiling.ddui.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.image.GlideUtils;

public class MineServiceAdapter extends BaseQuickAdapter<MineServiceAdapter.ServiceBean,BaseViewHolder> {


    public MineServiceAdapter() {
        super(R.layout.item_mine_service);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        helper.setBackgroundRes(R.id.iv_icon,item.url);
        helper.setText(R.id.tv_title,item.title);
        if (item.isClick){
            helper.getView(R.id.iv_icon).getBackground().setAlpha(255);
            helper.setTextColor(R.id.tv_title, Color.parseColor("#282828"));
        }else{
            helper.getView(R.id.iv_icon).getBackground().setAlpha(125);
            helper.setTextColor(R.id.tv_title, Color.parseColor("#939393"));
        }

    }


    public static class ServiceBean{
        int url;
        String title;
        boolean isClick;
        public ServiceBean(int url, String title,boolean isClick) {
            this.url = url;
            this.title = title;
            this.isClick = isClick;
        }

        public int getUrl() {
            return url;
        }

        public void setUrl(int url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isClick() {
            return isClick;
        }
    }

}
