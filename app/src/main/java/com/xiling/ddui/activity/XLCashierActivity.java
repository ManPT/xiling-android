package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.unionpay.UPPayAssistEx;
import com.xiling.R;
import com.xiling.ddui.adapter.XLBankPayAdapter;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.bean.WXPayBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.LargePayDialog;
import com.xiling.ddui.service.IBankService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.auth.Config;
import com.xiling.module.community.DateUtils;
import com.xiling.module.pay.PayMsg;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPayService;
import com.xiling.shared.util.AliPayUtils;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WePayUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;
import static com.xiling.shared.constant.Event.FINISH_ORDER;
import static com.xiling.shared.constant.Event.RECHARGE_SUCCESS;
import static com.xiling.shared.constant.Event.WEEK_CARD_PAY;
import static com.xiling.shared.service.contract.IPayService.CHANNEL_A_LI_PAY;
import static com.xiling.shared.service.contract.IPayService.CHANNEL_UNION_PAY;
import static com.xiling.shared.service.contract.IPayService.CHANNEL_WE_CHAT_PAY;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_CHARGE_MONEY;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_ORDER;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_WEEK_CARD;

/**
 * @auth 逄涛
 * 收银台
 */
public class XLCashierActivity extends BaseActivity {
    public static final int ADD_BAND_CODE = 1000;
    @BindView(R.id.rel_head)
    RelativeLayout relHead;
    @BindView(R.id.btn_pay_type)
    RelativeLayout btnPayType;
    @BindView(R.id.btn_add_bank)
    RelativeLayout btnAddBank;
    private IBankService mBankService;
    private IPayService mPayService;

    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_price_decimal)
    TextView tvPriceDecimal;

    @BindView(R.id.recycler_bank)
    RecyclerView recyclerBank;
    XLBankPayAdapter bankAdapter;
    List<BankListBean> bankListBeans = new ArrayList<>();
    @BindView(R.id.btn_pay)
    TextView btnPay;
    BankListBean mBankBean;
    LargePayDialog largePayDialog;
    double payMoney = 0; //支付金额
    long waitPayTimeMilli = 0;
    String key = "";
    String type;
    String weekSize;
    int blankMaxSize = 3;


    public static void jumpCashierActivity(Context context, String type, double payMoney, long waitPayTimeMilli, String key) {
        Intent intent = new Intent(context, XLCashierActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("payMoney", payMoney);
        intent.putExtra("waitPayTimeMilli", waitPayTimeMilli);
        intent.putExtra("key", key);
        context.startActivity(intent);
    }

    public static void jumpCashierActivity(Context context, String type, double payMoney, long waitPayTimeMilli, String key, String weekSize) {
        Intent intent = new Intent(context, XLCashierActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("payMoney", payMoney);
        intent.putExtra("waitPayTimeMilli", waitPayTimeMilli);
        intent.putExtra("key", key);
        intent.putExtra("weekSize", weekSize);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        String message = "";
        if (type.equals(PAY_TYPE_ORDER)) {
            message = "是否要放弃支付  订单会保留45分钟，请尽快支付";
        } else {
            message = "是否要放弃付款？";
        }

        D3ialogTools.showAlertDialog(context, message, "继续支付", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, "确认离开", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals(PAY_TYPE_ORDER)) {
                    //发送订单完成广播，通知页面关闭
                    XLOrderDetailsActivity.jumpOrderDetailsActivity(context, key);
                }
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlcashier);
        ButterKnife.bind(this);
        setTitle("收银台");
        setLeftBlack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBankService = ServiceManager.getInstance().createService(IBankService.class);
        mPayService = ServiceManager.getInstance().createService(IPayService.class);

        if (Config.systemConfigBean != null) {
            blankMaxSize = Config.systemConfigBean.getPayCardNumber();
        }
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            payMoney = getIntent().getDoubleExtra("payMoney", 0);
            waitPayTimeMilli = getIntent().getLongExtra("waitPayTimeMilli", 0);
            key = getIntent().getStringExtra("key");
            weekSize = getIntent().getStringExtra("weekSize");
        }

        if (type.equals(PAY_TYPE_WEEK_CARD)) {
            btnPayType.setVisibility(View.GONE);
        } else {
            btnPayType.setVisibility(View.VISIBLE);
        }

        NumberHandler.setPriceText(payMoney, tvPrice, tvPriceDecimal);
        startCountDown();

        WePayUtils.initWePay();
        initBank();
        getBankList();
    }

    /**
     * 启动倒计时
     */
    private void startCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(waitPayTimeMilli, 1000) {
            @Override
            public void onTick(long l) {
                DateUtils.setCountDownTimeStrng(l, tvHour, tvMinute, tvSecond);
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }

    private void initBank() {
        bankListBeans.clear();
        BankListBean wxBank = new BankListBean();
        wxBank.setBankLogoGround(R.drawable.icon_pay_wx);
        wxBank.setBankName("微信支付");
        bankListBeans.add(wxBank);

        BankListBean zfbBank = new BankListBean();
        zfbBank.setBankLogoGround(R.drawable.icon_pay_zfb);
        zfbBank.setBankName("支付宝");
        bankListBeans.add(zfbBank);

        if (bankAdapter == null) {
            recyclerBank.setLayoutManager(new LinearLayoutManager(this));
            bankAdapter = new XLBankPayAdapter(bankListBeans);
            recyclerBank.setAdapter(bankAdapter);
            bankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    bankAdapter.setSelectPosition(position);
                    mBankBean = bankListBeans.get(position);
                    btnPay.setText(bankListBeans.get(position).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(payMoney));
                }
            });
        }

    }

    private void getBankList() {
        APIManager.startRequest(mBankService.getBankCardList(), new BaseRequestListener<List<BankListBean>>(this) {

            @Override
            public void onSuccess(List<BankListBean> result) {
                super.onSuccess(result);
                if (result.size() > 0) {
                    initBank();
                    int position = bankListBeans.size();
                    bankListBeans.addAll(result);
                    bankAdapter.setSelectPosition(position);
                    mBankBean = bankListBeans.get(position);
                    btnPay.setText(bankListBeans.get(position).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(payMoney));

                    if (result.size() >= blankMaxSize) {
                        btnAddBank.setVisibility(View.GONE);
                    } else {
                        btnAddBank.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnPay.setText(bankListBeans.get(0).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(payMoney));
                    mBankBean = bankListBeans.get(0);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @OnClick({R.id.btn_pay, R.id.btn_add_bank, R.id.btn_pay_type})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view, 2000);
        switch (view.getId()) {
            case R.id.btn_pay:
                if (mBankBean.getBankName().equals("微信支付")) {
                    addPay(key, CHANNEL_WE_CHAT_PAY);
                } else if (mBankBean.getBankName().equals("支付宝")) {
                    addPay(key, CHANNEL_A_LI_PAY);
                } else {
                    addPay(key, CHANNEL_UNION_PAY);
                }
                break;
            case R.id.btn_add_bank:

                //  bankListBeans.size()  -2 代表银行卡数量，减去微信支付和支付宝支付
                if (bankListBeans != null && bankListBeans.size() - 2 < blankMaxSize) {
                    startActivityForResult(new Intent(context, XLAddBankActivity.class), 0);
                } else {
                    ToastUtil.error("银行卡已经添加到最大数量了");
                }

                break;
            case R.id.btn_pay_type:
                largePayDialog = new LargePayDialog(context, type, key, weekSize);
                largePayDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_BAND_CODE) {
            //添加银行卡
            getBankList();
        } else {
            //银联支付返回
            if (data == null) {
                return;
            }

            String msg = "";
            /*
             * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
             */
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BANK_SUCCEED));
            } else if (str.equalsIgnoreCase("fail")) {
                EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BANK_FAIL, msg));
            } else if (str.equalsIgnoreCase("cancel")) {
                msg = "用户取消了支付";
                EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BANK_FAIL, msg));
            }
        }
    }


    public static class EXT {
        private String UNION_PAY_CARD_ID;
        private String NUMBER;

        public String getUNION_PAY_CARD_ID() {
            return UNION_PAY_CARD_ID;
        }

        public void setUNION_PAY_CARD_ID(String UNION_PAY_CARD_ID) {
            this.UNION_PAY_CARD_ID = UNION_PAY_CARD_ID;
        }

        public String getNUMBER() {
            return NUMBER;
        }

        public void setNUMBER(String NUMBER) {
            this.NUMBER = NUMBER;
        }
    }


    /**
     * 创建流水单号
     */
    private void addPay(String key, final String channel) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("type", type);
        params.put("channel", channel);
        params.put("device", "ANDROID");
        if (channel.equals(CHANNEL_UNION_PAY)) {
            if (mBankBean != null) {
                EXT ext = new EXT();
                ext.UNION_PAY_CARD_ID = mBankBean.getId();
                params.put("ext", ext);
            }
        } else if (type.equals(PAY_TYPE_WEEK_CARD)) {
            EXT ext = new EXT();
            ext.NUMBER = weekSize;
            params.put("ext", ext);
        }

        APIManager.startRequest(mPayService.addPay(APIManager.buildJsonBody(params)), new BaseRequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                //获取流水单号进行支付
                pay(result, channel);
            }


            @Override
            public void onError(Throwable e, String businessCode) {
                super.onError(e);
                if (!TextUtils.isEmpty(businessCode)) {
                    if (businessCode.equals("un-auth")) {
                        UserManager.getInstance().isRealAuth(context, null);
                    }
                } else {
                    ToastUtil.error(e.getMessage());
                }
            }

        });
    }

    private void pay(String orderCode, final String channel) {
        APIManager.startRequest(mPayService.pay(orderCode), new BaseRequestListener<String>() {

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                switch (channel) {
                    case CHANNEL_A_LI_PAY:
                        AliPayUtils.pay(XLCashierActivity.this, result);
                        break;
                    case CHANNEL_WE_CHAT_PAY:
                        WXPayBean wxPayBean = new Gson().fromJson(result, WXPayBean.class);
                        WePayUtils.startPay(wxPayBean);
                        break;
                    case CHANNEL_UNION_PAY:
                        //银联支付
                        UPPayAssistEx.startPay(context, null, null, result, "00");
                        break;
                }

            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(PayMsg msgStatus) {
        switch (msgStatus.getAction()) {
            case PayMsg.ACTION_WXPAY_SUCCEED:
            case PayMsg.ACTION_ALIPAY_SUCCEED:
            case PayMsg.ACTION_BANK_SUCCEED:
                if (type.equals(PAY_TYPE_ORDER)) {
                    //发送订单完成广播，通知页面关闭
                    EventBus.getDefault().post(new EventMessage(FINISH_ORDER));
                   // XLOrderDetailsActivity.jumpOrderDetailsActivity(context, key);
                    //拆单需求变更，跳转订单列表
                    OrderListActivit.jumpOrderList(context,ORDER_WAIT_SHIP);
                    ToastUtil.showSuccessToast(context, "支付成功");
                } else if (type.equals(PAY_TYPE_CHARGE_MONEY)) {
                    EventBus.getDefault().post(new EventMessage(RECHARGE_SUCCESS,1));
                } else if (type.equals(PAY_TYPE_WEEK_CARD)) {
                    EventBus.getDefault().post(new EventMessage(WEEK_CARD_PAY));
                    ToastUtil.showSuccessToast(context, "支付成功");
                }

                finish();
                break;
            case PayMsg.ACTION_WXPAY_FAIL:
            case PayMsg.ACTION_ALIPAY_FAIL:
            case PayMsg.ACTION_BANK_FAIL:
                ToastUtil.showFailToast(context, "支付失败");
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        switch (message.getEvent()) {
            case FINISH_ORDER:
            case RECHARGE_SUCCESS:
                finish();
                break;
        }
    }

}
