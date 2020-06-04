package com.xiling.shared.service;

import com.xiling.ddui.bean.AccountInfo;
import com.xiling.ddui.bean.AccountManagerBean;
import com.xiling.ddui.bean.BalanceDetailsBean;
import com.xiling.ddui.bean.BalanceTypeBean;
import com.xiling.ddui.bean.InviterInfoBean;
import com.xiling.ddui.bean.MyClientListBean;
import com.xiling.ddui.bean.MyManagerBean;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.bean.RechareConfigBean;
import com.xiling.ddui.bean.UserCostomBean;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.api.RequestResult;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface INewUserService {
    /**
     * 微信注册--登录
     *
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/login-or-registry/we-chat-code?")
    Observable<RequestResult<NewUserBean>> wxLogin(@Query("code") String code, @Query("device") String device);


    /**
     * 微信登录绑定手机号
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/bind-phone")
    Observable<RequestResult<Object>> bindPhone(@Body RequestBody body);

    /**
     * 微信登录，绑定邀请码
     *
     * @param inviteCode 邀请码
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/bind-invite-code")
    Observable<RequestResult<NewUserBean>> bindInviteCode(@Query("inviteCode") String inviteCode,@Query("device") String device );


    /**
     * 发送登录验证码
     *
     * @param sendType 验证码类型
     * @param phone    手机号
     */
    @GET("user/sms-code")
    Observable<RequestResult<Object>> getLoginCode(
            @Query("type") int sendType,
            @Query("phone") String phone);


    /**
     * 手机号登录，校验验证码
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//添加header表明参数是json格式的
    @POST("user/login-or-registry/phone-code")
    Observable<RequestResult<NewUserBean>> checkCaptcha(@Body RequestBody body);

    /**
     * 手机号登录，绑定微信
     *
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/bind-we-chat")
    Observable<RequestResult<Object>> bindWXCode(@Query("code") String code, @Query("device") String device);


    /**
     * 获取用户信息
     */
    @GET("user/getRealUserInfo")
    Observable<RequestResult<NewUserBean>> getUserInfo();

    /**
     * 个人中心我的资产
     */
    @GET("user/getUserIncome")
    Observable<RequestResult<UserInComeBean>> getUserIncome();

    /**
     * 我的客户信息
     */
    @GET("client/my-client")
    Observable<RequestResult<UserCostomBean>> getMyCostom();

    /**
     * 获取账户信息
     */
    @GET("account/getInfo")
    Observable<RequestResult<AccountInfo>> getAccountInfo();


    /**
     * 根据邀请码获取用户基本信息
     */
    @GET("user/invite-code")
    Observable<RequestResult<InviterInfoBean>> getAccountInfoForInvite(@Query("inviteCode") String inviteCode);

    /**
     * 校验是否设置余额密码
     */
    @GET("account/hasPassword")
    Observable<RequestResult<Boolean>> hasPassword();

    /**
     * 校验余额支付密码
     */
    @POST("account/check-balance-password")
    Observable<RequestResult<Object>> checkBalancePassword(@Query("password") String password);


    /**
     * 设置余额支付密码
     */
    @POST("account/setAccountPassword")
    Observable<RequestResult<Object>> setAccountPassword(@Query("password") String password);


    /**
     * 获取验证码（更新手机号，设置交易密码，绑定银行卡使用）
     */
    @GET("captcha/getMemberInfoChangeMsg")
    Observable<RequestResult<Object>> getMemberMsg(@Query("phone") String phone, @Query("token") String token, @Query("sendType") String sendType);


    /**
     * 获取验证码（更新手机号，设置交易密码，绑定银行卡使用）
     */
    @GET("captcha/getUpdatePhoneMsg")
    Observable<RequestResult<Object>> getMemberMsgNewPhone(@Query("phone") String phone, @Query("token") String token, @Query("sendType") String sendType);

    /**
     * 校验验证码（更新手机号，设置交易密码）
     */
    @GET("captcha/checkOldPhone")
    Observable<RequestResult<Object>> checkMember(@Query("checkNumber") String checkNumber);

    /**
     * 校验验证码（更新手机号，设置交易密码）
     */
    @POST("userPhone/newPhoneBinding")
    Observable<RequestResult<Object>> newPhoneBinding(@Query("phone") String phone,@Query("checkNumber") String checkNumber);


    /**
     * 获取账户认证信息
     */
    @GET("auth/get")
    Observable<RequestResult<RealAuthBean>> getAuth();

    /**
     * 获取店铺平台列表
     */
    @GET("store/getAllStore")
    Observable<RequestResult<List<PlatformBean>>> getAllStore();


    /**
     * 提交账户认证信息
     */
    @FormUrlEncoded
    @POST("auth/edit")
    Observable<RequestResult<Object>> editAuth(@FieldMap HashMap<String,String> body);

    /**
     * 修改头像
     * @param imageUrl
     * @return
     */
    @FormUrlEncoded
    @POST("user/changeHead")
    Observable<RequestResult<Object>> updateAvatar(@Field("headImage") String imageUrl);

    /**
     * 修改昵称
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @POST("user/edit")
    Observable<RequestResult<Object>> editNickname(@Field("nickName") String nickname);

    /**
     * 更新手机号-身份证验证
     */
    @GET("userPhone/getAuthentication")
    Observable<RequestResult<Object>> getAuthentication(@Query("idCard")String idCard,@Query("token")String token);

    /**
     * 账户列表
     */
    @GET("account/getList")
    Observable<RequestResult<List<AccountManagerBean>>> getAcountList();


    /**
     * 获取余额分类
     */
    @GET("account/getBalanceType")
    Observable<RequestResult<List<BalanceTypeBean>>> getBalanceType();

    /**
     * 获取余额明细列表
     */
    @GET("account/getBalanceDeteil")
    Observable<RequestResult<BalanceDetailsBean>> getBalanceDeteil(@Query("pageOffset") int pageOffset, @Query("pageSize")int pageSize, @Query("balanceType")String balanceType);


    /**
     * 收益指数列表
     */
    @GET("income/page-list")
    Observable<RequestResult<BalanceDetailsBean>> getIncomeList(@Query("pageOffset") int pageOffset, @Query("pageSize")int pageSize);


    /**
     * 我的客户列表
     * 会员级别 : 1-普通,2-金牌,3-钻石
     */
    @FormUrlEncoded
    @POST("user/customer/getCustomerList")
    Observable<RequestResult<MyClientListBean>> getCustomerList(@FieldMap HashMap<String,String> map);

    /**
     * 我的客户列表
     * 会员级别 : 1-普通,2-金牌,3-钻石
     */
    @FormUrlEncoded
    @POST("user/customer/getCustomerDetail")
    Observable<RequestResult<MyClientListBean.DataBean>> getCustomerDetail(@Field("memberId") String memberId );


    /**
     * 获取我下属会员数量
     */
    @POST("user/customer/getCustomerCount")
    Observable<RequestResult<Integer>> getCustomerCount(@Query("customerType") String customerType);

    /**
     * 我的经理
     */
    @GET("manager/get")
    Observable<RequestResult<MyManagerBean>> getMyManager();


    /**
     * 个人中心-邀请分享
     */
    @GET("share/invite-friends-image")
    Observable<RequestResult<List<String>>> getInviteFriendsImage();

    /**
     * 充值配置参数
     */
    @GET("config/recharge-config")
    Observable<RequestResult<RechareConfigBean>> getRechareConfig();



}
