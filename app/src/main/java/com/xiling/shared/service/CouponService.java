package com.xiling.shared.service;

import android.content.Context;

import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.util.ToastUtil;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service
 * @since 2017-07-05
 */
public class CouponService {

    public static void getCoupon(Context context, String couponId) {
        ICouponService service = ServiceManager.getInstance().createService(ICouponService.class);
        APIManager.startRequest(service.receiveCouponById(couponId), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("优惠券领取成功");
            }
        });
    }

    public static void getCoupon(Context context, String couponId, final BaseCallback<Object> callback) {
        ICouponService service = ServiceManager.getInstance().createService(ICouponService.class);
        APIManager.startRequest(service.receiveCouponById(couponId), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("优惠券领取成功");
                callback.callback(result);
            }
        });
    }

}
