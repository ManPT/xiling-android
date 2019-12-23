package com.xiling.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.ExpressDetails;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IExpressService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class ExpressDetailsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private String mCompanyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);
        ButterKnife.bind(this);

        getIntentData();

        initData();
    }

    private void initData() {
        IExpressService service = ServiceManager.getInstance().createService(IExpressService.class);
        service.getExpressDetails(
                "222",
                "333",
                "1",
                System.currentTimeMillis()+""
        ).subscribe(new Consumer<ExpressDetails>() {
            @Override
            public void accept(ExpressDetails expressDetails) throws Exception {

            }
        });


    }

    private void getIntentData() {
        Intent intent = getIntent();
        mCompanyCode = intent.getStringExtra("companyCode");

    }
}