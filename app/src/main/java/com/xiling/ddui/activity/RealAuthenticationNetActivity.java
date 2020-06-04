package com.xiling.ddui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.BusinessCategorySelectPopWindow;
import com.xiling.ddui.custom.popupwindow.PlatformSelectPopWindow;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 账户认证-网络店铺认证
 */
public class RealAuthenticationNetActivity extends BaseActivity {

    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.tv_platform_name)
    TextView tvPlatformName;
    @BindView(R.id.et_platform_address)
    EditText etPlatformAddress;
    @BindView(R.id.tv_business_category)
    TextView tvBusinessCategory;
    @BindView(R.id.et_sales_volume)
    EditText etSalesVolume;
    @BindView(R.id.tv_upload_id)
    TextView tvUploadId;

    PlatformSelectPopWindow platformSelectPopWindow;
    private List<PlatformBean> storeIdList = new ArrayList<>();

    BusinessCategorySelectPopWindow businessCategorySelectPopWindow;
    private List<TopCategoryBean> topCategoryBeanList = new ArrayList<>();

    private int storeType = 1;
    private String storeName = "";
    private String storeIds = "";
    private String companyAddress = "";
    private String categoryIds = "";
    private String monthSales = "";
    private String idcardFrontImg = "";
    private String idcardBackImg = "";

    INewUserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_authentication_net);
        ButterKnife.bind(this);
        setTitle("网络店铺认证");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
    }

    @OnClick({R.id.btn_platform_select, R.id.btn_business_category, R.id.btn_upload_identification, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_platform_select:
                //销售平台
                platformSelectPopWindow = new PlatformSelectPopWindow(context, storeIdList);
                platformSelectPopWindow.setPlatformSelectListener(new PlatformSelectPopWindow.PlatformSelectListener() {
                    @Override
                    public void onPlatformSelect(List<PlatformBean> platformBeanList) {
                        storeIdList = platformBeanList;
                        String platName = "";
                        storeIds = "";
                        for (int i = 0; i < platformBeanList.size(); i++) {
                            if (i == platformBeanList.size() - 1) {
                                platName += platformBeanList.get(i).getStoreName();
                                storeIds += platformBeanList.get(i).getStoreId();
                            } else {
                                platName += platformBeanList.get(i).getStoreName() + ",";
                                storeIds += platformBeanList.get(i).getStoreId() + ",";
                            }
                        }
                        tvPlatformName.setText(platName);
                    }
                });

                platformSelectPopWindow.show();
                break;
            case R.id.btn_business_category:
                //经营类目
                businessCategorySelectPopWindow = new BusinessCategorySelectPopWindow(context, topCategoryBeanList);
                businessCategorySelectPopWindow.setSelectListener(new BusinessCategorySelectPopWindow.SelectListener() {
                    @Override
                    public void onPlatformSelect(List<TopCategoryBean> topCategoryList) {
                        topCategoryBeanList = topCategoryList;
                        String categoryName = "";
                        categoryIds = "";
                        for (int i = 0; i < topCategoryBeanList.size(); i++) {
                            if (i == topCategoryBeanList.size() - 1) {
                                categoryName += topCategoryBeanList.get(i).getCategoryName();
                                categoryIds += topCategoryBeanList.get(i).getCategoryId();
                            } else {
                                categoryName += topCategoryBeanList.get(i).getCategoryName() + ",";
                                categoryIds += topCategoryBeanList.get(i).getCategoryId() + ",";
                            }
                        }
                        tvBusinessCategory.setText(categoryName);
                    }
                });
                businessCategorySelectPopWindow.show();
                break;
            case R.id.btn_upload_identification:
                //上传身份证
                Intent intent = new Intent(context, IdentificationUploadActivity.class);
                if (!TextUtils.isEmpty(idcardFrontImg) && !TextUtils.isEmpty(idcardBackImg)) {
                    intent.putExtra("idcardFrontImg", idcardFrontImg);
                    intent.putExtra("idcardBackImg", idcardBackImg);
                }
                startActivityForResult(intent, 10001);
                break;
            case R.id.btn_submit:
                //提交
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            // 上传身份证返回
            if (data != null) {
                idcardFrontImg = data.getStringExtra("idcardFrontImg");
                idcardBackImg = data.getStringExtra("idcardBackImg");
                if (!TextUtils.isEmpty(idcardFrontImg) && !TextUtils.isEmpty(idcardBackImg)) {
                    tvUploadId.setTextColor(Color.parseColor("#DCB982"));
                    tvUploadId.setText("已上传");
                }
            }
        }

    }

    private void submit() {
        storeName = etShopName.getText().toString();
        companyAddress = etPlatformAddress.getText().toString();
        monthSales = etSalesVolume.getText().toString();

        if (TextUtils.isEmpty(storeName)) {
            ToastUtil.error("请输入您的店铺名称");
            return;
        }

        if (TextUtils.isEmpty(storeIds)) {
            ToastUtil.error("请选择所在平台");
            return;
        }

        if (TextUtils.isEmpty(idcardFrontImg) || TextUtils.isEmpty(idcardBackImg)) {
            ToastUtil.error("请上传身份证");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("storeType", storeType + "");
        params.put("storeName", storeName);
        params.put("storeIds", storeIds);
        params.put("companyAddress", companyAddress);
        params.put("categoryIds", categoryIds);
        params.put("monthSales", monthSales);
        params.put("idcardFrontImg", idcardFrontImg);
        params.put("idcardBackImg", idcardBackImg);

        APIManager.startRequest(mUserService.editAuth(params), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                ToastUtil.error(message);
                EventBus.getDefault().post(new EventMessage(Event.REAL_AUTH_SUCCESS));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());

            }
        });

    }

    @Override
    public void onBackPressed() {

        D3ialogTools.showAlertDialog(context, "还未提交认证，确认退出吗?", "退出", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
