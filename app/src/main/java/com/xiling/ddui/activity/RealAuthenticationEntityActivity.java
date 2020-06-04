package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.BusinessCategorySelectPopWindow;
import com.xiling.ddui.manager.AddressPicker;
import com.xiling.ddui.tools.DLog;
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
 * 账户认证-实体门店认证
 */
public class RealAuthenticationEntityActivity extends BaseActivity {

    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.et_business_license)
    EditText etBusinessLicense;
    @BindView(R.id.tv_platform_name)
    TextView tvPlatformName;
    @BindView(R.id.et_platform_address)
    EditText etPlatformAddress;
    @BindView(R.id.tv_business_category)
    TextView tvBusinessCategory;
    @BindView(R.id.et_sales_volume)
    EditText etSalesVolume;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.tv_upload_business)
    TextView tvUploadBusiness;


    private int storeType = 2;
    private String storeName = "";
    private String identityCard = "";//营业执照号
    private String storeAddress = "";//门店地址
    private String companyAddress = "";//详细地址
    private String categoryIds = ""; //经营类目（多个以，分隔）
    private String monthSales = ""; //月销售额
    private String idcardFrontImg = "";//门店照片
    private String idcardBackImg = "";//实景照片
    private String businessLicense = "";//营业执照

    BusinessCategorySelectPopWindow businessCategorySelectPopWindow;
    private List<TopCategoryBean> topCategoryBeanList = new ArrayList<>();
    AddressPicker picker = null;

    INewUserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_authentication_entity);
        ButterKnife.bind(this);
        setTitle("实体门店认证");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initAddress();

    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 初始化地址选择器
     */
    private void initAddress() {
        picker = new AddressPicker(context);
    }

    private AddressListBean.DatasBean mAddress = null;

    @OnClick({R.id.btn_platform_select, R.id.btn_business_category, R.id.btn_upload_store_phone, R.id.btn_upload_business_license, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_platform_select:
                //门店地址选择
                hideKeyboard();
                if (mAddress != null) {
                    picker.setDefaultData(mAddress.getProvinceId(), mAddress.getCityId(), mAddress.getDistrictId());
                }

                picker.setListener(new AddressPicker.AddressPickerListener() {
                    @Override
                    public void onAddressSelected(AddressPicker.Province province, AddressPicker.City city, AddressPicker.Area area) {
                        storeAddress = "";
                        if (mAddress == null) {
                            mAddress = new AddressListBean.DatasBean();
                        }
                        if (province != null) {
                            storeAddress += province.getName();
                            mAddress.setProvinceId(province.getCode());
                        }

                        if (city != null) {
                            storeAddress += city.getName();
                            mAddress.setCityId(city.getCode());
                        }

                        if (area != null) {
                            storeAddress += area.getName();
                            mAddress.setDistrictId(area.getCode());
                        }

                        tvPlatformName.setText(storeAddress);
                    }
                });
                picker.showPickerView();
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
            case R.id.btn_upload_store_phone:
                //门店照片上传
                Intent intent = new Intent(context, StorePhotoUploadActivity.class);
                if (!TextUtils.isEmpty(idcardFrontImg) && !TextUtils.isEmpty(idcardBackImg)) {
                    intent.putExtra("idcardFrontImg", idcardFrontImg);
                    intent.putExtra("idcardBackImg", idcardBackImg);
                }
                startActivityForResult(intent, 10001);
                break;
            case R.id.btn_upload_business_license:
                //营业执照上传
                Intent intent1 = new Intent(context, BusinessLicenseUpLoadActivity.class);
                if (!TextUtils.isEmpty(businessLicense)) {
                    intent1.putExtra("businessLicense", businessLicense);
                }
                startActivityForResult(intent1, 10002);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        storeName = etShopName.getText().toString();
        identityCard = etBusinessLicense.getText().toString();
        companyAddress = etPlatformAddress.getText().toString();
        monthSales = etSalesVolume.getText().toString();

        if (TextUtils.isEmpty(storeName)) {
            ToastUtil.error("请输入您的店铺名称");
            return;
        }

        if (TextUtils.isEmpty(storeAddress)) {
            ToastUtil.error("请选择门店地址");
            return;
        }

        if (TextUtils.isEmpty(companyAddress)) {
            ToastUtil.error("请填写详细地址");
            return;
        }

        if (TextUtils.isEmpty(idcardFrontImg) || TextUtils.isEmpty(idcardBackImg)) {
            ToastUtil.error("请上传门店照片");
            return;
        }

        if (TextUtils.isEmpty(businessLicense)) {
            ToastUtil.error("请上传营业执照");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("storeType", storeType + "");
        params.put("storeName", storeName);
        params.put("identityCard", identityCard);
        params.put("storeAddress", storeAddress);
        params.put("companyAddress", companyAddress);
        params.put("categoryIds", categoryIds);
        params.put("monthSales", monthSales);
        params.put("idcardFrontImg", idcardFrontImg);
        params.put("idcardBackImg", idcardBackImg);
        params.put("businessLicense", businessLicense);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            // 上传实景照片返回
            if (data != null) {
                idcardFrontImg = data.getStringExtra("idcardFrontImg");
                idcardBackImg = data.getStringExtra("idcardBackImg");
                if (!TextUtils.isEmpty(idcardFrontImg) && !TextUtils.isEmpty(idcardBackImg)) {
                    tvUploadPhoto.setTextColor(Color.parseColor("#DCB982"));
                    tvUploadPhoto.setText("已上传");
                }
            }
        } else if (requestCode == 10002) {
            //上传营业执照返回
            if (data != null) {
                businessLicense = data.getStringExtra("businessLicense");
                if (!TextUtils.isEmpty(businessLicense)) {
                    tvUploadBusiness.setTextColor(Color.parseColor("#DCB982"));
                    tvUploadBusiness.setText("已上传");
                }
            }
        }

    }
}
