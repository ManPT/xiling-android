package com.xiling.ddui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CustomerOrderBean;
import com.xiling.ddui.bean.DetailsBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.tools.NumberHandler;

import java.util.List;

import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * 我的订单
 */
public class CustomterOrderAdapter extends BaseQuickAdapter<CustomerOrderBean.OrderDetailsBean, BaseViewHolder> {

    public CustomterOrderAdapter() {
        super(R.layout.item_customter_order);
    }

    OnButtomItemClickListener onButtomItemClickListener;

    public void setOnButtomItemClickListener(OnButtomItemClickListener onButtomItemClickListener) {
        this.onButtomItemClickListener = onButtomItemClickListener;
    }


    @Override
    protected void convert(BaseViewHolder helper, final CustomerOrderBean.OrderDetailsBean item) {
        helper.setText(R.id.order_number, "订单号: " + item.getOrderCode());
        helper.setText(R.id.tv_price, "¥ " + NumberHandler.reservedDecimalFor2(item.getReceiptsIndices()));


        helper.setText(R.id.tv_freight, "含运费¥" + NumberHandler.reservedDecimalFor2(item.getFreight()));
        helper.setText(R.id.tv_taxation, "税费¥" + NumberHandler.reservedDecimalFor2(item.getTotalTaxes()));
        helper.setText(R.id.tv_total_number,"共计"+item.getTotalQuantoity()+"件");
        List<DetailsBean> details = item.getClientOrderDetailList();

        RecyclerView skuRecyclerView = helper.getView(R.id.itemRecyclerView);
        skuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SkuOrderAdapter skuAdapter = new SkuOrderAdapter();
        skuRecyclerView.setAdapter(skuAdapter);
        skuAdapter.setNewData(details);

        switch (item.getOrderStatus()) {
            case "10":
                helper.setText(R.id.itemStatusTv, "待支付");
                break;
            case "15":
                helper.setText(R.id.itemStatusTv, "待审核");
                break;
            case "20":
                helper.setText(R.id.itemStatusTv, "待发货");
                break;
            case "30":
                helper.setText(R.id.itemStatusTv, "待收货");
                break;
            case "40":
                helper.setText(R.id.itemStatusTv, "已收货");
                break;
            case "50":
                helper.setText(R.id.itemStatusTv, "已关闭");
                break;
        }
        skuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onButtomItemClickListener != null) {
                    onButtomItemClickListener.onItemClickListerer(item);
                }
            }
        });

    }


    public interface OnButtomItemClickListener {
        void onItemClickListerer(CustomerOrderBean.OrderDetailsBean recordsBean);
    }


}
