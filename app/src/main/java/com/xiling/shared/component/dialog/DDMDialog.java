package com.xiling.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.common.AdvancedCountdownTimer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Jigsaw at 2018/8/27
 */
public class DDMDialog extends Dialog {

    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_btn_confirm)
    TextView mTvBtnPositive;
    @BindView(R.id.tv_btn_cancel)
    TextView mTvBtnNegative;

    private AdvancedCountdownTimer mCountdownTimer;
    private long mCountTime = 6 * 1000;
    private long mInterval = 1000;

    private CharSequence mContent;
    private String mTitle;
    private int mContentGravity = -1;

    private boolean mEnableClose = true;

    private String mPositiveButtonName;
    private String mNegativeButtonName;

    private View mContentView;

    private View.OnClickListener mOnPositiveClickListener;
    private View.OnClickListener mOnNegativeClickListener;

    public DDMDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public DDMDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDMDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ddm);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {


        if (TextUtils.isEmpty(mTitle)){
            mTvTitle.setVisibility(View.GONE);
        }else{
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mContent)) {
            mTvContent.setText(mContent);
        }

        if (null != mOnNegativeClickListener) {
            mTvBtnNegative.setText(mNegativeButtonName);
            mTvBtnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    mOnNegativeClickListener.onClick(v);

                }
            });
        } else {
            mTvBtnNegative.setVisibility(View.GONE);
        }

        if (null != mOnPositiveClickListener) {
            mTvBtnPositive.setText(mPositiveButtonName);
            mTvBtnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    mOnPositiveClickListener.onClick(v);
                }
            });
        } else {
            mTvBtnPositive.setVisibility(View.GONE);
        }

    }

    public DDMDialog setContent(CharSequence content) {
        this.mContent = content;
        return this;
    }

    public DDMDialog setCustomContentView(View view) {
        this.mContentView = view;
        return this;
    }

    public DDMDialog setContextGravity(int gravity) {
        this.mContentGravity = gravity;
        return this;
    }

    public DDMDialog enableClose(boolean enable) {
        this.mEnableClose = enable;
        return this;
    }

    public DDMDialog setPositiveButton(String buttonName, View.OnClickListener positiveButton) {
        this.mPositiveButtonName = buttonName;
        this.mOnPositiveClickListener = positiveButton;
        return this;
    }

    public DDMDialog setNegativeButton(String buttonName, View.OnClickListener negativeButton) {
        this.mNegativeButtonName = buttonName;
        this.mOnNegativeClickListener = negativeButton;
        return this;
    }

    public void showWithDelay() {
        showWithDelay(mCountTime);
    }

    public void showWithDelay(long delay) {
        show();
        mCountTime = delay;
        mTvBtnPositive.setText(String.format("%s s", mCountTime / 1000));
        mTvBtnPositive.setEnabled(false);
        mCountdownTimer = new AdvancedCountdownTimer(mCountTime, mInterval) {

            @Override
            public void onTick(long millisUntilFinished, int percent) {
                mTvBtnPositive.setText(String.format("%s s", millisUntilFinished / 1000));
                if (millisUntilFinished / 1000 == 0) {
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                mTvBtnPositive.setText(mPositiveButtonName);
                mTvBtnPositive.setEnabled(true);
                mCountdownTimer.cancel();
            }
        }.start();

    }

    public DDMDialog setTitle(String title){
        mTitle = title;
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
    }


}
