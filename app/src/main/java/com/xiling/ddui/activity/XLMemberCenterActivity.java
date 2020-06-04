package com.xiling.ddui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.WeekPackageAdapter;
import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.ddui.custom.popupwindow.BuyWeekCardPopWindow;
import com.xiling.ddui.custom.popupwindow.DirectRechargeDialog;
import com.xiling.ddui.custom.popupwindow.WeekBeOverdueDialog;
import com.xiling.ddui.service.IMemberService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.MainActivity;
import com.xiling.module.community.DateUtils;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.viewHome;

/**
 * @auth 逄涛
 * 会员中心
 */
public class XLMemberCenterActivity extends BaseActivity {
    IMemberService iMemberService;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_my_week_card)
    TextView tvMyWeekCard;
    @BindView(R.id.iv_roleLevel)
    ImageView ivRoleLevel;
    @BindView(R.id.tv_roleLevel)
    TextView tvRoleLevel;
    @BindView(R.id.rel_backgroud_head)
    RelativeLayout relBackgroudHead;
    @BindView(R.id.tv_coupon_size)
    TextView tvCouponSize;
    @BindView(R.id.tv_couponDate)
    TextView tvCouponDate;
    @BindView(R.id.growth_value_current)
    TextView growthValueCurrent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_growth_message)
    TextView tvGrowthMessage;
    @BindView(R.id.tv_week_card_name)
    TextView tvWeekCardName;
    @BindView(R.id.tv_week_card_expired_time)
    TextView tvWeekCardExpiredTime;
    @BindView(R.id.rel_week_card)
    RelativeLayout relWeekCard;
    @BindView(R.id.rel_title)
    RelativeLayout relTitle;
    @BindView(R.id.btn_growth_detailed)
    View btnGrowthDetailed;
    @BindView(R.id.iv_cart_us)
    ImageView ivCartUs;
    @BindView(R.id.tv_cart_us)
    TextView tvCartUs;
    @BindView(R.id.pager_weekpackage)
    ViewPager pagerWeekPackage;
    @BindView(R.id.iv_upgrading)
    ImageView ivUpgrading;


    WeekPackageAdapter weekPackageAdapter;
    List<View> weekPackageList = new ArrayList<>();


    WeekCardInfo weekCardInfo;
    boolean isOpenWeekCard = false;
    @BindView(R.id.btn_buy)
    TextView btnBuy;
    @BindView(R.id.btn_sale)
    TextView btnSale;
    @BindView(R.id.tv_experience)
    TextView tvExperience;

    List<WeekCardConfigBean> weekCardConfigBeanList = new ArrayList<>();
    List<WeekCardConfigBean> myWeekCardList = new ArrayList<>();
    List<WeekCardConfigBean> myBuyWeekCardList = new ArrayList<>();
    WeekCardConfigBean weekCardConfigBean = null;
    @BindView(R.id.btn_order_invitation)
    RelativeLayout tvMyWeekCardPackage;
    @BindView(R.id.rel_bottom)
    View relBottom;

    private NewUserBean mUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlmember_center);
        setLeftBlack();
        ButterKnife.bind(this);
        makeStatusBarTranslucent();
        iMemberService = ServiceManager.getInstance().createService(IMemberService.class);
        initView();
        getMemberCenterInfo();
    }


    private void initView() {
        pagerWeekPackage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                weekCardConfigBean = myWeekCardList.get(position);
                if (weekCardConfigBean == null) {
                    ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_vip);
                    btnBuy.setVisibility(View.VISIBLE);
                    btnSale.setVisibility(View.GONE);
                } else if (weekCardConfigBean.getWeekType() == 2) {
                    ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_svip);
                    btnBuy.setVisibility(View.VISIBLE);
                    btnSale.setVisibility(View.VISIBLE);
                } else if (weekCardConfigBean.getWeekType() == 3) {
                    ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_balck);
                    btnBuy.setVisibility(View.VISIBLE);
                    btnSale.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * 获取周卡配置
     */
    private void getWeekCardConfigList() {
        APIManager.startRequest(iMemberService.getWeekCardConfigList(), new BaseRequestListener<List<WeekCardConfigBean>>() {
            @Override
            public void onSuccess(List<WeekCardConfigBean> result) {
                super.onSuccess(result);
                if (result != null && result.size() > 0) {
                    pagerWeekPackage.setVisibility(View.VISIBLE);
                    weekPackageList.clear();
                    myWeekCardList.clear();
                    myBuyWeekCardList.clear();
                    NewUserBean userBean = UserManager.getInstance().getUser();
                    weekCardConfigBeanList = result;

                    if (userBean.getRoleId() == 0) {
                        //如果是普通用户，添加升级vip权益
                        View view = LayoutInflater.from(context).inflate(R.layout.item_week_package, null);
                        ImageView imageView = view.findViewById(R.id.iv_img);
                        imageView.setBackgroundResource(R.drawable.bg_upgrading_vip);
                        myWeekCardList.add(null);
                        weekPackageList.add(view);
                    }

                    for (WeekCardConfigBean weekCardConfigBean : result) {
                        if (userBean.getRoleId() == 0 || userBean.getRoleId() == 1) {
                            // 如果本身是普通会员，都可以购买
                            View view = LayoutInflater.from(context).inflate(R.layout.item_week_package, null);
                            ImageView imageView = view.findViewById(R.id.iv_img);
                            if (weekCardConfigBean.getWeekType() == 2) {
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_svip);
                            } else if (weekCardConfigBean.getWeekType() == 3) {
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_balck);
                            }
                            myWeekCardList.add(weekCardConfigBean);
                            myBuyWeekCardList.add(weekCardConfigBean);
                            weekPackageList.add(view);
                        } else if (userBean.getRoleId() == 2) {
                            //如果是svip会员，只能购买黑卡
                            if (weekCardConfigBean.getWeekType() == 3) {
                                View view = LayoutInflater.from(context).inflate(R.layout.item_week_package, null);
                                ImageView imageView = view.findViewById(R.id.iv_img);
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_balck);
                                myWeekCardList.add(weekCardConfigBean);
                                myBuyWeekCardList.add(weekCardConfigBean);
                                weekPackageList.add(view);
                            }
                        }
                    }

                    if (myWeekCardList.size() > 0) {
                        weekCardConfigBean = myWeekCardList.get(0);
                        if (weekCardConfigBean == null) {
                            ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_vip);
                            btnBuy.setVisibility(View.VISIBLE);
                            btnSale.setVisibility(View.GONE);
                        } else if (myWeekCardList.get(0).getWeekType() == 2) {
                            ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_svip);
                            btnBuy.setVisibility(View.VISIBLE);
                            btnSale.setVisibility(View.VISIBLE);
                        } else if (myWeekCardList.get(0).getWeekType() == 3) {
                            ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_balck);
                            btnBuy.setVisibility(View.VISIBLE);
                            btnSale.setVisibility(View.VISIBLE);
                        }
                    }


                    if (weekPackageAdapter == null) {
                        weekPackageAdapter = new WeekPackageAdapter(context, weekPackageList);
                        pagerWeekPackage.setAdapter(weekPackageAdapter);
                    } else {
                        weekPackageAdapter.notifyDataSetChanged();
                    }

                } else {
                    pagerWeekPackage.setVisibility(View.GONE);
                    ivUpgrading.setVisibility(View.GONE);
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
     * 会员中心首页接口
     */
    private void getCenterInfo(final NewUserBean newUserBean) {
        mUserBean = newUserBean;
        APIManager.startRequest(iMemberService.getCenterInfo(), new BaseRequestListener<MemberCenterInfo>() {
            @Override
            public void onSuccess(MemberCenterInfo result) {
                super.onSuccess(result);
                GlideUtils.loadHead(context, ivHead, result.getHeadImage());
                tvMemberName.setText(result.getNickName());
                tvCouponSize.setText(result.getWeekCardCount() + "");
                if (!TextUtils.isEmpty(result.getLastWeekCardExpiredTime())) {
                    tvCouponDate.setText(DateUtils.timeStamp2Date(Long.valueOf(DateUtils.date2TimeStamp(result.getLastWeekCardExpiredTime(), "")), "yyyy.MM.dd") + "到期");
                } else {
                    tvCouponDate.setText("查看更多");
                }

                int intGrowValueTotle = (int) result.getGrowValueTotle();
                if (intGrowValueTotle == result.getGrowValueTotle()) {
                    growthValueCurrent.setText(intGrowValueTotle + "");
                } else {
                    growthValueCurrent.setText(result.getGrowValueTotle() + "");
                }

                if (newUserBean.getRoleId() == 3) {
                    progressBar.setProgress(100);
                    progressBar.setMax(100);
                    tvGrowthMessage.setText("您当前已是黑卡会员");

                    ToastUtil.hideLoading();
                    pagerWeekPackage.setVisibility(View.GONE);
                    relBottom.setVisibility(View.GONE);
                    ivUpgrading.setVisibility(View.GONE);
                    setWeekCardStatus(null);

                } else {
                    double max = Double.valueOf(result.getNextMemberRole().getGrowValue());
                    progressBar.setMax((int) max);
                    double progress = Double.valueOf(result.getGrowValueTotle());
                    progressBar.setProgress((int) progress);
                    tvGrowthMessage.setText("差" + NumberHandler.reservedDecimalFor2(Double.valueOf(result.getNextMemberRole().getGrowValue()) - Double.valueOf(result.getGrowValueTotle()))
                            + "成长值升级成为终身" + result.getNextMemberRole().getRoleName() + "(享受" + Double.valueOf(result.getNextMemberRole().getRoleDiscount()) / 10 + "折优惠)");

                    pagerWeekPackage.setVisibility(View.VISIBLE);
                    ivUpgrading.setVisibility(View.VISIBLE);
                    relBottom.setVisibility(View.VISIBLE);
                    getWeekCardConfigList();
                    getWeekCardInfo();

                }
                switch (UserManager.getInstance().getCommodityLevel()) {
                    case 0:
                        // 普通会员
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_ordinary);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_ordinary);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_ordinary);
                        tvMyWeekCardPackage.setVisibility(View.GONE);
                        break;
                    case 1:
                        //VIP
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_vip);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_vip);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_vip);
                        tvMyWeekCardPackage.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        //SVIP
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_svip);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_svip);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_svip);
                        tvMyWeekCardPackage.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        //BLACK
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_black);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_black);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_black);
                        tvMyWeekCardPackage.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_svip);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_svip_ex);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_svip);
                        tvMyWeekCardPackage.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_black);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_black_ex);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_black);
                        tvMyWeekCardPackage.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
                ToastUtil.hideLoading();
            }
        });
    }

    /**
     * 启动倒计时
     */
    CountDownTimer countDownTimer;

    private void startCountDown(long waitPayTimeMilli) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(waitPayTimeMilli, 1000) {
            @Override
            public void onTick(long l) {
                tvWeekCardExpiredTime.setText(DateUtils.getDayForTime(l));
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }

    /**
     * 设置周卡
     *
     * @param weekCardInfo
     */
    private void setWeekCardStatus(WeekCardInfo weekCardInfo) {
        if (weekCardInfo == null) {
            // 没有周卡
            tvMyWeekCard.setText("当前暂无周卡体验");
            relWeekCard.setVisibility(View.GONE);
        } else {
            //有周卡
            if (weekCardInfo.getStatus().equals("1")) {
                // 有效
                switch (weekCardInfo.getWeekType()) {
                    case "2":
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_vip);
                        ivCartUs.setBackgroundResource(R.drawable.icon_us_vip);
                        tvCartUs.setTextColor(Color.parseColor("#6D8891"));
                        tvExperience.setTextColor(Color.parseColor("#6D8891"));
                        break;
                    case "3":
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_black);
                        ivCartUs.setBackgroundResource(R.drawable.icon_us_black);
                        tvCartUs.setTextColor(Color.parseColor("#A27309"));
                        tvExperience.setTextColor(Color.parseColor("#B68B2A"));
                        break;
                }

                tvMyWeekCard.setText(weekCardInfo.getWeekName());
                relWeekCard.setVisibility(View.VISIBLE);
                tvWeekCardName.setText(weekCardInfo.getWeekName());
                long waitPayTimeMilli = DateUtils.date2TimeStampLong(weekCardInfo.getExpiredTime(), "") - System.currentTimeMillis();
                startCountDown(waitPayTimeMilli);
            } else {
                //过期
                tvMyWeekCard.setText("当前周卡已到期");
                relWeekCard.setVisibility(View.GONE);
                //如果没有弹出过，弹出过期提醒
                if (SharedPreferenceUtil.getInstance().getBoolean(UserManager.getInstance().getUser().getMemberId() + weekCardInfo.getWeekOrderNo())) {
                    SharedPreferenceUtil.getInstance().putBoolean(UserManager.getInstance().getUser().getMemberId() + weekCardInfo.getWeekOrderNo(), false);
                    WeekBeOverdueDialog weekBeOverdueDialog = new WeekBeOverdueDialog(context);
                    weekBeOverdueDialog.show();
                }

            }
        }
    }


    /**
     * 获取周卡状态
     */
    private void getWeekCardInfo() {
        APIManager.startRequest(iMemberService.getWeekCardInfo(), new BaseRequestListener<WeekCardInfo>() {
            @Override
            public void onSuccess(WeekCardInfo result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
                weekCardInfo = result;
                if (result != null && result.getWeekType() != null) {
                    NewUserBean newUserBean = UserManager.getInstance().getUser();
                    //有效
                    if (TextUtils.isEmpty(result.getWeekType())) {
                        setWeekCardStatus(null);
                    } else {
                        if (Integer.valueOf(result.getWeekType()) > newUserBean.getRoleId()) {
                            setWeekCardStatus(result);
                        }
                    }

                } else {
                    setWeekCardStatus(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
            }
        });
    }


    /**
     * 获取会员中心信息
     */
    private void getMemberCenterInfo() {
        ToastUtil.showLoading(context);
        UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
            @Override
            public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                getCenterInfo(newUserBean);
            }

            @Override
            public void onCheckUserInfoFail() {
                ToastUtil.hideLoading();
            }
        });
    }


    @OnClick({R.id.btn_close, R.id.btn_notes, R.id.btn_order_goods, R.id.btn_order_invitation, R.id.btn_growth, R.id.ll_coupon,
            R.id.ll_growth_detailed, R.id.btn_buy, R.id.btn_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_notes:
                //会员说明
                WebViewActivity.jumpUrl(context, "会员说明", H5UrlConfig.MEMBER_EXPLAIN);
                break;
            case R.id.btn_order_invitation:
                //去邀请
                startActivity(new Intent(context, InviteFriendsActivity.class));
                break;
            case R.id.btn_growth:
                WebViewActivity.jumpUrl(context, "成长值说明", H5UrlConfig.GROWTH_INTRO);
                break;
            case R.id.ll_growth_detailed:
                WebViewActivity.jumpUrl(context, "成长值明细", H5UrlConfig.GROWTH_DETAILED);
                break;
            case R.id.ll_coupon:
                //优惠券
                WebViewActivity.jumpUrl(context, "卡券中心", H5UrlConfig.CARD_VOUCHER_CENTER);
                break;
            case R.id.btn_order_goods:
                //去订货
                EventBus.getDefault().post(new EventMessage(viewHome));
                finish();
                break;
            case R.id.btn_buy:
                //购买周卡
                if (mUserBean != null) {
                    new BuyWeekCardPopWindow(context, myBuyWeekCardList).show();
                }
                break;
            case R.id.btn_sale:
                //预存货款升级
                if (weekCardConfigBean.getWeekType() == 2) {
                    new DirectRechargeDialog(context, DirectRechargeDialog.TYPE_VIP).show();
                } else if (weekCardConfigBean.getWeekType() == 3) {
                    new DirectRechargeDialog(context, DirectRechargeDialog.TYPE_BLACK).show();
                }
                break;
        }
    }

    /**
     * 去购买周卡，fragment调用
     */
    public void purchaseWeekCard(WeekCardConfigBean weekCardConfigBean) {
        String myWeekCard = tvMyWeekCard.getText().toString();
        BuyWeekCardActivity.jumpBuyWeekCardActivity(this, weekCardConfigBean, myWeekCard);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventMessage message) {
        switch (message.getEvent()) {
            case WEEK_CARD_OPEN:
                //开通了周卡,刷新
                isOpenWeekCard = true;
                getMemberCenterInfo();
                break;
            case WEEK_CARD_PAY:
                // 购买了周卡，刷新可用体验卡数量
                APIManager.startRequest(iMemberService.getCenterInfo(), new BaseRequestListener<MemberCenterInfo>() {
                    @Override
                    public void onSuccess(MemberCenterInfo result) {
                        super.onSuccess(result);
                        tvCouponSize.setText(result.getWeekCardCount() + "");
                        if (!TextUtils.isEmpty(result.getLastWeekCardExpiredTime())) {
                            tvCouponDate.setText(DateUtils.timeStamp2Date(Long.valueOf(DateUtils.date2TimeStamp(result.getLastWeekCardExpiredTime(), "")), "yyyy.MM.dd") + "到期");
                        } else {
                            tvCouponDate.setText("查看更多");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.error(e.getMessage());
                    }
                });
                break;

        }
    }
}
