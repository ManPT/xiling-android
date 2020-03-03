package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 宋秉经
 * 修改手机号  获取当前手机号的验证码页面
 */
public class AccessCaptchaActivity extends BaseActivity implements CaptchaBtn.OnCountDownListener {
    INewUserService mUserService;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.edit_mobile)
    EditText editMobile;
    @BindView(R.id.cb_captcha)
    CaptchaBtn cbCaptcha;
    @BindView(R.id.cb_btn_captcha_voice)
    TextView cbCaptchaVoice;
    @BindView(R.id.tv_btn_next)
    Button btnOk;
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_captcha);
        ButterKnife.bind(this);
        setTitle("我的手机号");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initData();
        initView();
    }

    private void initData() {
        mPhoneNumber = UserManager.getInstance().getUser().getPhone();
    }

    private void initView() {
        tvPhoneNumber.setHint(PhoneNumberUtil.getSecretPhoneNumber(mPhoneNumber));
        cbCaptcha.setOnCountDownListener(this);
        cbCaptcha.setOnCountDownListener(this);
        btnOk.setEnabled(false);

        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editMobile.getText().toString().length() == 4) {
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.cb_captcha, R.id.cb_btn_captcha_voice, R.id.tv_btn_next, R.id.btn_no_use})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.cb_captcha:
                cbCaptcha.start();
                getMemberInfoChangeMsg(mPhoneNumber, "0");
                break;
            case R.id.cb_btn_captcha_voice:
                cbCaptcha.start();
                cbCaptchaVoice.setVisibility(View.GONE);
                getMemberInfoChangeMsg(mPhoneNumber, "1");
                break;
            case R.id.btn_no_use:
                getAuth();
                break;
            case R.id.tv_btn_next:
                checkMember(editMobile.getText().toString());
                break;
        }
    }


    @Override
    public void onCountDownFinish(CaptchaBtn view) {
        cbCaptchaVoice.setVisibility(View.VISIBLE);
        view.setText("重新获取");
    }

    /**
     * 获取实名认证信息
     */
    private void getAuth() {
        APIManager.startRequest(mUserService.getAuth(), new BaseRequestListener<RealAuthBean>() {
            @Override
            public void onSuccess(RealAuthBean result) {
                super.onSuccess(result);

                if (result.getAuthStatus() == 2) {
                    // 已实名认证
                    startActivity(new Intent(context, UpdatePhoneIdentityActivity.class));
                } else {
                    if (result.getAuthStatus() == 1) {
                        //认证中
                        D3ialogTools.showSingleAlertDialog(context, "",
                                "您的实名认证正在认证中\n1个工作日内通过，请耐心等待~~~", "我知道了",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // startActivity(new Intent(mContext, RealAuthActivity.class));
                                    }
                                });
                    } else {
                        D3ialogTools.showAlertDialog(context, "请先实名认证当前商户信息", "去认证", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(context, RealAuthActivity.class));
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
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
     * 获取验证码（更新手机号，设置交易密码，绑定银行卡使用）
     *
     * @param phone
     * @param sendType 0=短信 1=语音
     */
    private void getMemberInfoChangeMsg(String phone, String sendType) {
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mUserService.getMemberMsg(phone, token, sendType), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                ToastUtil.success(message);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.success(e.getMessage());
            }
        });
    }

    /**
     * 校验验证码
     */
    private void checkMember(String code) {
        APIManager.startRequest(mUserService.checkMember(code), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                startActivity(new Intent(context, NewPhoneActivity.class));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case UPDATEE_PHONE:
                finish();
                break;
        }
    }
}
