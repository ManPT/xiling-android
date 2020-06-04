package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.tools.GlideEngine;
import com.xiling.ddui.tools.PhotoTools;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.community.DateUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.service.contract.IPayService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bigkoo.pickerview.TimePickerView.Type.HOURS_MINS;
import static com.bigkoo.pickerview.TimePickerView.Type.YEAR_MONTH_DAY;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;
import static com.xiling.shared.constant.Event.FINISH_ORDER;
import static com.xiling.shared.constant.Event.RECHARGE_SUCCESS;
import static com.xiling.shared.service.contract.IPayService.CHANNEL_OFFLINE_PAY;
import static com.xiling.shared.service.contract.IPayService.CHANNEL_UNION_PAY;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_CHARGE_MONEY;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_ORDER;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_WEEK_CARD;

/**
 * 大额支付，上传凭证
 */
public class UploadCredentalsActivity extends BaseActivity {
    IPayService iPayService;

    @BindView(R.id.et_full_name)
    EditText etFullName;
    @BindView(R.id.et_account_pay)
    EditText etAccountPay;
    @BindView(R.id.tv_time_pay)
    TextView tvTimePay;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_work_number)
    TextView tvWorkNumber;
    @BindView(R.id.et_remarks)
    EditText etRemarks;

    private String key;
    private String type;

    private String fullName, accountPay, remarks;
    private String payTime = "";
    private String uploadImg = "";
    String weekSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_credentals);
        ButterKnife.bind(this);
        iPayService = ServiceManager.getInstance().createService(IPayService.class);
        setTitle("凭证信息");
        setLeftBlack();
        initView();
    }


    private void initView() {
        if (getIntent() != null) {
            key = getIntent().getStringExtra("key");
            type = getIntent().getStringExtra("type");
            weekSize = getIntent().getStringExtra("weekSize");
        }
        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int lengthAfter) {
                tvWorkNumber.setText(charSequence.length() + "/" + "100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 时间选择器
     */
    private void showTimeSelecter() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                payTime = DateUtils.getFormatTextFromDate(date, "YYYY/MM/dd");
                tvTimePay.setText(payTime);
            }
        }).setType(YEAR_MONTH_DAY)
                .build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }


    @OnClick({R.id.btn_submit, R.id.btn_time_select, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                checkSubmit();
                break;
            case R.id.btn_time_select:
                showTimeSelecter();
                KeyboardUtils.hideSoftInput(this);
                break;
            case R.id.btn_upload:
                PhotoTools.openAlbum(this, 10101);
                break;
        }
    }

    /**
     * 上传凭证
     */
    private void payOffline(String flowCode) {
        APIManager.startRequest(iPayService.payOffline(flowCode, fullName, accountPay, payTime, uploadImg, remarks), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (type.equals(PAY_TYPE_ORDER)) {
                    //发送订单完成广播，通知页面关闭
                    EventBus.getDefault().post(new EventMessage(FINISH_ORDER));
                    // XLOrderDetailsActivity.jumpOrderDetailsActivity(context, key);
                    //拆单需求变更，跳转订单列表
                    OrderListActivit.jumpOrderList(context, ORDER_WAIT_SHIP);
                } else if (type.equals(PAY_TYPE_CHARGE_MONEY)) {
                    EventBus.getDefault().post(new EventMessage(RECHARGE_SUCCESS,2));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    /**
     * 创建流水单号
     */
    private void addPay(String key) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("type", type);
        params.put("channel", CHANNEL_OFFLINE_PAY);
        params.put("device", "ANDROID");

        if (type.equals(PAY_TYPE_WEEK_CARD)) {
            XLCashierActivity.EXT ext = new XLCashierActivity.EXT();
            ext.setNUMBER(weekSize);
            params.put("ext", ext);
        }

        APIManager.startRequest(iPayService.addPay(APIManager.buildJsonBody(params)), new BaseRequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                payOffline(result);
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


    private void checkSubmit() {
        fullName = etFullName.getText().toString();
        accountPay = etAccountPay.getText().toString();
        fullName = etFullName.getText().toString();
        remarks = etRemarks.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            ToastUtil.error("请填写汇款方全称");
            return;
        }

        String accountPayError = PhoneNumberUtil.checkBankNumber(accountPay);
        if (!TextUtils.isEmpty(accountPayError)) {
            ToastUtil.error("请正确填写付款账号");
            return;
        }


        if (TextUtils.isEmpty(payTime)) {
            ToastUtil.error("请选择付款时间");
            return;
        }

        if (TextUtils.isEmpty(uploadImg)) {
            ToastUtil.error("请上传付款截图");
            return;
        }

        addPay(key);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10101) {
            if (data != null) {
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() > 0) {
                    uploadImage(resultPhotos.get(0).path);
                }
            }
        }
    }

    private void uploadImage(final String path) {
        UploadManager.uploadImage(this, path, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                uploadImg = result.url;
                GlideUtils.loadImage(context, ivPhoto, uploadImg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
                uploadImg = "";
                GlideUtils.loadImage(context, ivPhoto, uploadImg);
            }
        });
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
