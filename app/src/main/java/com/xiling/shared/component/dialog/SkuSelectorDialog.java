package com.xiling.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.adapter.SkuSelectChildAdapter;
import com.xiling.ddui.adapter.SkuSelectParentAdapter;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ShopUtils;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.component.NumberField;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.util.CommonUtil;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkuSelectorDialog extends Dialog {

    @BindView(R.id.recycler_parent)
    protected RecyclerView mParentRecyclerView;
    @BindView(R.id.thumbIv)
    protected SimpleDraweeView mThumbIv;
    @BindView(R.id.buyNowBtn)
    protected TextView mBuyNowBtn;
    @BindView(R.id.addToCartBtn)
    protected TextView mAddToCartBtn;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;
    @BindView(R.id.numberField)
    protected NumberField mNumberField;


    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_discount_price_decimal)
    TextView tvDiscountPriceDecimal;
    @BindView(R.id.tv_minPrice)
    TextView tvMinPrice;
    @BindView(R.id.tv_minMarketPrice)
    TextView tvMinMarketPrice;
    @BindView(R.id.tvSkuName)
    TextView tvSkuName;
    protected ProductNewBean mSpuInfo;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.iv_rate)
    TextView ivRate;
    private OnSelectListener mSelectListener;
    List<ProductNewBean.SkusBean> skusBeanList;
    private ProductNewBean.SkusBean skuBean;
    private int selectCount = 1;
    private SkuSelectParentAdapter parentAdapter;


    private List<ProductNewBean.PropertiesBean> parentList = new ArrayList<>();
    private HashMap<String, String> parentNameMap = new HashMap<>();
    private HashMap<String, ProductNewBean.PropertiesBean.PropertyValuesBean> childMap = new HashMap<>();
    private HashMap<String, ProductNewBean.PropertiesBean.PropertyValuesBean> selectMap = new HashMap<>();

    public void setmAction(int mAction) {
        this.mAction = mAction;
        selectAction();
    }

    public void setmSpuInfo(ProductNewBean mSpuInfo) {
        this.mSpuInfo = mSpuInfo;
        initViews();
    }


    /**
     * 用来判断是 加入购物车还是立即购买，如果是0，则进入选择规格流程
     * 0 - 选择规格
     * 1 - 加入购物车
     * 2 - 立即购买
     */
    private int mAction;
    public static final int ACTION_SELECT = 0;
    public static final int ACTION_CARD = 1;
    public static final int ACTION_SHOPPING = 2;

    public SkuSelectorDialog(Context context, ProductNewBean product) {
        this(context, 0);
        this.mSpuInfo = product;
    }

    private SkuSelectorDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    public SkuSelectorDialog(Context context, ProductNewBean mProduct, int action) {
        this(context, mProduct);
        this.mAction = action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_sku_selector);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        initViews();
    }


    private void setSkuName(final String skuName) {
        tvSkuName.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(skuName)) {
                    tvSkuName.setText("请选择规格");
                } else {
                    tvSkuName.setText("已选择 " + skuName);
                }
            }
        });


    }

    /**
     * 判断状态，如果是选择规格，隐藏确认按钮，否则显示确认按钮
     */
    private void selectAction() {
        if (mAction == ACTION_SELECT) {
            mConfirmBtn.setVisibility(View.GONE);
            mAddToCartBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setVisibility(View.VISIBLE);
        } else {
            mConfirmBtn.setVisibility(View.VISIBLE);
            mAddToCartBtn.setVisibility(View.GONE);
            mBuyNowBtn.setVisibility(View.GONE);
        }
    }

    private void upDateSku(boolean isField) {
        ivRate.setBackgroundResource(UserManager.getInstance().getDiscountIconForUser(true));
        if (skuBean != null) {
            setSkuName(skuBean.getPropertyValues());
            GlideUtils.loadImage(getContext(), mThumbIv, skuBean.getThumbUrlForShopNow());
            //优惠价，需要根据用户等级展示不同价格
            NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(skuBean), tvDiscountPrice, tvDiscountPriceDecimal);

            String mStatus = ShopUtils.checkShopStatus(skuBean.getStatus(), skuBean.getStock());
            //售价
            tvMinPrice.setText("¥" + NumberHandler.reservedDecimalFor2(skuBean.getRetailPrice()));
            //划线价
            tvMinMarketPrice.setText("¥" + NumberHandler.reservedDecimalFor2(skuBean.getMarketPrice()));
            if (!isField) {
                mNumberField.setLimit(skuBean.getStep(), skuBean.getStock());
                mNumberField.setValue(skuBean.getStep());
                if (skuBean.getStep() > 1) {
                    tvStep.setVisibility(View.VISIBLE);
                    tvStep.setText("批采商品数量必须为" + skuBean.getStep() + "的倍数");
                } else {
                    tvStep.setVisibility(View.GONE);
                }

                if (skuBean.getStep() > 1 && skuBean.getShowShippingFree() > 0) {
                    tvFreight.setVisibility(View.VISIBLE);
                    tvFreight.setText("含运费：¥" + NumberHandler.reservedDecimalFor2(skuBean.getShowShippingFree()));
                } else {
                    tvFreight.setVisibility(View.GONE);
                }


            }

            tvStock.setText("库存" + skuBean.getStock() + "件");
        } else {
            setSkuName("");
            tvStep.setVisibility(View.GONE);
            GlideUtils.loadImage(getContext(), mThumbIv, mSpuInfo.getThumbUrl());
            //优惠价，需要根据用户等级展示不同价格
            NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(mSpuInfo), tvDiscountPrice, tvDiscountPriceDecimal);
            //售价
            tvMinPrice.setText("¥" + NumberHandler.reservedDecimalFor2(mSpuInfo.getMinPrice()));
            //划线价
            tvMinMarketPrice.setText("¥" + NumberHandler.reservedDecimalFor2(mSpuInfo.getMinMarketPrice()));
            tvStock.setText("");
            tvFreight.setVisibility(View.GONE);
            mNumberField.setLimit(1, 1);
            mNumberField.setNull();
        }
    }


    /**
     * 根据选中的规格匹配商品
     */
    private String matchSkuBean() {
        //
        if (selectMap.size() == parentList.size()) {
            List<ProductNewBean.SkusBean> mSkuBeanList = new ArrayList<>();
            List<ProductNewBean.SkusBean> delectList = new ArrayList<>();
            mSkuBeanList.addAll(skusBeanList);

            for (ProductNewBean.PropertiesBean.PropertyValuesBean valuesBean : selectMap.values()) {
                String valueId = valuesBean.getPropertyValueId();
                delectList.clear();
                for (ProductNewBean.SkusBean skusBean : skusBeanList) {
                    if (!skusBean.getPropertyValueIds().contains(valueId)) {
                        delectList.add(skusBean);
                    }
                }
                mSkuBeanList.removeAll(delectList);
            }

            if (mSkuBeanList.size() == 1) {
                skuBean = mSkuBeanList.get(0);
            }
        } else {
            skuBean = null;
            for (ProductNewBean.PropertiesBean parentBean : parentList) {
                if (selectMap.get(parentBean.getPropertyId()) == null) {
                    return "请选择 " + parentBean.getPropertyName();
                }
            }
        }
        return "";
    }


    /**
     * 校验规格是否可选
     *
     * @return
     */
    private void checkSkuBean(String propertyValueId, boolean isSelect) {
        if (propertyValueId.isEmpty()) {
            for (ProductNewBean.PropertiesBean.PropertyValuesBean childBean : childMap.values()) {
                childBean.setNeedSelect(true);
            }
        } else {
            for (ProductNewBean.SkusBean skusBean : skusBeanList) {
                if (skusBean.getPropertyValueIds().contains(propertyValueId)) {
                    String[] valueIds = skusBean.getPropertyValueIds().split(",");
                    for (String id : valueIds) {
                        if (!id.equals(propertyValueId)) {
                            ProductNewBean.PropertiesBean.PropertyValuesBean propertyValuesBean = childMap.get(id);
                            if (propertyValuesBean != null) {
                                String mStatus = ShopUtils.checkShopStatus(skusBean.getStatus(), skusBean.getStock());
                                if (isSelect) {
                                    propertyValuesBean.setNeedSelect(TextUtils.isEmpty(mStatus));
                                } else {
                                    if (!TextUtils.isEmpty(mStatus)) {
                                        propertyValuesBean.setNeedSelect(true);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        parentAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        selectAction();
        skusBeanList = mSpuInfo.getSkus();
        parentList = mSpuInfo.getProperties();
        if (parentList == null || parentList.size() == 0) {
            ToastUtil.error("规格列表为空");
            return;
        }

        childMap.clear();
        parentNameMap.clear();
        for (ProductNewBean.PropertiesBean parentBean : parentList) {
            parentNameMap.put(parentBean.getPropertyId(), parentBean.getPropertyName());
            for (ProductNewBean.PropertiesBean.PropertyValuesBean childBean : parentBean.getPropertyValues()) {
                childMap.put(childBean.getPropertyValueId(), childBean);
            }
        }


        mNumberField.setOnChangeListener(new OnValueChangeLister() {
            @Override
            public void changed(int value) {
                if (selectCount != value && value > 0) {
                    selectCount = value;
                    upDateSku(true);
                }
            }
        });
        tvMinMarketPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvMinMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        initRecyclerView();
        upDateSku(false);
        if (skuBean != null) {
            selectCount = skuBean.getStep();
        }
        getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
    }


    private void initRecyclerView() {
        if (parentAdapter == null) {
            LinearLayoutManager parentLayoutManager = new LinearLayoutManager(getContext());
            parentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            mParentRecyclerView.setHasFixedSize(true);
            mParentRecyclerView.setNestedScrollingEnabled(false);
            mParentRecyclerView.setLayoutManager(parentLayoutManager);
            parentAdapter = new SkuSelectParentAdapter(R.layout.item_sku_select_parent, parentList);
            /**
             * 如果是单规格，默认选中
             */
            if (parentList.size() == 1) {
                if (parentList.get(0).getPropertyValues().size() == 1) {
                    parentAdapter.setSelectChildId(parentList.get(0).getPropertyValues().get(0).getPropertyValueId());
                    selectMap.put(parentList.get(0).getPropertyId(), parentList.get(0).getPropertyValues().get(0));
                    matchSkuBean();
                }
            }
            mParentRecyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getContext(), 10), ScreenUtils.dip2px(getContext(), 10)));
            parentAdapter.setOnSelectListener(new SkuSelectParentAdapter.OnSelectListener() {
                @Override
                public void selectItem(ProductNewBean.PropertiesBean parentBean, ProductNewBean.PropertiesBean.PropertyValuesBean childBean, SkuSelectChildAdapter childAdapter) {
                    if (!childAdapter.getSelectId().equals(childBean.getPropertyValueId())) {
                        //选中
                        childAdapter.setSelectId(childBean.getPropertyValueId());
                        selectMap.put(parentBean.getPropertyId(), childBean);
                        //校验新的数据
                        checkSkuBean(childBean.getPropertyValueId(), true);
                    } else {
                        //取消选中
                        childAdapter.setSelectId("");
                        selectMap.remove(parentBean.getPropertyId());
                        //校验新的数据
                        checkSkuBean(childBean.getPropertyValueId(), false);
                    }
                    matchSkuBean();
                    upDateSku(false);
                    parentAdapter.notifyDataSetChanged();


                }
            });
            mParentRecyclerView.setAdapter(parentAdapter);
        } else {
            parentAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.tv_close)
    protected void onClose() {
        if (skuBean != null && mSelectListener != null) {
            mSelectListener.onClose(skuBean);
        }
        dismiss();
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    @OnClick({R.id.addToCartBtn, R.id.buyNowBtn, R.id.confirmBtn})
    public void onViewClicked(View view) {
        String error = matchSkuBean();
        if (!TextUtils.isEmpty(error)) {
            ToastUtil.error(error);
            return;
        }

        switch (view.getId()) {
            case R.id.addToCartBtn:
                if (mSelectListener != null) {
                    mSelectListener.joinShopCart(skuBean.getSkuId(), skuBean, selectCount);
                }
                dismiss();
                break;
            case R.id.buyNowBtn:
                if (mSelectListener != null) {
                    mSelectListener.buyItNow(skuBean.getSkuId(), skuBean, selectCount);
                }
                dismiss();
                break;
            case R.id.confirmBtn:
                if (mAction == ACTION_CARD) {
                    mSelectListener.joinShopCart(skuBean.getSkuId(), skuBean, selectCount);
                } else if (mAction == ACTION_SHOPPING) {
                    mSelectListener.buyItNow(skuBean.getSkuId(), skuBean, selectCount);
                }
                dismiss();
                break;
        }
    }


    public interface OnSelectListener {
        void onClose(ProductNewBean.SkusBean skusBean);

        //加入购物车
        void joinShopCart(String skuId, ProductNewBean.SkusBean skusBean, int selectCount);

        //立即购买
        void buyItNow(String propertyIds, ProductNewBean.SkusBean skusBean, int selectCount);
    }

}
