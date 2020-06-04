package com.xiling.ddui.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.custom.popupwindow.ScreeningPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScreeningView extends RelativeLayout {


    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.sortStatusView)
    ImageView sortStatusView;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.ll_screening)
    LinearLayout llScreening;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.salesStatusView)
    ImageView salesStatusView;

    ScreeningPopupWindow screeningPopupWindow;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.profitStatusView)
    ImageView profitStatusView;

    /**
     * 排序属性 0-价格,1-上新,2-销量
     * 默认 上新
     */
    private int orderBy = 1;

    /**
     * 排序方式 0-降序(Desc), 1-升序(Asc)
     */
    private int orderPriceType = 0;

    private int orderSaleType = -1;

    private int orderProfitType = -1;


    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    OnItemClickLisener onItemClickLisener;

    public ScreeningView(Context context) {
        super(context);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_screening, this, false);
        ButterKnife.bind(this, view);

        llPrice = view.findViewById(R.id.ll_price);
        llScreening = view.findViewById(R.id.ll_screening);
        llPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderBy != 0) {

                    if (orderBy == 2) {
                        tvSales.setTextColor(Color.parseColor("#AAAAAA"));
                        salesStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    } else if (orderBy == 3) {
                        tvProfit.setTextColor(Color.parseColor("#AAAAAA"));
                        profitStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    }


                    orderBy = 0;
                    tvPrice.setTextColor(Color.parseColor("#FF4647"));
                    if (orderPriceType == 0) {
                        sortStatusView.setImageResource(R.mipmap.icon_price_down);
                    } else {
                        sortStatusView.setImageResource(R.mipmap.icon_price_up);
                    }
                } else {
                    if (orderPriceType == 0) {
                        orderPriceType = 1;
                        sortStatusView.setImageResource(R.mipmap.icon_price_up);
                    } else {
                        orderPriceType = 0;
                        sortStatusView.setImageResource(R.mipmap.icon_price_down);
                    }
                }

                if (onItemClickLisener != null) {
                    onItemClickLisener.onSort(orderBy, orderPriceType);
                }

            }
        });

        llScreening.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (screeningPopupWindow == null) {
                    screeningPopupWindow = new ScreeningPopupWindow(context);
                }
                screeningPopupWindow.showForRight(ScreeningView.this);
                screeningPopupWindow.setOnScreenListener(new ScreeningPopupWindow.onScreenListener() {
                    @Override
                    public void onScreenListener(String tradeType, String saleType, String minPrice, String maxPrice) {
                        if (onItemClickLisener != null) {
                            onItemClickLisener.onFilter(tradeType, saleType,minPrice, maxPrice);
                        }
                    }
                });

            }
        });

        addView(view);
    }

    @OnClick({R.id.ll_profit,R.id.ll_sales_volume})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_profit:
                if (orderBy != 3) {

                    if (orderBy == 0) {
                        tvPrice.setTextColor(Color.parseColor("#AAAAAA"));
                        sortStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    } else if (orderBy == 2) {
                        tvSales.setTextColor(Color.parseColor("#AAAAAA"));
                        salesStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    }


                    orderBy = 3;
                    tvProfit.setTextColor(Color.parseColor("#FF4647"));
                    orderProfitType = 1;
                } else {
                    if (orderProfitType == 1) {
                        tvProfit.setTextColor(Color.parseColor("#AAAAAA"));
                        orderProfitType = -1;
                    } else {
                        tvProfit.setTextColor(Color.parseColor("#FF4647"));
                        orderProfitType = 1;
                    }
                }
                break;
            case R.id.ll_sales_volume:
                if (orderBy != 2) {

                    if (orderBy == 0) {
                        tvPrice.setTextColor(Color.parseColor("#AAAAAA"));
                        sortStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    } else if (orderBy == 3) {
                        tvProfit.setTextColor(Color.parseColor("#AAAAAA"));
                        profitStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    }
                    orderBy = 2;
                    tvSales.setTextColor(Color.parseColor("#FF4647"));
                    orderSaleType = 0;
                } else {
                    if (orderSaleType == 0) {
                        orderSaleType = -1;
                        tvSales.setTextColor(Color.parseColor("#AAAAAA"));
                    } else {
                        orderSaleType = 0;
                        tvSales.setTextColor(Color.parseColor("#FF4647"));
                    }
                }

                break;
        }


        if (onItemClickLisener != null) {
            onItemClickLisener.onSort(orderBy, orderProfitType);
        }
    }

    public interface OnItemClickLisener {
        void onSort(int orderBy, int orderType);

        void onFilter(String tradeType, String saleType, String minPrice, String maxPrice);
    }

}
