package com.xiling.ddui.custom.popupwindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.R;
import com.xiling.ddui.bean.BrandBean;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrandShareDialog extends Dialog {
    BrandBean brandBean;
    String shareUrl = "";
    Activity mContext;

    public BrandShareDialog(@NonNull Activity context, BrandBean brandBean) {
        super(context, R.style.WXShareDialog);
        this.brandBean = brandBean;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_brand_share);
        ButterKnife.bind(this);
        initWindow();
        shareUrl = H5UrlConfig.WEB_URL_BRAND + brandBean.getBrandId();
        // ShareUtils.share(BrandActivity.this,result.getBrandName(),"",result.getIconUrl(),shareUrl);
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

    @OnClick({R.id.llShareWechat, R.id.llShareWechatCircle, R.id.tv_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llShareWechat:
                ShareUtils.share(mContext, "喜领精选品牌【" + brandBean.getBrandName() + "】", "精选全球当红大牌", brandBean.getIconUrl(), shareUrl,null);
                dismiss();
                break;
            case R.id.llShareWechatCircle:
                ShareUtils.share(mContext, "喜领精选品牌【" + brandBean.getBrandName() + "】", "精选全球当红大牌", brandBean.getIconUrl(), shareUrl,SHARE_MEDIA.WEIXIN_CIRCLE,null);
                dismiss();
                break;
            case R.id.tv_btn_cancel:
                dismiss();
                break;
        }
    }


}
