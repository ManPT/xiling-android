package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.CustomterOrderAdapter;
import com.xiling.ddui.adapter.SkuOrderAdapter;
import com.xiling.ddui.bean.ClentOrderDetailBean;
import com.xiling.ddui.bean.CustomerOrderBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 逄涛
 * 客户订单详情
 */
public class CustomerOrderDetailsActivity extends BaseActivity {
    IOrderService mOrderService;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_shouyi)
    TextView tvShouyi;
    @BindView(R.id.tv_warehouse_name)
    TextView tvWarehouseName;
    @BindView(R.id.recycler_sku)
    RecyclerView recyclerSku;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_create_time)
    TextView tvOrderCreateTime;
    @BindView(R.id.tv_order_pay_time)
    TextView tvOrderPayTime;
    @BindView(R.id.tv_order_complete_time)
    TextView tvOrderCompleteTime;

    String orderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);
        ButterKnife.bind(this);
        setTitle("订单详情");
        setLeftBlack();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        if (getIntent() != null) {
            orderCode = getIntent().getStringExtra("orderCode");
        }
        if (TextUtils.isEmpty(orderCode)) {
            return;
        }
        initData();
    }

    private void initData() {
        APIManager.startRequest(mOrderService.getClientOrderDetail(orderCode), new BaseRequestListener<ClentOrderDetailBean>() {
            @Override
            public void onSuccess(ClentOrderDetailBean result) {
                super.onSuccess(result);
                if (result != null){
                    initView(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void initView(ClentOrderDetailBean recordsBean) {
        GlideUtils.loadHead(context, ivHead, recordsBean.getHeadImage());
        tvName.setText("经销商：" + recordsBean.getNickName());
        tvShouyi.setText("收益指数 " + recordsBean.getReceiptsIndices());
        SkuOrderAdapter skuAdapter = new SkuOrderAdapter();
        recyclerSku.setAdapter(skuAdapter);
        recyclerSku.setLayoutManager(new LinearLayoutManager(context));
        skuAdapter.setNewData(recordsBean.getClientOrderDetailList());
        tvWarehouseName.setText(recordsBean.getStoreName());
        if (!TextUtils.isEmpty(recordsBean.getOrderCode())) {
            tvOrderId.setText("订单编号：" + recordsBean.getOrderCode());
        }


        tvOrderCreateTime.setText("创建时间：" + (TextUtils.isEmpty(recordsBean.getCreateDate()) ? "" : recordsBean.getCreateDate()));
        tvOrderPayTime.setText("付款时间：" + (TextUtils.isEmpty(recordsBean.getPayDate()) ? "" : recordsBean.getPayDate()));
        tvOrderCompleteTime.setText("完成时间：" + (TextUtils.isEmpty(recordsBean.getReceivedDate()) ? "" : recordsBean.getReceivedDate()));

    }
}
