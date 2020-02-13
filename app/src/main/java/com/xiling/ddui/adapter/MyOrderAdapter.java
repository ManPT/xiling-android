package com.xiling.ddui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.DetailsBean;
import com.xiling.ddui.bean.MyOrderDetailBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.tools.NumberHandler;

import java.util.List;

import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * 我的订单
 */
public class MyOrderAdapter extends BaseQuickAdapter<XLOrderDetailsBean, BaseViewHolder> {

    public MyOrderAdapter() {
        super(R.layout.item_order);
    }

    OnButtomItemClickListener onButtomItemClickListener;

    public void setOnButtomItemClickListener(OnButtomItemClickListener onButtomItemClickListener) {
        this.onButtomItemClickListener = onButtomItemClickListener;
    }


    @Override
    protected void convert(BaseViewHolder helper, final XLOrderDetailsBean item) {
        helper.setText(R.id.order_number, "订单号: " + item.getOrderCode());
        helper.setText(R.id.tv_total_size, "共" + item.getTotalQuantity() + "件");
        helper.setText(R.id.tv_price, "¥ " + NumberHandler.reservedDecimalFor2(item.getTotalPrice()));
        List<DetailsBean> details = item.getDetails();

        RecyclerView skuRecyclerView = helper.getView(R.id.itemRecyclerView);
        skuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SkuOrderAdapter skuAdapter = new SkuOrderAdapter();
        skuRecyclerView.setAdapter(skuAdapter);
        skuAdapter.setNewData(details);
        helper.setText(R.id.itemStatusTv, item.getOrderStatus());
        setStatus(helper, item.getOrderStatusUs());
        skuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onItemClickListerer(item);
                }
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onItemClickListerer(item);
                }
            }
        });

        helper.setOnClickListener(R.id.btn_see, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onSeeClickListerer(item);
                }
            }
        });

        helper.setOnClickListener(R.id.btn_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onConfirmClickListerer(item);
                }
            }
        });

        helper.setOnClickListener(R.id.btn_remind, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onRemindClickListerer(item);
                }
            }
        });

        helper.setOnClickListener(R.id.btm_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onCancelClickListerer(item);
                }
            }
        });

        helper.setOnClickListener(R.id.btn_payment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onPaymentClickListerer(item);
                }
            }
        });


    }


    private void setStatus(BaseViewHolder helper, String status) {
        switch (status) {
            case ORDER_WAIT_PAY:
                helper.setVisible(R.id.btn_see, false);
                helper.setVisible(R.id.btn_confirm, false);
                helper.setVisible(R.id.btn_remind, false);
                helper.setVisible(R.id.btm_cancel, true);
                helper.setVisible(R.id.btn_payment, true);

                break;
            case ORDER_WAIT_SHIP:
                helper.setVisible(R.id.btn_see, false);
                helper.setVisible(R.id.btn_confirm, false);
                helper.setVisible(R.id.btn_remind, true);
                helper.setVisible(R.id.btm_cancel, false);
                helper.setVisible(R.id.btn_payment, false);
                break;
            case ORDER_WAIT_RECEIVED:
                helper.setVisible(R.id.btn_see, true);
                helper.setVisible(R.id.btn_confirm, true);
                helper.setVisible(R.id.btn_remind, false);
                helper.setVisible(R.id.btm_cancel, false);
                helper.setVisible(R.id.btn_payment, false);
                break;

            default:
                helper.setVisible(R.id.btn_see, true);
                helper.setVisible(R.id.btn_confirm, false);
                helper.setVisible(R.id.btn_remind, false);
                helper.setVisible(R.id.btm_cancel, false);
                helper.setVisible(R.id.btn_payment, false);
                break;
        }
    }


    public interface OnButtomItemClickListener {
        void onSeeClickListerer(XLOrderDetailsBean recordsBean);

        void onConfirmClickListerer(XLOrderDetailsBean recordsBean);

        void onRemindClickListerer(XLOrderDetailsBean recordsBean);

        void onCancelClickListerer(XLOrderDetailsBean recordsBean);

        void onPaymentClickListerer(XLOrderDetailsBean recordsBean);

        void onItemClickListerer(XLOrderDetailsBean recordsBean);

    }


}