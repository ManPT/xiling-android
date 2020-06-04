package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.xiling.R;
import com.xiling.ddui.bean.AutoClickBean;
import com.xiling.ddui.manager.AutoClickManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.bean.Splash;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pt
 * 新人优惠弹框
 */
public class NewcomerDiscountDialog extends Dialog {
    Context mContext;
    Splash splashBean;
    @BindView(R.id.btn_content)
    ImageView btnContent;

    public NewcomerDiscountDialog(@NonNull Context context, Splash splashBean) {
        super(context);
        mContext = context;
        this.splashBean = splashBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_newcomer_discount);
        ButterKnife.bind(this);
        setCancelable(false);
        GlideUtils.loadIntoUseFitWidth(mContext,splashBean.getBackUrl(),btnContent);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @OnClick({R.id.btn_content, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_content:
                AutoClickBean autoClickBean = new AutoClickBean() {
                    @Override
                    public String getEvent() {
                        return  splashBean.getEvent();
                    }

                    @Override
                    public String getTarget() {
                        return  splashBean.getTarget();
                    }
                };
                AutoClickManager.pars(mContext,autoClickBean);
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
