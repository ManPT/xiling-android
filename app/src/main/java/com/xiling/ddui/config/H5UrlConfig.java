package com.xiling.ddui.config;

import com.xiling.BuildConfig;

public class H5UrlConfig {
    //购买记录
    public static final String WEEK_CARD_RECORD = BuildConfig.BASE_URL + "weekCardRecord";
    //周卡包-黑卡会员
    public static final String WEEKLY_BLACK = BuildConfig.BASE_URL + "wallet";
    //查看物流
    public static final String WEB_URL_EXPRESS = BuildConfig.BASE_URL + "logistics?expressCode=@expressCode&expressId=@expressId";
    //会员规则
    public static final String MEMBER_EXPLAIN = BuildConfig.BASE_URL + "memberExplain";
    //成长值说明
    public static final String GROWTH_INTRO = BuildConfig.BASE_URL + "growthIntro";
    //成长值明细
    public static final String GROWTH_DETAILED = BuildConfig.BASE_URL + "growthList";
    //关于我们
    public static final String ABOUT_US = BuildConfig.BASE_URL + "aboutUs";
    //规则中心
    public static final String RULES_CENTER = BuildConfig.BASE_URL + "rulesPolicyCenter?type=2";
    //政策
    public static final String POLICY = BuildConfig.BASE_URL + "rulesPolicyCenter?type=1";
    //帮助与客服
    public static final String HELP = BuildConfig.BASE_URL + "help";
    //隐私协议
    public static final String PRIVACY_AGREEMENT = BuildConfig.BASE_URL + "privacyAgreement";
    //服务协议
    public static final String SERVICE_AGREEMENT = BuildConfig.BASE_URL + "serverAgreement";
    //大额支付
    public static final String LARGE_PAYMENT  =BuildConfig.BASE_URL +"voucher?orderId=";
    //用户需知
    public static final String CROSS_NEED_NOTE  =BuildConfig.BASE_URL +"crossNeedNote";
    //卡券中心
    public static final String CARD_VOUCHER_CENTER  =BuildConfig.BASE_URL +"cardsAndCouponsList";
    //排行榜列表页
    public static final String WEB_URL_RANK  =BuildConfig.BASE_URL +"rank";
    //排行榜列表页
    public static final String WEB_URL_BRAND  =BuildConfig.BASE_URL +"brand?brandId=";

}
