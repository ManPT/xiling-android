package com.xiling.ddui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.activity.CustomerOrderActivity;
import com.xiling.ddui.activity.IncomeIndexActivity;
import com.xiling.ddui.activity.InviteFriendsActivity;
import com.xiling.ddui.activity.MyClientActivity;
import com.xiling.ddui.activity.MyManagerActivity;
import com.xiling.ddui.activity.OrderListActivit;
import com.xiling.ddui.activity.RealAuthActivity;
import com.xiling.ddui.activity.XLCouponActivity;
import com.xiling.ddui.activity.XLFinanceManangerActivity;
import com.xiling.ddui.activity.XLMemberCenterActivity;
import com.xiling.ddui.activity.XLNewsGroupActivity;
import com.xiling.ddui.activity.XLSettingActivity;
import com.xiling.ddui.adapter.MineServiceAdapter;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.bean.UserCostomBean;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.NestScrollView;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.ddui.tools.XLManager;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.address.AddressListActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.OrderCount;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.ItemWithIcon;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xiling.ddui.fragment.OrderFragment.ORDER_STATUS;
import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * 个人中心
 */
public class XLMineFragment extends BaseFragment implements OnRefreshListener, NestScrollView.OnScrollChangedCallback {

    INewUserService iNewUserService;
    IOrderService mOrderService;

    Unbinder unbinder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.avatarIv)
    ImageView avatarIv;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_user_ordinary)
    ImageView ivUserOrdinary;
    @BindView(R.id.ll_user_vip)
    LinearLayout llUserVip;
    @BindView(R.id.rel_custom)
    RelativeLayout relCustom;
    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    MineServiceAdapter serviceAdapter;
    List<MineServiceAdapter.ServiceBean> serviceBeanList = new ArrayList<>();
    @BindView(R.id.tv_incomeDay)
    TextView tvIncomeDay;
    @BindView(R.id.tv_incomeMonth)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_incomeTotal)
    TextView tvIncomeTotal;
    @BindView(R.id.tv_custom)
    TextView tvCustom;
    @BindView(R.id.tv_custom_order)
    TextView tvCustomOrder;
    @BindView(R.id.orider_wait_pay)
    ItemWithIcon oriderWaitPay;
    @BindView(R.id.orider_wait_ship)
    ItemWithIcon oriderWaitShip;
    @BindView(R.id.orider_wait_received)
    ItemWithIcon oriderWaitReceived;
    @BindView(R.id.orider_closed)
    ItemWithIcon oriderClosed;

    UserInComeBean userInComeBean;
    @BindView(R.id.rel_user_vip)
    RelativeLayout relUserVip;
    @BindView(R.id.rel_asset)
    RelativeLayout relAsset;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.recycler_order)
    LinearLayout recyclerOrder;
    @BindView(R.id.rel_order)
    RelativeLayout relOrder;
    @BindView(R.id.btn_my_client)
    LinearLayout btnMyClient;
    @BindView(R.id.ll_custom)
    LinearLayout llCustom;
    @BindView(R.id.btn_invite_friends)
    ImageView btnInviteFriends;
    @BindView(R.id.btn_my_housekeeper)
    ImageView btnMyHousekeeper;
    @BindView(R.id.btn_financial_management)
    ImageView btnFinancialManagement;
    @BindView(R.id.ll_my_servuce)
    LinearLayout llMyServuce;
    @BindView(R.id.iv_message)
    ImageView ivMessage;


    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.rel_title)
    RelativeLayout relTitle;
    @BindView(R.id.title_message)
    ImageView titleMessage;
    @BindView(R.id.scroll_view)
    NestScrollView scrollView;
    float titleBarHeight = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xl_mine, null, false);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        XLManager.UIConfigure();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        //设置拉动事件监听
        titleBarHeight = getResources().getDimension(R.dimen.dp_187);
        scrollView.setOnScrollChangedCallback(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerService.setLayoutManager(new GridLayoutManager(mContext, 4));
        serviceAdapter = new MineServiceAdapter();
        recyclerService.setAdapter(serviceAdapter);
        return view;
    }


    private void initView() {
        serviceBeanList.clear();
        if (UserManager.getInstance().getUserLevel() > 0) {
            ivUserOrdinary.setVisibility(View.GONE);
            llUserVip.setVisibility(View.VISIBLE);
            relCustom.setVisibility(View.VISIBLE);
            requestVipData();

            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_address, "管理地址", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_coupon, "卡券中心", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_policy, "政策", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_help, "帮助与客服", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_rule, "规则中心", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_paihang, "排行榜", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_activite, "活动报名", false));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_collage, "我的拼团", false));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_author, "授权管理", false));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_complaint, "投诉建议", false));
        } else {
            ivUserOrdinary.setVisibility(View.VISIBLE);
            llUserVip.setVisibility(View.GONE);
            relCustom.setVisibility(View.GONE);
            ivUserOrdinary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, XLMemberCenterActivity.class));
                }
            });

            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_address, "管理地址", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_coupon, "卡券中心", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_finance, "财务管理", true));
            serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_help, "帮助与客服", true));
        }

        serviceAdapter.setNewData(serviceBeanList);

        serviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ViewUtil.setViewClickedDelay(view);
                switch (serviceBeanList.get(position).getTitle()) {
                    case "管理地址":
                        Intent intent = new Intent(mContext, AddressListActivity.class);
                        startActivity(intent);
                        break;
                    case "卡券中心":
                        WebViewActivity.jumpUrl(mContext, "卡券中心", H5UrlConfig.CARD_VOUCHER_CENTER);
                        break;
                    case "财务管理":
                        startActivity(new Intent(mContext, XLFinanceManangerActivity.class));
                        break;
                    case "政策":
                        WebViewActivity.jumpUrl(mContext, "政策", H5UrlConfig.POLICY);
                        break;
                    case "帮助与客服":
                        WebViewActivity.jumpUrl(mContext, "帮助与客服", H5UrlConfig.HELP);
                        break;
                    case "规则中心":
                        WebViewActivity.jumpUrl(mContext, "规则中心", H5UrlConfig.RULES_CENTER);
                        break;
                    case "活动报名":
                        // ToastUtil.success(serviceBeanList.get(position).getTitle());
                        break;
                    case "排行榜":
                        WebViewActivity.jumpUrl(mContext,"排行榜",H5UrlConfig.WEB_URL_RANK);
                        break;
                    case "我的拼团":

                        break;
                    case "授权管理":

                        break;
                    case "投诉建议":

                        break;
                }
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            requestUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void requestUserInfo() {
        if (UserManager.getInstance().isLogin()) {
            getOrderCount();
            UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
                @Override
                public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                    refreshLayout.finishRefresh();
                    if (newUserBean != null) {
                        GlideUtils.loadHead(mContext, avatarIv, newUserBean.getHeadImage());
                        tvName.setText(newUserBean.getNickName());
                        titleName.setText(newUserBean.getNickName());

                        if (newUserBean.getAuthStatus() != 2) {
                            tvPhone.setBackgroundResource(R.drawable.icon_mine_unauth);
                        }else{
                            tvPhone.setBackgroundResource(R.drawable.icon_mine_auth);
                        }
                      //  tvPhone.setText(PhoneNumberUtil.getSecretPhoneNumber(newUserBean.getPhone()));
                        switch (newUserBean.getRoleId()) {
                            case 0:
                                relUserVip.setBackgroundResource(R.drawable.icon_mine_user);
                                break;
                            case 1:
                                relUserVip.setBackgroundResource(R.drawable.icon_mine_vip);
                                break;
                            case 2:
                                relUserVip.setBackgroundResource(R.drawable.icon_mine_svip);
                                break;
                            case 3:
                                relUserVip.setBackgroundResource(R.drawable.icon_mine_black);
                                break;
                        }
                        initView();
                    }
                }

                @Override
                public void onCheckUserInfoFail() {
                    refreshLayout.finishRefresh();
                }
            });
        }
    }

    /**
     * 获取订单状态数量
     */
    private void getOrderCount() {
        APIManager.startRequest(mOrderService.getOrderCount(), new BaseRequestListener<List<OrderCount>>() {

            @Override
            public void onSuccess(List<OrderCount> result) {
                super.onSuccess(result);

                Map<Integer, Integer> orderMap = new HashMap<>();
                orderMap.put(10, 0);
                orderMap.put(20, 0);
                orderMap.put(30, 0);
                for (OrderCount orderCount : result) {
                    orderMap.put(orderCount.getOrderStatus(), orderCount.getOrderCount());
                }

                oriderWaitPay.setBadge(orderMap.get(10));
                oriderWaitShip.setBadge(orderMap.get(20));
                oriderWaitReceived.setBadge(orderMap.get(30));
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }


        });
    }

    private void requestVipData() {
        APIManager.startRequest(iNewUserService.getUserIncome(), new BaseRequestListener<UserInComeBean>() {
            @Override
            public void onSuccess(UserInComeBean result) {
                super.onSuccess(result);
                if (result != null) {
                    userInComeBean = result;
                    tvIncomeDay.setText(NumberHandler.reservedDecimalFor2(result.getIncomeDay()));
                    tvIncomeMonth.setText(NumberHandler.reservedDecimalFor2(result.getIncomeMonth()));
                    tvIncomeTotal.setText(NumberHandler.reservedDecimalFor2(result.getIncomeTotal()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });


        APIManager.startRequest(iNewUserService.getMyCostom(), new BaseRequestListener<UserCostomBean>() {
            @Override
            public void onSuccess(UserCostomBean result) {
                super.onSuccess(result);
                if (result != null) {
                    tvCustom.setText(result.getClientCount() + "");
                    tvCustomOrder.setText(result.getClientOrderCount() + "");
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });

    }


    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestUserInfo();
    }

    @OnClick({R.id.iv_setting, R.id.title_setting, R.id.title_rel_message, R.id.orider_wait_pay, R.id.orider_wait_ship, R.id.orider_wait_received, R.id.orider_closed, R.id.ll_user_vip, R.id.rel_user_vip, R.id.ll_custom, R.id.btn_my_client
            , R.id.btn_invite_friends, R.id.btn_my_housekeeper, R.id.btn_financial_management, R.id.rel_message, R.id.btn_order_see_all
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
            case R.id.title_setting:
                startActivity(new Intent(mContext, XLSettingActivity.class));
                break;
            case R.id.rel_message:
            case R.id.title_rel_message:
                //消息
                startActivity(new Intent(mContext, XLNewsGroupActivity.class));
                break;
            case R.id.orider_wait_pay:
                jumpOrderList(ORDER_WAIT_PAY);
                break;
            case R.id.orider_wait_ship:
                jumpOrderList(ORDER_WAIT_SHIP);
                break;
            case R.id.orider_wait_received:
                jumpOrderList(ORDER_WAIT_RECEIVED);
                break;
            case R.id.orider_closed:
                jumpOrderList(ORDER_IS_RECEIVED);
                break;
            case R.id.ll_user_vip:
                if (userInComeBean != null) {
                    Intent intent = new Intent(mContext, IncomeIndexActivity.class);
                    intent.putExtra("userInComeBean", userInComeBean);
                    startActivity(intent);
                }
                break;
            case R.id.rel_user_vip:
                startActivity(new Intent(mContext, XLMemberCenterActivity.class));
                break;
            case R.id.ll_custom:
                //客户订单
                startActivity(new Intent(mContext, CustomerOrderActivity.class));
                break;
            case R.id.btn_my_client:
                //我的客户
                startActivity(new Intent(mContext, MyClientActivity.class));
                break;
            case R.id.btn_invite_friends:
                //邀请好友
                startActivity(new Intent(mContext, InviteFriendsActivity.class));
                break;
            case R.id.btn_my_housekeeper:
                //我的管家
                startActivity(new Intent(mContext, MyManagerActivity.class));
                break;
            case R.id.btn_financial_management:
                // 财务管理
                startActivity(new Intent(mContext, XLFinanceManangerActivity.class));
                break;
            case R.id.btn_order_see_all:
                //查看全部订单
                jumpOrderList(Constants.ORDER_ALL);
                break;
        }
    }

    private void jumpOrderList(String status) {
        Intent intent = new Intent(mContext, OrderListActivit.class);
        intent.putExtra(ORDER_STATUS, status);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case UPDATE_AVATAR://更新头像
                GlideUtils.loadHead(mContext, avatarIv, (String) message.getData());
                break;
            case UPDATE_NICK://更新昵称
                tvName.setText((String) message.getData());
                break;
            case UPDATEE_PHONE://更新电话
                tvPhone.setText(PhoneNumberUtil.getSecretPhoneNumber((String) message.getData()));
                break;
            case WEEK_CARD_OPEN:
                //开通了周卡,刷新
                requestUserInfo();
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyStatus status) {
        if (status != null) {
            if (status.messageCount > 0) {
                ivMessage.setVisibility(View.VISIBLE);
                titleMessage.setVisibility(View.VISIBLE);
            } else {
                ivMessage.setVisibility(View.INVISIBLE);
                titleMessage.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onScroll(int dx, int dy) {
        refreshLayout.setEnabled(dy == 0);

        float alpha = 0;
        if (dy > titleBarHeight) {
            //标题栏完全显示
            alpha = 1;
        } else {
            if (dy == 0) {
                //隐藏标题栏颜色
                alpha = 0;
            } else {
                //修改状态栏颜色透明度
                alpha = (float) ((dy * 1.0) / titleBarHeight);
            }
        }

        relTitle.setAlpha(alpha);

    }
}
