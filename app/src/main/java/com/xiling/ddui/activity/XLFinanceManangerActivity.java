package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.AccountInfo;
import com.xiling.ddui.custom.popupwindow.RechargeDialog;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.RECHARGE_SUCCESS;

/**
 * @auth 逄涛
 * 财务管理
 */
public class XLFinanceManangerActivity extends BaseActivity {
    INewUserService iNewUserService;
    @BindView(R.id.tv_balace)
    TextView tvBalace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlfinance_mananger);
        ButterKnife.bind(this);

        setTitle("财务管理");
        setLeftBlack();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        getAccountInfo();
    }

    /**
     * 获取账户信息（余额）
     */
    private void getAccountInfo() {
        APIManager.startRequest(iNewUserService.getAccountInfo(), new BaseRequestListener<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo result) {
                super.onSuccess(result);
                if (result != null) {
                    tvBalace.setText(NumberHandler.reservedDecimalFor2(result.getBalance()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("账户信息获取失败");
            }
        });
    }

    @OnClick({R.id.btn_detailed, R.id.btn_recharge})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_detailed:
                startActivity(new Intent(context, BalanceDetailsActivity.class));
                break;
            case R.id.btn_recharge:
                // 如果身份在svip及以上，需要账户认证后才能充值
                if (UserManager.getInstance().getUserLevel() >= 2) {
                    UserManager.getInstance().isRealAuth(context, new UserManager.RealAuthListener() {
                        @Override
                        public void onRealAuth() {
                            new RechargeDialog(context).show();
                        }
                    });
                } else {
                    new RechargeDialog(context).show();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventMessage message) {
        //充值成功，刷新
        if (message.getEvent().equals(RECHARGE_SUCCESS)) {
            getAccountInfo();
            if ((int)message.getData() == 1){
                ToastUtil.error("充值成功");
            }else {
                ToastUtil.error("提交成功\n等待审核");
            }

        }
    }

}
