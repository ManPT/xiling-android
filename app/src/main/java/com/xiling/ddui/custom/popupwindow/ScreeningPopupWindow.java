package com.xiling.ddui.custom.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 逄涛
 * 分类筛选
 */
public class ScreeningPopupWindow extends PopupWindow implements View.OnClickListener {
    @BindView(R.id.recycler_product_type)
    RecyclerView recyclerProductType;
    @BindView(R.id.recycler_service_type)
    RecyclerView recyclerServiceType;
    private Context mContext;
    TypeAdapter productAdapter;
    TypeAdapter serviceAdapter;
    List<TypeBean> productList = new ArrayList<>();
    List<TypeBean> serviceList = new ArrayList<>();

    public ScreeningPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public void setOnScreenListener(ScreeningPopupWindow.onScreenListener onScreenListener) {
        this.onScreenListener = onScreenListener;
    }

    private onScreenListener onScreenListener;

    private View btnReset, btnOK;
    private EditText edMin, edMax;

    private boolean isfree = false;

    private String minPrice = "", maxPrice = "";

    private String saleType, tradeType;
    private int productPosition = -1,servicePosition = -1;


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_screen, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ScreenUtils.dip2px(mContext, 300));
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationRight);
        // 设置点击popupwindow外屏幕其它地方消失
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(false);
                full(false);
            }
        });

        productList.clear();
        productList.add(new TypeBean("海外直邮", "4", false));
        productList.add(new TypeBean("一般贸易", "3", false));
        productList.add(new TypeBean("跨境保税", "2", false));
        productList.add(new TypeBean("国内品牌", "1", false));
        serviceList.clear();
        serviceList.add(new TypeBean("一件代发", "1", false));
        serviceList.add(new TypeBean("批量采购", "2", false));

        recyclerProductType.setLayoutManager(new GridLayoutManager(mContext, 3));
        productAdapter = new TypeAdapter(productList);
        recyclerProductType.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                productList.get(position).setClick(!productList.get(position).isClick);
                tradeType = productList.get(position).isClick?productList.get(position).getType():"";

                if (productList.get(position).isClick){
                    if (productPosition > -1){
                        productList.get(productPosition).setClick(!productList.get(productPosition).isClick);
                    }
                    productPosition =position;
                }else{
                    productPosition = -1;
                }



                productAdapter.notifyDataSetChanged();
            }
        });


        recyclerServiceType.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        serviceAdapter = new TypeAdapter(serviceList);
        recyclerServiceType.setAdapter(serviceAdapter);

        serviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                serviceList.get(position).setClick(!serviceList.get(position).isClick);
                saleType = serviceList.get(position).isClick?serviceList.get(position).getType():"";

                if (serviceList.get(position).isClick){
                    if (servicePosition > -1){
                        serviceList.get(servicePosition).setClick(!serviceList.get(servicePosition).isClick);
                    }
                    servicePosition = position;
                }else{
                    servicePosition = -1;
                }
                serviceAdapter.notifyDataSetChanged();
            }
        });


        btnReset = view.findViewById(R.id.btn_reset);
        btnOK = view.findViewById(R.id.btn_ok);
        edMin = view.findViewById(R.id.ed_min);
        edMax = view.findViewById(R.id.ed_max);

        btnReset.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }


    public void showForRight(View view) {
        setWindowAlpha(true);
        full(true);
        showAtLocation(view, Gravity.NO_GRAVITY, ScreenUtils.dip2px(mContext, 300), 0);
    }

    private void setWindowAlpha(boolean isAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        if (!isAlpha) {
            lp.alpha = 1f;
        } else {
            lp.alpha = 0.5f;
        }

        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void full(boolean enable) {
        if (enable) {//隐藏状态栏
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity) mContext).getWindow().setAttributes(lp);
        } else {//显示状态栏
            WindowManager.LayoutParams attr = ((Activity) mContext).getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) mContext).getWindow().setAttributes(attr);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                isfree = false;
                minPrice = "";
                maxPrice = "";
                edMin.setText("");
                edMax.setText("");
                break;
            case R.id.btn_ok:
                minPrice = edMin.getText().toString();
                maxPrice = edMax.getText().toString();

                if (onScreenListener != null) {
                    Log.d("pangtao","tradeType = " + tradeType + "saleType = " +saleType);
                    onScreenListener.onScreenListener(tradeType,saleType, minPrice, maxPrice);
                }

                this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface onScreenListener {
        void onScreenListener(String tradeType, String saleType, String minPrice, String maxPrice);
    }


    class TypeBean {
        String name;
        String type;
        boolean isClick;

        public TypeBean(String name, String type, boolean isClick) {
            this.name = name;
            this.isClick = isClick;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    class TypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {

        public TypeAdapter(@Nullable List<TypeBean> data) {
            super(R.layout.item_screen_popup_type, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TypeBean item) {
            helper.setText(R.id.tv_title, item.getName());
            helper.setBackgroundRes(R.id.tv_title, item.isClick ? R.drawable.bg_screen_select : R.drawable.bg_screen_unselect);

        }
    }


}
