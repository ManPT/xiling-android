package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.OrderSkuAdapter;
import com.xiling.ddui.bean.AccountInfo;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.ddui.bean.CouponBean;
import com.xiling.ddui.bean.OrderAddBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.CouponSelectorDialog;
import com.xiling.ddui.custom.popupwindow.PayPopWindow;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.address.AddressListActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;
import static com.xiling.shared.constant.Event.FINISH_ORDER;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_ORDER;

/**
 * @author 逄涛
 * 确认订单页面
 */
public class ConfirmationOrderActivity extends BaseActivity {
    public static final String SKULIST = "skuList";
    public static final String ORDER_SOURCE = "orderSource";
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_balance_use)
    TextView tvBalanceUse;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @BindView(R.id.tv_balance_price)
    TextView tvBalancePrice;
    @BindView(R.id.tv_freight_price)
    TextView tvFreightPrice;
    @BindView(R.id.tv_need_price)
    TextView tvNeedPrice;
    @BindView(R.id.tv_need_price_decimal)
    TextView tvNeedPriceDecimal;
    @BindView(R.id.iv_arrow2)
    ImageView ivArrow2;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.switch_balance)
    ImageView switchBalance;
    @BindView(R.id.tv_identity_price)
    TextView tvIdentityPrice;
    @BindView(R.id.et_subscribers_name)
    EditText etSubscribersName;
    @BindView(R.id.et_subscribers_id)
    EditText etSubscribersId;
    @BindView(R.id.ll_subscribers_info)
    LinearLayout llSubscribersInfo;
    @BindView(R.id.ll_purchase_instructions)
    LinearLayout llPurchaseInstructions;
    @BindView(R.id.ll_taxation)
    RelativeLayout llTaxation;
    @BindView(R.id.tv_taxation)
    TextView tvTaxation;

    private CouponBean couponBean = null;
    ArrayList<SkuListBean> skuList;
    IOrderService mOrderService;
    IAddressService mAddressService;
    INewUserService mUserService;

    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_address_detail)
    TextView tvAddressDetail;
    @BindView(R.id.tv_contacts)
    TextView tvContacts;
    @BindView(R.id.btn_address)
    RelativeLayout btnAddress;

    OrderSkuAdapter orderSkuAdapter;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.recycler_sku)
    RecyclerView recyclerSku;

    private AddressListBean.DatasBean mAddress;
    private boolean isBalance = false;
    private String balancePassword = "";
    private double useBalance = 0;
    private double totlaPrice = 0;
    private AccountInfo accountInfo = null;
    private int orderSource = 1;
    private String orderer = "";//跨境购-订购人
    private String identityCard = "";//跨境购-身份证
    boolean isCross;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);
        ButterKnife.bind(this);
        setTitle("确认订单");
        setLeftBlack();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        if (getIntent() != null) {
            skuList = getIntent().getParcelableArrayListExtra(SKULIST);
            orderSource = getIntent().getIntExtra(ORDER_SOURCE, 1);
        }


        if (skuList == null) {
            ToastUtil.error("商品列表为空");
            return;
        }
        orderer = SharedPreferenceUtil.getInstance().getString("orderer");
        identityCard = SharedPreferenceUtil.getInstance().getString("identityCard");
        if (!TextUtils.isEmpty(orderer)) {
            etSubscribersName.setText(orderer);
        }
        if (!TextUtils.isEmpty(identityCard)) {
            etSubscribersId.setText(identityCard);
        }
        recyclerSku.setLayoutManager(new LinearLayoutManager(this));
        orderSkuAdapter = new OrderSkuAdapter();
        recyclerSku.setAdapter(orderSkuAdapter);

        getAccountInfo();
        getDefaultAddress();
    }

    private void setAddress(AddressListBean.DatasBean address) {
        mAddress = address;
        if (address == null) {
            llAddress.setVisibility(View.GONE);
            tvContacts.setVisibility(View.GONE);
        } else {
            llAddress.setVisibility(View.VISIBLE);
            tvContacts.setVisibility(View.VISIBLE);

            ivDefault.setVisibility(address.getIsDefault() == 1 ? View.VISIBLE : View.GONE);
            tvAddress.setText(address.getFullRegion());
            tvAddressDetail.setText(address.getDetail());
            tvContacts.setText(address.getContactAndPhone());
        }
    }

    /**
     * 获取默认地址
     */
    private void getDefaultAddress() {
        APIManager.startRequest(mAddressService.getDefaultAddress(), new BaseRequestListener<AddressListBean.DatasBean>() {
            @Override
            public void onSuccess(AddressListBean.DatasBean result) {
                setAddress(result);
            }

            @Override
            public void onError(Throwable e) {
                setAddress(null);
            }
        });
    }


    private void upDataBalance() {
        if (isBalance) {
            tvBalanceUse.setVisibility(View.VISIBLE);
            tvBalanceUse.setText("使用 ¥" + NumberHandler.reservedDecimalFor2(useBalance));
            NumberHandler.setPriceText(totlaPrice - useBalance, tvNeedPrice, tvNeedPriceDecimal);
            tvBalancePrice.setText("-¥" + useBalance);
        } else {
            tvBalanceUse.setText("使用 ¥" + NumberHandler.reservedDecimalFor2(0));
            tvBalanceUse.setVisibility(View.GONE);
            tvBalancePrice.setText("-¥" + NumberHandler.reservedDecimalFor2(0));
            NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
        }
    }


    /**
     * 获取确认订单数据
     */
    private void getConfirmOrder(final AccountInfo accountInfo) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", skuList);
        if (couponBean != null) {
            params.put("couponId", couponBean.getId());
        }
        APIManager.startRequest(mOrderService.getConfirmOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<OrderDetailBean>(this) {
            @Override
            public void onSuccess(OrderDetailBean result) {
                super.onSuccess(result);
                orderSkuAdapter.setNewData(result.getStores());
                isCross(result.getIsCross() == 1, result);
                if (result != null) {
                    useBalance = result.getTotalPrice();
                    totlaPrice = result.getTotalPrice();
                    if (accountInfo != null) {
                        //如果余额小于商品总价，使用金额为余额
                        if (useBalance > accountInfo.getBalance()) {
                            useBalance = accountInfo.getBalance();
                        }
                    } else {
                        NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
                    }
                    upDataBalance();
                    switch (UserManager.getInstance().getCommodityLevel()) {
                        case 0:
                            //普通会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_register);
                            break;
                        case 1:
                            //VIP
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_ordinary);
                            break;
                        case 2:
                            //sVip会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_vip);
                            break;
                        case 3:
                            //黑卡会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_black);
                            break;
                        case 4:
                            //临时SVIP
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_vip_ex);
                            break;
                        case 5:
                            //临时黑卡会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_black_ex);
                            break;
                    }

                    tvGoodsPrice.setText("¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalRetailPrice()));
                    tvDiscountPrice.setText("¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalPrice()));
                    tvCouponPrice.setText("-¥" + NumberHandler.reservedDecimalFor2(result.getCouponReductionPrice()));
                    tvFreightPrice.setText("+¥" + NumberHandler.reservedDecimalFor2(result.getFreight()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void isCross(boolean isCross, OrderDetailBean result) {
        this.isCross = isCross;
        if (isCross) {
            llSubscribersInfo.setVisibility(View.VISIBLE);
            llPurchaseInstructions.setVisibility(View.VISIBLE);
            llTaxation.setVisibility(View.VISIBLE);
            tvTaxation.setText("+¥" + NumberHandler.reservedDecimalFor2(result.getTaxes()));
        } else {
            llSubscribersInfo.setVisibility(View.GONE);
            llPurchaseInstructions.setVisibility(View.INVISIBLE);
            llTaxation.setVisibility(View.GONE);
        }

    }

    /**
     * 获取账户信息（余额）
     */
    private void getAccountInfo() {
        APIManager.startRequest(mUserService.getAccountInfo(), new BaseRequestListener<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo result) {
                super.onSuccess(result);
                if (result != null) {
                    tvBalance.setText("共 ¥" + NumberHandler.reservedDecimalFor2(result.getBalance()));
                    accountInfo = result;
                    getConfirmOrder(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("账户信息获取失败");
                getConfirmOrder(null);
            }
        });
    }


    private void switchBalance(View view) {
        isBalance = !isBalance;
        view.setSelected(isBalance);
        upDataBalance();
    }


    /**
     * 校验是否设置余额密码
     */
    private void hasPassword(final View view) {
        APIManager.startRequest(mUserService.hasPassword(), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    PayPopWindow payPopWindow = new PayPopWindow(context);
                    payPopWindow.setOnPasswordEditListener(new PayPopWindow.OnPasswordEditListener() {
                        @Override
                        public void onEditFinish(String password) {
                            //在此校验输入的密码
                            checkPassword(password, view);
                        }
                    });
                    payPopWindow.show();
                } else {
                    startActivity(new Intent(context, TransactionPasswordActivity.class));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @OnClick({R.id.btn_address, R.id.switch_balance, R.id.btn_coupon, R.id.btn_send, R.id.btn_purchase_instructions})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_address://更换地址
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra("action", Key.SELECT_ADDRESS);
                startActivityForResult(intent, 0);
                break;
            case R.id.switch_balance: // 使用账户余额
                if (accountInfo.getBalance() == 0) {
                    return;
                }
                if (!isBalance) {
                    //打开
                    //先校验是否有支付密码，如果没有跳转设置密码的界面
                    hasPassword(view);
                } else {
                    //关闭
                    switchBalance(view);
                    balancePassword = "";
                }

                break;
            case R.id.btn_coupon:// 选择优惠券
                getCouponList();
                break;
            case R.id.btn_send:
                //提交订单
                subOrder();
                break;
            case R.id.btn_purchase_instructions:
                //用户需知
                WebViewActivity.jumpUrl(context, "跨境商品用户购买须知", H5UrlConfig.CROSS_NEED_NOTE);
                break;
        }

    }

    ICouponService mCouponService;

    /**
     * 优惠券列表
     */
    private void getCouponList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", skuList);
        if (mCouponService == null) {
            mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
        }
        APIManager.startRequest(mCouponService.getCouponList(APIManager.buildJsonBody(params)), new BaseRequestListener<List<CouponBean>>() {
            @Override
            public void onSuccess(List<CouponBean> result) {
                if (result != null && result.size() > 0) {
                    CouponSelectorDialog dialog = new CouponSelectorDialog(context, result);
                    if (couponBean != null) {
                        dialog.setSelect(couponBean);
                    }

                    dialog.setOnCouponSelectListener(new CouponSelectorDialog.OnCouponSelectListener() {
                        @Override
                        public void onCouponSelected(CouponBean couponBean) {
                            ConfirmationOrderActivity.this.couponBean = couponBean;
                            getConfirmOrder(accountInfo);
                            tvCoupon.setText(couponBean.getName());
                        }

                        @Override
                        public void onCancel() {
                            //取消时，清空优惠券
                            couponBean = null;
                            getConfirmOrder(accountInfo);
                            tvCoupon.setText("");
                        }
                    });
                    dialog.show();
                } else {
                    ToastUtil.error("暂无可用优惠券");
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    /**
     * 提交订单
     */
    private void subOrder() {

        if (mAddress == null) {
            ToastUtil.error("请选择收货地址");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("useBalance", isBalance);
        params.put("balance", isBalance ? useBalance * 100 : 0);
        if (isBalance) {
            params.put("balancePassword", balancePassword);
        }

        if (isCross) {
            orderer = etSubscribersName.getText().toString();
            identityCard = etSubscribersId.getText().toString();
            params.put("orderer", orderer);
            params.put("identityCard", identityCard);
        }

        params.put("products", skuList);
        params.put("device", 1);
        params.put("addressId", mAddress.getAddressId());
        if (couponBean != null) {
            params.put("couponId", couponBean.getId());
        }
        params.put("orderSource", orderSource);

        APIManager.startRequest(mOrderService.addOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<OrderAddBean>(this) {
            @Override
            public void onSuccess(OrderAddBean result) {
                super.onSuccess(result);
                if (result.isWaitPay()) {
                    getOrderDetails(result.getOrderId());
                } else {
                    OrderListActivit.jumpOrderList(context, ORDER_WAIT_SHIP);
                }
                EventBus.getDefault().post(new EventMessage(FINISH_ORDER));
            }

            @Override
            public void onError(Throwable e, String businessCode) {
                super.onError(e);
                if (!TextUtils.isEmpty(businessCode)) {
                    if (businessCode.equals("un-auth")) {
                        UserManager.getInstance().isRealAuth(context, null);
                    } else if (businessCode.equals("coupon")) {
                        //优惠券已使用
                        ToastUtil.error(e.getMessage());
                        couponBean = null;
                        getConfirmOrder(accountInfo);
                        tvCoupon.setText("");
                    }
                } else {
                    ToastUtil.error(e.getMessage());
                }
            }
        });
    }

    private void getOrderDetails(final String orderId) {
        APIManager.startRequest(mOrderService.getOrderDetails(orderId), new BaseRequestListener<XLOrderDetailsBean>() {
            @Override
            public void onSuccess(XLOrderDetailsBean result) {
                super.onSuccess(result);
                if (result.getOrderStatusUs().equals(ORDER_WAIT_PAY)) {
                    //如果是待支付，跳转收银台
                    XLCashierActivity.jumpCashierActivity(context, PAY_TYPE_ORDER, result.getPayMoney(), result.getWaitPayTimeMilli(), result.getOrderId());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            AddressListBean.DatasBean address = data.getParcelableExtra("address");
            if (address != null) {
                setAddress(address);
            }
        }

    }

    /**
     * 校验余额密码
     *
     * @param password
     */
    private void checkPassword(final String password, final View switchView) {
        APIManager.startRequest(mUserService.checkBalancePassword(password), new BaseRequestListener<Object>(context) {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                switchBalance(switchView);
                balancePassword = password;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                balancePassword = "";
                D3ialogTools.showAlertDialog(context, e.getMessage(), "忘记密码", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, TransactionPasswordActivity.class));
                    }
                }, "重新输入", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PayPopWindow payPopWindow = new PayPopWindow(context);
                        payPopWindow.setOnPasswordEditListener(new PayPopWindow.OnPasswordEditListener() {
                            @Override
                            public void onEditFinish(String password) {
                                //在此校验输入的密码
                                checkPassword(password, switchView);
                            }
                        });
                        payPopWindow.show();
                    }
                });

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        switch (message.getEvent()) {
            case FINISH_ORDER:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
                break;
            case saveAddress:
                //修改收获地址
                AddressListBean.DatasBean newAddress = (AddressListBean.DatasBean) message.getData();
                if (mAddress != null && newAddress != null) {
                    if (mAddress.getAddressId().equals(newAddress.getAddressId())) {
                        mAddress = newAddress;
                        setAddress(mAddress);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderer = etSubscribersName.getText().toString();
        identityCard = etSubscribersId.getText().toString();
        SharedPreferenceUtil.getInstance().putString("orderer", orderer);
        SharedPreferenceUtil.getInstance().putString("identityCard", identityCard);
    }
}
