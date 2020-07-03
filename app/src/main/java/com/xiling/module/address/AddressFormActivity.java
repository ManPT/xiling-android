package com.xiling.module.address;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.utils.StringUtils;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.activity.SelectAddressMapActivity;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.DDDeleteDialog;
import com.xiling.ddui.manager.AddressMapManager;
import com.xiling.ddui.manager.AddressPicker;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.SmartAddressUtil;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.IRegionService;
import com.xiling.shared.util.ShareUtilsNew;
import com.xiling.shared.util.ToastUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * @author 逄涛
 * 添加收货地址
 */
public class AddressFormActivity extends BaseActivity {

    private boolean isEditAddress = false;
    private IAddressService mAddressService;
    private AddressListBean.DatasBean mAddress = new AddressListBean.DatasBean();

    private AMapLocationClient mLocationClient;
    public AMapLocation mAMapLocation;

    @BindView(R.id.contactsEt)
    protected EditText mContactsEt;
    @BindView(R.id.phoneEt)
    protected EditText mPhoneEt;
    @BindView(R.id.regionTv)
    protected TextView mRegionTv;
    @BindView(R.id.detailEt)
    protected EditText mDetailEt;
    @BindView(R.id.defaultSwitch)
    protected ImageView mDefaultSwitch;
    private IRegionService mRegionService;

    AddressPicker picker = null;
    String addressId = "";
    boolean isFirst = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_form_layout);
        ButterKnife.bind(this);
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        mRegionService = ServiceManager.getInstance().createService(IRegionService.class);
        mDefaultSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultSwitch.setSelected(!mDefaultSwitch.isSelected());
            }
        });

        Intent intent = getIntent();

        if (intent == null) {
            isEditAddress = false;
        } else if (intent.getExtras() == null) {
            isEditAddress = false;
        } else {
            String action = intent.getExtras().getString("action");
            isEditAddress = action != null && action.equals(Key.EDIT_ADDRESS);
            addressId = intent.getExtras().getString("addressId");
        }
        picker = new AddressPicker(context);

       /* if (TextUtils.isEmpty(addressId)) {
            //如果剪贴板没有内容，定位当前位置
            final String clipMessage = ShareUtilsNew.getClipMessage(context);
            if (TextUtils.isEmpty(clipMessage)) {

            }
        }*/
        setTitle("收货地址");
        initHeaderButtons();
        if (isEditAddress && !TextUtils.isEmpty(addressId)) {
            getAddressInfo(addressId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //因为Android10只有在页面完全显示的情况下，才允许使用剪贴板，所以把智能识别地址的代码发在此处
        if (TextUtils.isEmpty(addressId)) {
            this.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    intelligentAnalysis();
                    isFirst = false;
                }
            });
        }
    }

    private void intelligentAnalysis() {
        //如果剪贴板没有内容，定位当前位置
        final String clipMessage = ShareUtilsNew.getClipMessage(context);
        if (!TextUtils.isEmpty(clipMessage)) {
            // 如果剪贴板有内容，提示是否使用剪贴板内容智能匹配地址
            D3ialogTools.showAlertDialog(context, "是否粘贴一下地址", clipMessage, "立即粘贴", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareUtilsNew.clearClipboard(context);
                    final AddressPicker picker = new AddressPicker(context);
                    picker.setDataLoadListener(new AddressPicker.AddressDataLoadListener() {
                        @Override
                        public void onLoadFinish() {
                            AddressListBean.DatasBean addressBean = SmartAddressUtil.smartAddressParse(clipMessage, picker);
                            if (addressBean != null) {
                                mAddress.setProvinceName(addressBean.getProvinceName());
                                mAddress.setProvinceId(addressBean.getProvinceId());
                                mAddress.setCityName(addressBean.getCityName());
                                mAddress.setCityId(addressBean.getCityId());
                                mAddress.setDistrictName(addressBean.getDistrictName());
                                mAddress.setDistrictId(addressBean.getDistrictId());
                                mAddress.setDetail(addressBean.getDetail());
                                mAddress.setContact(addressBean.getContact());
                                mAddress.setPhone(addressBean.getPhone());

                                setPCAText(mAddress.getFullRegion());
                                if (!TextUtils.isEmpty(mAddress.getDetail())) {
                                    mDetailEt.setText("" + mAddress.getDetail());
                                }

                                if (!TextUtils.isEmpty(mAddress.getContact())) {
                                    mContactsEt.setText(mAddress.getContact());
                                }

                                if (!TextUtils.isEmpty(mAddress.getPhone())) {
                                    mPhoneEt.setText(mAddress.getPhone());
                                }

                            }
                        }
                    });
                    picker.readData();
                }
            }, "我再想想", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }else {
            if (isFirst){
                autoLocationCity();
            }
        }

    }


    /**
     * 定位到当前城市
     */
    private void autoLocationCity() {
        startLocation(this);
    }

    private void setLocationSetting() {
        mLocationClient = new AMapLocationClient(MyApplication.getInstance().getApplicationContext());
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mAMapLocation = aMapLocation;
                if (mAMapLocation.getErrorCode() != 0) {
                    return;
                }
                final String provinceName = mAMapLocation.getProvince();
                final String cityName = mAMapLocation.getCity();
                final String distinctName = mAMapLocation.getDistrict();

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("" + mAMapLocation.getStreet());
                stringBuffer.append("" + mAMapLocation.getStreetNum());
                stringBuffer.append("" + mAMapLocation.getPoiName());
                final String addressValue = stringBuffer.toString();
                convertMapData(provinceName, cityName, distinctName, addressValue);
            }
        });
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setOnceLocation(false);
        option.setNeedAddress(true);
        option.setMockEnable(true);
        option.setLocationCacheEnable(true);
        option.setInterval(1000 * 60 * 10);
        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
    }


    public void startLocation(Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    setLocationSetting();
                } else {
                    ToastUtil.error("缺少定位权限");
                }
            }
        });
    }


    /**
     * 转化地图数据为系统内部编码可识别的数据
     */
    public void convertMapData(final String provinceName, final String cityName, final String distinctName, final String addressValue) {
        final AddressPicker picker = new AddressPicker(context);
        picker.setDataLoadListener(new AddressPicker.AddressDataLoadListener() {
            @Override
            public void onLoadFinish() {
                AddressPicker.Province province = picker.getProvinceByName(provinceName);
                if (province != null) {

                    mAddress.setProvinceId(province.getCode());
                    mAddress.setProvinceName(province.getName());

                    int pIndex = picker.getProvinceIndex(province);
                    if (pIndex > -1) {
                        AddressPicker.City city = picker.getCityByName(pIndex, cityName);
                        if (city != null) {

                            mAddress.setCityId(city.getCode());
                            mAddress.setCityName(city.getName());

                            int cIndex = picker.getCityIndex(pIndex, city);
                            if (cIndex > -1) {
                                AddressPicker.Area area = picker.getAreaByName(pIndex, cIndex, distinctName);
                                if (area != null) {

                                    mAddress.setDistrictId(area.getCode());
                                    mAddress.setDistrictName(area.getName());

                                    DLog.i("=================================");
                                    DLog.d("命中！");
                                    DLog.d("" + province);
                                    DLog.d("" + city);
                                    DLog.d("" + area);
                                    DLog.i("=================================");

                                    setPCAText(mAddress.getFullRegion());
                                    mDetailEt.setText("" + addressValue);
                                }
                            }
                        }
                    }
                }
            }
        });
        picker.readData();
    }


    private void getAddressInfo(final String addressId) {
        APIManager.startRequest(mAddressService.getAddressDetail(addressId), new BaseRequestListener<AddressListBean.DatasBean>(this) {
            @Override
            public void onSuccess(AddressListBean.DatasBean address) {
                mAddress = address;
                mContactsEt.setText(mAddress.getContact());
                mContactsEt.setSelection(mContactsEt.getText().length());
                mPhoneEt.setText(mAddress.getPhone());
                setPCAText(mAddress.getFullRegion());
                mDetailEt.setText(mAddress.getDetail());
                mDefaultSwitch.setSelected(mAddress.getIsDefault() == 1);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    public void setPCAText(String text) {
        mRegionTv.setText("" + text);
        mRegionTv.setTextColor(Color.BLACK);
    }

    private void initHeaderButtons() {
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isEditAddress) {
            getHeaderLayout().setRightText("删除地址");
            getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DDDeleteDialog dialog = new DDDeleteDialog(context);
                    dialog.setTitle("确定要狠心删除此地址吗");
                    dialog.setListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteAddress();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    private void deleteAddress() {
        APIManager.startRequest(mAddressService.deleteAddress(mAddress.getAddressId()), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("删除成功");
                EventBus.getDefault().post(new EventMessage(Event.deleteAddress, mAddress));
                finish();
            }
        });
    }

    @OnClick(R.id.regionLayout)
    protected void showRegionSelector() {
        hideKeyboard();
        picker.setDefaultData(mAddress.getProvinceId(), mAddress.getCityId(), mAddress.getDistrictId());
        picker.setListener(new AddressPicker.AddressPickerListener() {
            @Override
            public void onAddressSelected(AddressPicker.Province province, AddressPicker.City city, AddressPicker.Area area) {
                DLog.d("fullAddress:" + province + " " + city + " " + area);
                DLog.d("ids:" + province.getCode() + "," + city.getCode() + "," + area.getCode());
                if (province != null) {
                    mAddress.setProvinceId(province.getCode());
                    mAddress.setProvinceName(province.getName());
                }

                if (city != null) {
                    mAddress.setCityId(city.getCode());
                    mAddress.setCityName(city.getName());
                }

                if (area != null) {
                    mAddress.setDistrictId(area.getCode());
                    mAddress.setDistrictName(area.getName());
                }

                setPCAText(mAddress.getFullRegion());
            }
        });
        picker.showPickerView();
    }

    @OnClick(R.id.saveBtn)
    protected void onSave() {
        final String contacts = mContactsEt.getText().toString();
        if (contacts.isEmpty()) {
            ToastUtil.error("请填写收货人姓名哦～");
            return;
        }
        final String phone = mPhoneEt.getText().toString();
        if (phone.isEmpty()) {
            ToastUtil.error("请填写手机号码哦～");
            return;
        }

        if (!phone.startsWith("1") || phone.length() != 11) {
            ToastUtil.error("请填写正确的手机号码哦～");
            return;
        }

        if (StringUtils.isEmpty(mAddress.getProvinceId()) || StringUtils.isEmpty(mAddress.getCityId()) || StringUtils.isEmpty(mAddress.getDistrictId())) {
            ToastUtil.error("请选择地区");
            return;
        }
        final String detail = mDetailEt.getText().toString();
        if (detail.isEmpty()) {
            ToastUtil.error("请填写详细地址哦～");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("isDefault", mDefaultSwitch.isSelected() ? 1 : 0);
        params.put("contact", contacts);
        params.put("phone", phone);
        params.put("provinceName", mAddress.getProvinceName());
        params.put("cityName", mAddress.getCityName());
        params.put("districtName", mAddress.getDistrictName());
        params.put("provinceId", mAddress.getProvinceId());
        params.put("cityId", mAddress.getCityId());
        params.put("districtId", mAddress.getDistrictId());
        params.put("detail", detail);
        Observable<RequestResult<Object>> resultObservable;
        if (isEditAddress) {
            params.put("addressId", addressId);
            resultObservable = mAddressService.editAddress(params);
        } else {
            resultObservable = mAddressService.createAddress(params);
        }
        APIManager.startRequest(resultObservable, new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("保存成功");
                if (isEditAddress && mAddress != null) {
                    mAddress.setContact(contacts);
                    mAddress.setPhone(phone);
                    mAddress.setDetail(detail);
                    mAddress.setIsDefault(mDefaultSwitch.isSelected() ? 1 : 0);
                    EventBus.getDefault().post(new EventMessage(Event.saveAddress, mAddress));
                }
                finish();
            }
        });
    }
}
