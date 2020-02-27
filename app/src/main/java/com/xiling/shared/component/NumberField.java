package com.xiling.shared.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.shared.component.dialog.EditNumberDialog;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumberField extends LinearLayout {

    @BindView(R.id.minusBtn)
    protected ImageView mMinusBtn;
    @BindView(R.id.plusBtn)
    protected ImageView mPlusBtn;
    @BindView(R.id.valueTv)
    protected TextView mValueTv;

    private int mMin = 1;
    private int mMax = 999;

    public int getmValue() {
        return mValue;
    }

    private int mValue = 1;
    private OnValueChangeLister mListener;

    public NumberField(Context context) {
        super(context);
        initView();
    }

    public NumberField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.cmp_number_field_layout, this);
        ButterKnife.bind(this, view);
        setButtonsEnabled();
    }

    public void setViewSize(int size) {
        int rSize = ConvertUtil.dip2px(size);

        ViewGroup.LayoutParams param1 = mMinusBtn.getLayoutParams();
        param1.width = rSize;
        param1.height = rSize;
        mMinusBtn.setLayoutParams(param1);

        ViewGroup.LayoutParams param2 = mPlusBtn.getLayoutParams();
        param2.width = rSize;
        param2.height = rSize;
        mPlusBtn.setLayoutParams(param2);
    }

    /**
     * 默认数据
     */
    public void setDefaultValue(int value) {
        this.mValue = value;
        mValueTv.setText("" + this.mValue);
        setButtonsEnabled();
    }

    /**
     * 设置数据有效区间
     */
    public void setLimit(int min, int max) {
        mMin = min;
        mMax = max;
        if (mValue > mMax) {
            mValue = mMax;
        } else if (mValue < mMin) {
            mValue = mMin;
        }
        setValue(mValue);
        setButtonsEnabled();
    }

    public void setValue(int value) {
            if (value > mMax) {
                ToastUtil.error("数量超出范围");
            }

            value = value < mMin ? mMin : value;
            this.mValue = value <= mMax ? value : mMax;

            mValueTv.setText("" + this.mValue);
            if (this.mListener != null) {
                this.mListener.changed(this.mValue);
            }
            setButtonsEnabled();
    }

    private void setButtonsEnabled() {
        this.mMinusBtn.setEnabled(mMin != this.mValue);
        this.mPlusBtn.setEnabled(mMax != this.mValue);
    }

    public void setOnChangeListener(OnValueChangeLister listener) {
        this.mListener = listener;
    }

    public void setValues(int value, int min, int max) {
        this.mMin = min;
        this.mMax = max;
        this.setValue(value);
    }

    public void setEditable(boolean editable) {
        mValueTv.setEnabled(editable);
    }

    @OnClick(R.id.minusBtn)
    protected void onMinus(View view) {
        ViewUtil.setViewClickedDelay(view);
        this.mValue--;
        setValue(this.mValue);
    }

    @OnClick(R.id.plusBtn)
    protected void onPlus(View view) {
        ViewUtil.setViewClickedDelay(view);
        this.mValue++;
        setValue(this.mValue);
    }

    @OnClick(R.id.valueTv)
    protected void showEditTextDialog() {
        if (mMax > 1) {
            EditNumberDialog dialog = new EditNumberDialog(getContext(), this.mValue, this.mMin, this.mMax);
            dialog.setOnChangeListener(new OnValueChangeLister() {
                @Override
                public void changed(int value) {
                    setValue(value);
                }
            });
            dialog.show();
        }

    }

}
