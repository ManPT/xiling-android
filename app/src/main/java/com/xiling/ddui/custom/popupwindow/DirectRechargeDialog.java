package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.XLCashierActivity;
import com.xiling.ddui.bean.RechareConfigBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_CHARGE_MONEY;

/**
 * @author 逄涛
 * 黑卡直充
 */
public class DirectRechargeDialog extends Dialog {
    public static final String TYPE_VIP = "SVIP";
    public static final String TYPE_BLACK = "黑卡";
    Context mContext;
    @BindView(R.id.et_amount)
    TextView etAmount;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_recharging_instructions)
    TextView tvRechargingInstructions;
    String type;
    INewUserService iNewUserService;
    @BindView(R.id.tv_recharg_lable)
    TextView tvRechargLable;

    public DirectRechargeDialog(@NonNull Context context, String type) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.type = type;
    }


    public DirectRechargeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_direct_recharge);
        ButterKnife.bind(this);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();
    }


    private void initView() {
        initWindow();

        APIManager.startRequest(iNewUserService.getRechareConfig(), new BaseRequestListener<RechareConfigBean>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onSuccess(RechareConfigBean result) {
                super.onSuccess(result);

                switch (type) {
                    case TYPE_VIP:
                        etAmount.setText(result.getSvipPrice() + "");
                        if (!TextUtils.isEmpty(result.getSvipLable())) {
                            tvRechargLable.setVisibility(View.VISIBLE);
                            tvRechargLable.setText(result.getSvipLable());
                        } else {
                            tvRechargLable.setVisibility(View.GONE);
                        }

                        tvAccount.setText("直升SVIP会员");
                        tvRechargingInstructions.setText("充值说明：\n 一次性充值货款16800直升VIP会员，充值的货款将直接存储 到个人账户中，后续下单订货可以直接使用余额支付 ");
                        break;
                    case TYPE_BLACK:
                        etAmount.setText(result.getBlackPrice() + "");
                        if (!TextUtils.isEmpty(result.getBlackLable())) {
                            tvRechargLable.setVisibility(View.VISIBLE);
                            tvRechargLable.setText(result.getBlackLable());
                        } else {
                            tvRechargLable.setVisibility(View.GONE);
                        }

                        tvAccount.setText("直升黑卡会员");
                        tvRechargingInstructions.setText("充值说明：\n 一次性充值货款58000直升黑卡会员，充值的货款将直接存储 到个人账户中，后续下单订货可以直接使用余额支付 ");
                        break;
                }
            }
        });


    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    private void recharge() {
        double amount = 0;
        try {
            amount = Double.valueOf(etAmount.getText().toString());
            //跳转收银台
            XLCashierActivity.jumpCashierActivity(mContext, PAY_TYPE_CHARGE_MONEY, amount, 45 * 60 * 1000, (int) (amount * 100) + "");
        } catch (Exception e) {
            ToastUtil.error("充值金额必须为整数");
        }
    }

    @OnClick({R.id.iv_close, R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_recharge:

                // 如果身份在svip及以上，需要账户认证后才能充值
                if (UserManager.getInstance().getUserLevel() >= 2) {
                    UserManager.getInstance().isRealAuth(mContext, new UserManager.RealAuthListener() {
                        @Override
                        public void onRealAuth() {
                            recharge();
                        }
                    });
                } else {
                    recharge();
                }


                break;
        }

        dismiss();
    }


}
