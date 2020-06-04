package com.xiling.dduis.magnager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.xiling.R;
import com.xiling.ddui.activity.RealAuthActivity;
import com.xiling.ddui.activity.UpdatePhoneIdentityActivity;
import com.xiling.ddui.bean.IndexBrandBean;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录对象管理类
 */
public class UserManager {
    private static UserManager userManager;
    private static SPUtils spUtils;
    public static final String USER_TABLE = "user_table_name";
    public static final String USER_MYUSER = "user";
    private static Gson gson;
    ICaptchaService iCaptchaService;
    INewUserService iNewUserService;

    private UserManager() {
        spUtils = new SPUtils(USER_TABLE);
        gson = new Gson();
        iCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
    }

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }

    public void setOAuthToken(String token) {
        spUtils.putString(Key.OAUTH, token);
    }

    public String getOAuthToken() {
        return spUtils.getString(Key.OAUTH, "");
    }

    public void setUser(NewUserBean user) {
        spUtils.putString(USER_MYUSER, gson.toJson(user));
    }

    public void loginSuccess(Context context, NewUserBean user) {
        spUtils.putString(USER_MYUSER, gson.toJson(user));
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_SUCCESS));
        PushManager.setJPushInfo(context, user);
    }

    /**
     * 退出登录
     */
    public void loginOut(Context context) {
        spUtils.remove(Key.OAUTH);
        spUtils.remove(USER_MYUSER);
        PushManager.setJPushInfo(context, null);
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_OUT));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public NewUserBean getUser() {
        String userJson = spUtils.getString(USER_MYUSER);

        if (!TextUtils.isEmpty(userJson)) {
            return gson.fromJson(userJson, NewUserBean.class);
        }
        return null;
    }


    public boolean isLogin() {
        return getUser() != null;
    }

    public boolean isLogin(Context context) {
        if (getUser() == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return getUser() != null;
    }


    /**
     * 获取真实身份
     *
     * @return
     */
    public int getUserLevel() {
        int level = 0;
        NewUserBean userBean = getUser();
        if (userBean != null) {
            level = userBean.getRoleId();
        }
        return level;
    }

    /**
     * 获取临时身份
     *
     * @return
     */
    public int getTemporaryUserLevel() {
        int level = 0;
        NewUserBean userBean = getUser();
        if (userBean != null) {
            int roleId = userBean.getRoleId();
            String sWeekRoleId = userBean.getWeekRoleId();
            int weekRoleId = 0;
            if (!TextUtils.isEmpty(sWeekRoleId)) {
                weekRoleId = Integer.valueOf(sWeekRoleId);
            }
            level = roleId > weekRoleId ? roleId : weekRoleId;
        }

        return level;
    }


    /**
     * 根据用户等级，获取税费
     *
     * @return
     */
    public double getTaxationForUser(ProductNewBean item) {

        if (item.getSkus().size() == 0) {
            return 0;
        }
        double mPrice = item.getSkus().get(0).getRetailTax();
        switch (getTemporaryUserLevel()) {
            case 1:
                mPrice = item.getSkus().get(0).getLevel10Tax();
                break;
            case 2:
                mPrice = item.getSkus().get(0).getLevel20Tax();
                break;
            case 3:
                mPrice = item.getSkus().get(0).getLevel30Tax();
                break;
        }

        return mPrice;
    }

    /**
     * 根据用户等级，获取税费
     *
     * @return
     */
    public double getTaxationForUser(ProductNewBean.SkusBean item) {

        double mPrice = item.getRetailTax();
        switch (getTemporaryUserLevel()) {
            case 1:
                mPrice = item.getLevel10Tax();
                break;
            case 2:
                mPrice = item.getLevel20Tax();
                break;
            case 3:
                mPrice = item.getLevel30Tax();
                break;
        }

        return mPrice;
    }


    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(ProductNewBean item) {
        double mPrice = item.getMinPrice();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (getTemporaryUserLevel()) {
                case 1:
                    mPrice = item.getLevel10Price();
                    break;
                case 2:
                    mPrice = item.getLevel20Price();
                    break;
                case 3:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }


    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(IndexBrandBean.IndexBrandBeanListBean item) {
        double mPrice = item.getMinPrice();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (getTemporaryUserLevel()) {
                case 1:
                    mPrice = item.getLevel10Price();
                    break;
                case 2:
                    mPrice = item.getLevel20Price();
                    break;
                case 3:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }


    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(HomeRecommendDataBean.DatasBean item) {
        double mPrice = item.getMinPrice();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (getTemporaryUserLevel()) {
                case 1:
                    mPrice = item.getLevel10Price();
                    break;
                case 2:
                    mPrice = item.getLevel20Price();
                    break;
                case 3:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }

    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(ProductNewBean.SkusBean item) {
        double mPrice = item.getRetailPrice();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (getTemporaryUserLevel()) {
                case 1:
                    mPrice = item.getLevel10Price();
                    break;
                case 2:
                    mPrice = item.getLevel20Price();
                    break;
                case 3:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }

    /**
     * 根据身份获取优惠价图标
     *
     * @param allowPurchase 允许购买，已下架或已售罄此值为false
     * @return
     */
    public int getDiscountIconForUser(boolean allowPurchase) {
        int drawableRes = 0;

        NewUserBean userBean = getUser();
        if (userBean != null) {
            int roleId = userBean.getRoleId();
            String sWeekRoleId = userBean.getWeekRoleId();
            int weekRoleId = 0;
            if (!TextUtils.isEmpty(sWeekRoleId)) {
                weekRoleId = Integer.valueOf(sWeekRoleId);
            }

            if (weekRoleId > roleId) {
                // 临时身份
                if (weekRoleId == 2) {
                    drawableRes = allowPurchase ? R.drawable.icon_discount_svip_ex : R.drawable.icon_discount_svip_ex_un;
                } else if (weekRoleId == 3) {
                    drawableRes = allowPurchase ? R.drawable.icon_discount_black_ex : R.drawable.icon_discount_black_ex_un;
                }
            } else {
                //真实身份
                switch (roleId) {
                    case 0:
                        drawableRes = allowPurchase ? R.drawable.icon_discount : R.drawable.icon_discount_un;
                        break;
                    case 1:
                        drawableRes = allowPurchase ? R.drawable.icon_discount_vip : R.drawable.icon_discount_vip_un;
                        break;
                    case 2:
                        drawableRes = allowPurchase ? R.drawable.icon_discount_svip : R.drawable.icon_discount_svip_un;
                        break;
                    case 3:
                        drawableRes = allowPurchase ? R.drawable.icon_discount_black : R.drawable.icon_discount_black_un;
                        break;
                }
            }
        } else {
            drawableRes = allowPurchase ? R.drawable.icon_discount : R.drawable.icon_discount_un;
        }

        return drawableRes;
    }

    public int getCommodityLevel() {
        int level = 0;
        NewUserBean userBean = getUser();
        if (userBean != null) {
            int roleId = userBean.getRoleId();
            String sWeekRoleId = userBean.getWeekRoleId();
            int weekRoleId = 0;
            if (!TextUtils.isEmpty(sWeekRoleId)) {
                weekRoleId = Integer.valueOf(sWeekRoleId);
            }
            if (weekRoleId > roleId) {
                switch (weekRoleId) {
                    case 2:
                        level = 4;
                        break;
                    case 3:
                        level = 5;
                        break;
                }

            } else {
                level = roleId;
            }
        }

        return level;
    }

    /**
     * 设置昵称
     */
    public void setNickName(String nickName) {
        NewUserBean userBean = getUser();
        userBean.setNickName(nickName);
        setUser(userBean);
    }


    //校验用户信息，保证用户信息的正确性
    public void checkUserInfo(final OnCheckUserInfoLisense onCheckUserInfoLisense) {
        if (UserManager.getInstance().isLogin()) {
            APIManager.startRequest(iCaptchaService.getUserInfo(), new BaseRequestListener<NewUserBean>() {
                @Override
                public void onSuccess(NewUserBean result) {
                    super.onSuccess(result);
                    UserManager.getInstance().setUser(result);
                    if (onCheckUserInfoLisense != null) {
                        onCheckUserInfoLisense.onCheckUserInfoSucess(result);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ToastUtil.error(e.getMessage());
                    if (onCheckUserInfoLisense != null) {
                        onCheckUserInfoLisense.onCheckUserInfoFail();
                    }
                }
            });
        } else {
            onCheckUserInfoLisense.onCheckUserInfoFail();
        }
    }

    public interface OnCheckUserInfoLisense {
        void onCheckUserInfoSucess(NewUserBean newUserBean);

        void onCheckUserInfoFail();
    }


    /**
     * 判断账户认证
     * 认证状态（0，未认证，1，认证申请，2，认证通过，4，认证拒绝）
     */
    public void isRealAuth(final Context mContext, final RealAuthListener realAuthListener) {
        APIManager.startRequest(iNewUserService.getAuth(), new BaseRequestListener<RealAuthBean>() {
            @Override
            public void onSuccess(RealAuthBean result) {
                super.onSuccess(result);
                if (result.getAuthStatus() == 2) {
                    // 已账户认证
                    if (realAuthListener != null) {
                        realAuthListener.onRealAuth();
                    }
                } else {
                    showNoAuthDialog(mContext, result);
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
     * 未账户认证
     */
    public void showNoAuthDialog(final Context mContext, RealAuthBean result) {
        if (result.getAuthStatus() == 1) {
            D3ialogTools.showSingleAlertDialog(mContext, "",
                    "您的账户正在认证中，1个工作日内审核，请耐心等待～～", "我知道了",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // startActivity(new Intent(mContext, RealAuthActivity.class));
                        }
                    });
        } else {
            D3ialogTools.showAlertDialog(mContext, "请先完善认证当前用户实名信息", "去认证", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, RealAuthActivity.class));
                }
            }, "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    public interface RealAuthListener {
        void onRealAuth();
    }


}
