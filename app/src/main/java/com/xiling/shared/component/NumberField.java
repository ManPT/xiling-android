package com.xiling.shared.component;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.view.KeyBoardEditText;
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
    protected KeyBoardEditText mValueTv;
    @BindView(R.id.parent_edit)
    LinearLayout parentEdit;

    private int mMin = 1;
    private int mMax = 999;
    //加入数量步进
    int mStep = 1;
    int editSize;//输入的数量
    int oldEditSize;

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

        mValueTv.setOnFinishComposingListener(new KeyBoardEditText.OnFinishComposingListener() {

            @Override
            public void finishComposing() {
                if (editSize == 0) {
                    mValue = mMin;
                } else if (mStep > 0 && editSize < mMax && editSize % mStep != 0) {
                    ToastUtil.error("本品一手批发，数量为" + mStep + "的倍数");
                }
                checkValidity(mValue);
                setValue(mValue);
            }
        });

        mValueTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String size = charSequence.toString();
                editSize = 0;
                if (!TextUtils.isEmpty(size)) {
                    editSize = Integer.valueOf(size);
                }

                if (editSize > 0 && editSize != oldEditSize) {
                    oldEditSize = editSize;
                    if (mStep > 0 && editSize % mStep == 0) {
                        mValue = editSize;
                    }
                    mValueTv.setText("" + editSize);
                    setButtonsEnabled();

                    mValueTv.setSelection(mValueTv.getText().length());//将光标移至文字末尾
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        // 确保最小值大于0
        if (min > 0) {
            mMin = min;
            mStep = min;
        } else {
            mMin = 1;
            mStep = 1;
        }

        mMax = max;
        if (mValue > mMax) {
            mValue = mMax;
        } else if (mValue < mMin) {
            mValue = mMin;
        }
        mValueTv.setText("" + this.mValue);
        //   setValue(mValue);
        setButtonsEnabled();
    }

    /**
     * 校验数量有效性
     */
    private void checkValidity(int value) {
      /*  if (mMax != 0 && value > mMax) {
            ToastUtil.error("商品数量已经达到最大值");
        } else*/
        if (value < mMin) {
            ToastUtil.error("商品数量已经达到最小值");
        }
        value = value < mMin ? mMin : value;
        this.mValue = value /*<= mMax ? value : mMax*/;
    }

    public void setValue(int value) {
        mValueTv.setText("" + value);
        setButtonsEnabled();
        if (this.mListener != null) {
            this.mListener.changed(value);
        }
    }

    /**
     * adapter 刷新时用
     *
     * @param value
     * @param isListener
     */
    public void setValue(int value, boolean isListener) {
        // value = value < mMin ? mMin : value;
        //   this.mValue = value <= mMax ? value : mMax;
        this.mValue = value;
        mValueTv.setText("" + this.mValue);
        if (isListener && this.mListener != null) {
            this.mListener.changed(this.mValue);
        }
        setButtonsEnabled();
    }


    private void setButtonsEnabled() {
        this.mMinusBtn.setEnabled(this.mValue > mMin);
        //  this.mPlusBtn.setEnabled(this.mValue < mMax);
        this.mPlusBtn.setEnabled(true);
    }

    /**
     * sku为null时，禁止改动数量
     */
    public void setNull() {
        this.mMinusBtn.setEnabled(false);
        this.mPlusBtn.setEnabled(false);
    }

    public void setOnChangeListener(OnValueChangeLister listener) {
        this.mListener = listener;
    }

    public void setEditable(boolean editable) {
        mValueTv.setEnabled(editable);
    }

    @OnClick(R.id.minusBtn)
    protected void onMinus() {
        if (mValue % mStep == 0) {
            mValue -= mStep;
        } else {
            mValue = mStep;
        }
        checkValidity(mValue);
        setValue(mValue);
    }

    @OnClick(R.id.plusBtn)
    protected void onPlus() {
        if (mValue % mStep == 0) {
            mValue += mStep;
        } else {
            mValue = mStep;
        }
        checkValidity(mValue);
        setValue(mValue);
    }

}
