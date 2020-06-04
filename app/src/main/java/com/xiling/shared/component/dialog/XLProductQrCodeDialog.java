package com.xiling.shared.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.util.CommonUtil;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class XLProductQrCodeDialog extends Dialog {

    Activity mActivity;
    ProductNewBean productNewBean;
    @BindView(R.id.img_thumb)
    ImageView imgThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_market_price)
    TextView tvMarketPrice;
    @BindView(R.id.RelScreen)
    RelativeLayout relScreen;
    @BindView(R.id.iv_headIcon)
    ImageView ivHeadIcon;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    private String sharedTitle = "";
    private String sharedDes = "从源头到客服一站式服务、一件代发，专享批发价格";
    private String sharedThumb = "";
    private String sharedUrl = "";
    Unbinder unbinder;

    public XLProductQrCodeDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
    }

    public XLProductQrCodeDialog(Activity actiity, ProductNewBean productNewBean) {
        super(actiity);
        this.mActivity = actiity;
        this.productNewBean = productNewBean;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prodecu_qr_xl);
        unbinder = ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initSharedData();
        initView();
    }

    private void initView() {
        tvMarketPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (productNewBean != null) {
            if (productNewBean.getImages() != null && productNewBean.getImages().size() > 0) {
                GlideUtils.loadImage(mActivity, imgThumb, productNewBean.getImages().get(0));
            }

            if (!TextUtils.isEmpty(productNewBean.getProductName())) {
                tvTitle.setText(productNewBean.getProductName());
            }

            tvPrice.setText("¥ " + NumberHandler.reservedDecimalFor2(productNewBean.getMinPrice()));
            tvMarketPrice.setText("¥ " + NumberHandler.reservedDecimalFor2(productNewBean.getMinMarketPrice()));
        }

        GlideUtils.loadHead(mActivity,ivHeadIcon,UserManager.getInstance().getUser().getHeadImage());
        tvUserName.setText(UserManager.getInstance().getUser().getNickName());

        ShareUtils.createQRImage(mActivity, sharedUrl, new ShareUtils.OnQRImageListener() {
            @Override
            public void onCreatQR(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQr.setImageBitmap(bitmap);
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        });
    }

    private void initSharedData() {
        if (productNewBean != null) {
            sharedTitle = productNewBean.getProductName();
            if (productNewBean.getImages() != null && productNewBean.getImages().size() > 0) {
                sharedThumb = productNewBean.getImages().get(0);
            }
            sharedUrl = BuildConfig.BASE_URL + "spu/" + productNewBean.getProductId() + "/" + UserManager.getInstance().getUser().getInviteCode();
        }

    }


    @OnClick({R.id.llShareWechat, R.id.llShareWechatCircle, R.id.llShareDisk, R.id.tv_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llShareWechat:
                ShareUtils.share(mActivity, sharedTitle, sharedDes, sharedThumb, sharedUrl, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtil.success("分享成功");
                        dismiss();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtil.success("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                });
                dismiss();
                break;
            case R.id.llShareWechatCircle:
                ShareUtils.shareTo3rdPlatform(mActivity, relScreen, SHARE_MEDIA.WEIXIN_CIRCLE, productNewBean.getProductId());
                dismiss();
                break;
            case R.id.llShareDisk:
                ShareUtils.saveDiskShare(mActivity, relScreen, productNewBean.getProductId());
                dismiss();
                break;
            case R.id.tv_btn_cancel:
                dismiss();
                break;
        }
    }


}
