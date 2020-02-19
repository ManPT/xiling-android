package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.XLCashierActivity;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPayService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_CHARGE_MONEY;

/**
 * @author 逄涛
 * 黑卡直充
 */
public class DirectRechargeDialog extends Dialog {
    Context mContext;
    IPayService iPayService;
    @BindView(R.id.et_amount)
    TextView etAmount;

    public DirectRechargeDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public DirectRechargeDialog(@NonNull Context context, ArrayList<SkuListBean> skuList, String selectId) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public DirectRechargeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public DirectRechargeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_direct_recharge);
        ButterKnife.bind(this);
        iPayService = ServiceManager.getInstance().createService(IPayService.class);
        initView();
    }


    private void initView() {
        initWindow();
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

    @OnClick({R.id.iv_close, R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_recharge:
                double amount = 0;
                try {
                    amount = Integer.valueOf(etAmount.getText().toString());
                    //跳转收银台
                    XLCashierActivity.jumpCashierActivity(mContext, PAY_TYPE_CHARGE_MONEY, amount, 45 * 60 * 1000, (int) (amount * 100) + "");
                } catch (Exception e) {
                    ToastUtil.error("充值金额必须为整数");
                }
                break;
        }

        dismiss();
    }


}