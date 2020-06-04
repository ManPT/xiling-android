package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.module.page.WebViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServicePolicyDialog extends Dialog {

    TextView tvContent;
    String content = "请您务必审视阅读，充分理解“服务协议”和“隐私协议”各条款，包括但不限于：为可向您提供移动商城、内容分享等服务您可阅读《服务协议》和《隐私协议》了解详细信息，如你同意，请点击“同意”开始接受我们的服务";
    Context context;

    public ServicePolicyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_policy);
        ButterKnife.bind(this);
        tvContent = findViewById(R.id.tv_content);
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(content);
        setCanceledOnTouchOutside(false);
        // 在设置点击事件、同时设置字体颜色
        ClickableSpan clickableSpanTwo = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                WebViewActivity.jumpUrl(context, "喜领服务协议", H5UrlConfig.SERVICE_AGREEMENT);
            }

        };
        spannableBuilder.setSpan(clickableSpanTwo, 59, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 在设置点击事件、同时设置字体颜色
        ClickableSpan clickableSpanTwo2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                WebViewActivity.jumpUrl(context, "隐私协议", H5UrlConfig.PRIVACY_AGREEMENT);
            }

        };
        spannableBuilder.setSpan(clickableSpanTwo2, 66, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannableBuilder);
    }


    @OnClick({R.id.tv_btn_cancel, R.id.tv_btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_cancel:
                dismiss();
                break;
            case R.id.tv_btn_confirm:
                dismiss();
                break;
        }
    }
}
