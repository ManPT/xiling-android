package com.xiling.dduis.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.activity.CategorySecondActivity;
import com.xiling.ddui.activity.RealAuthActivity;
import com.xiling.ddui.activity.XLMemberCenterActivity;
import com.xiling.ddui.activity.XLNewsGroupActivity;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.NewcomerDiscountDialog;
import com.xiling.ddui.manager.AutoClickManager;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.view.RLoopRecyclerView;
import com.xiling.ddui.view.RPagerSnapHelper;
import com.xiling.dduis.adapter.HomeActivityAdapter;
import com.xiling.dduis.adapter.HomeBrandAdapter;
import com.xiling.dduis.adapter.HomeHotAdapter;
import com.xiling.dduis.adapter.HomeTabAdapter;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.BannerManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.search.SearchActivity;
import com.xiling.module.splash.SplashActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.xiling.shared.Constants.PAGE_SIZE;
import static com.xiling.shared.constant.Event.viewCenter;


public class DDHomeMainFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    Unbinder unbinder;
    DDHomeService homeService;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recyclerView_hot)
    RecyclerView recyclerViewHot;
    HomeHotAdapter hotAdapter;
    List<HomeDataBean.SecondCategoryListBean> hots = new ArrayList<>();

    @BindView(R.id.banner)
    Banner banner;
    List<String> bannerList = new ArrayList<>();


    @BindView(R.id.recyclerView_tab)
    RecyclerView recyclerViewTab;
    List<HomeDataBean.TabListBean> tabListBeanList = new ArrayList<>();
    HomeTabAdapter homeTabAdapter;

    @BindView(R.id.recyclerView_activity)
    RecyclerView recyclerViewActivity;
    List<HomeDataBean.ActivityListBean> activityBeanList = new ArrayList<>();
    HomeActivityAdapter activityAdapter;

    @BindView(R.id.recyclerView_brand)
    RLoopRecyclerView recyclerViewBrand;
    List<HomeDataBean.BrandHotSaleListBean> brandList = new ArrayList<>();
    HomeBrandAdapter brandAdapter;
    int brandPosition = 1, brandSize = 0;
    @BindView(R.id.tv_brandPosition)
    TextView tvBrandPosition;
    @BindView(R.id.tv_brandSize)
    TextView tvBrandSize;
    @BindView(R.id.rel_brand_head)
    RelativeLayout relBrandHead;

    @BindView(R.id.recyclerView_recommend)
    RecyclerView recyclerViewRecommend;
    List<HomeRecommendDataBean.DatasBean> recommendDataList = new ArrayList<>();
    ShopListAdapter recommendAdapter;
    int pageOffset = 1, pageSize = PAGE_SIZE, totalPage;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.iv_headIcon)
    CircleImageView ivHeadIcon;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.ll_brand)
    LinearLayout llBrand;
    private LinearLayoutManager bannerLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化协议
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_s_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        checkUserInfo();
        showNewComerDialog();
        return view;
    }

    private void showNewComerDialog() {
        SPUtils spUtils = new SPUtils(SplashActivity.class.getName() + "_" + BuildConfig.VERSION_NAME);
        if (!spUtils.getBoolean("oneStart")) {
            NewcomerDiscountDialog newcomerDiscountDialog = new NewcomerDiscountDialog(mContext);
            newcomerDiscountDialog.show();
            spUtils.putBoolean("oneStart", true);
        }


    }

    private void initView() {
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerViewHot.setLayoutManager(gridLayoutManager);
        hotAdapter = new HomeHotAdapter(R.layout.item_home_hot, hots);
        recyclerViewHot.setAdapter(hotAdapter);

        LinearLayoutManager tabLayoutManager = new LinearLayoutManager(getActivity());
        tabLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTab.setLayoutManager(tabLayoutManager);
        homeTabAdapter = new HomeTabAdapter(R.layout.item_home_tab, tabListBeanList);
        recyclerViewTab.setAdapter(homeTabAdapter);

        LinearLayoutManager activityLayoutManager = new LinearLayoutManager(getActivity());
        activityLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewActivity.setLayoutManager(activityLayoutManager);
        activityAdapter = new HomeActivityAdapter(R.layout.item_home_activity, activityBeanList);
        recyclerViewActivity.setAdapter(activityAdapter);


        bannerLayoutManager = new LinearLayoutManager(getActivity());
        bannerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBrand.setLayoutManager(bannerLayoutManager);
        brandAdapter = new HomeBrandAdapter(mContext);
        recyclerViewBrand.setAdapter(brandAdapter);
      /*  PagerSnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                //监听滑动到第几个

            }
        };
        snapHelper.attachToRecyclerView(recyclerViewBrand);*/

        RPagerSnapHelper rPagerSnapHelper = new RPagerSnapHelper();
        rPagerSnapHelper.setOnPageListener(new RPagerSnapHelper.OnPageListener() {
            @Override
            public void onPageSelector(int position) {
                position %= brandList.size();
                if (position < 0) {
                    position = brandList.size() + position;
                }
                brandPosition = position+1;
                tvBrandPosition.setText(brandPosition + "");
            }
        });
        rPagerSnapHelper.attachToRecyclerView(recyclerViewBrand);

        GridLayoutManager recommendLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewRecommend.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 12), ScreenUtils.dip2px(getActivity(), 12)));
        recyclerViewRecommend.setLayoutManager(recommendLayoutManager);
        recommendAdapter = new ShopListAdapter(R.layout.item_old_home_recommend, recommendDataList);
        recyclerViewRecommend.setAdapter(recommendAdapter);
    }


    /**
     * 请求数据
     */
    private void requestData() {

        APIManager.startRequest(homeService.getHomeData(), new BaseRequestListener<HomeDataBean>() {
            @Override
            public void onSuccess(final HomeDataBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                if (result != null) {
                    //热搜
                    hots = result.getSecondCategoryList();
                    if (hots == null) {
                        hots = new ArrayList<>();
                    }
                    //如果超过四个，只保留四个
                    if (hots.size() > 4) {
                        hots.subList(0, 3);
                    }

                    hotAdapter.setNewData(hots);
                    hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            CategorySecondActivity.jumpCategorySecondActivity(mContext, hots.get(position).getParentId(), hots.get(position).getCategoryId());
                        }
                    });


                    //轮播图
                    bannerList.clear();
                    if (result.getBannerList() != null) {
                        for (HomeDataBean.BannerListBean bannerBean : result.getBannerList()) {
                            if (bannerBean.getImgUrl() != null) {
                                bannerList.add(bannerBean.getImgUrl());
                            }
                        }
                    }

                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            AutoClickManager.pars(mContext, result.getBannerList().get(position));
                        }
                    });

                    BannerManager.startBanner(banner, bannerList);

                    //tab
                    tabListBeanList.clear();
                    if (result.getTabList() != null) {
                        tabListBeanList = result.getTabList();
                    }

                    if (tabListBeanList.size() > 0) {
                        llTab.setVisibility(View.VISIBLE);
                    } else {
                        llTab.setVisibility(View.GONE);
                    }

                    homeTabAdapter.setNewData(tabListBeanList);
                    homeTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            AutoClickManager.pars(mContext, tabListBeanList.get(position));
                        }
                    });
                    //activity
                    activityBeanList.clear();
                    if (result.getActivityList() != null) {
                        if (result.getActivityList().size() >= 3) {
                            //超过三个取前三个
                            for (int i = 0; i < 3; i++) {
                                HomeDataBean.ActivityListBean activityListBean = result.getActivityList().get(i);
                                activityBeanList.add(activityListBean);
                            }
                        }
                    }

                    if (activityBeanList.size() == 0) {
                        llActivity.setVisibility(View.GONE);
                    } else {
                        llActivity.setVisibility(View.VISIBLE);
                    }

                    activityAdapter.setNewData(activityBeanList);
                    activityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            AutoClickManager.pars(mContext, activityBeanList.get(position));
                        }
                    });
                    //brand

                    if (result.getBrandHotSaleList() != null) {
                        brandList = result.getBrandHotSaleList();
                    }

                    if (brandList == null) {
                        brandList = new ArrayList<>();
                    }
                    if (brandList.size() > 0) {
                        llBrand.setVisibility(View.VISIBLE);
                        brandAdapter.setNewData(brandList);
                        brandSize = brandList.size();
                        tvBrandPosition.setText(brandPosition + "");
                        tvBrandSize.setText(brandSize + "");
                    } else {
                        llBrand.setVisibility(View.GONE);
                    }
                    recyclerViewBrand.scrollToPosition(brandAdapter.getItemRawCount() * 10000);//开始时的偏移量
                    brandAdapter.setOnItemClickListener(new HomeBrandAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(HomeDataBean.BrandHotSaleListBean bean, int position) {
                            AutoClickManager.pars(mContext, brandList.get(position));
                        }

                       /* @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                        }*/
                    });


                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
            }
        });
        requestRecommend();
    }

    private void checkUserInfo() {

        if (UserManager.getInstance().isLogin()) {
            //如果登录过，先进行用户信息校验，保证用户信息准确性
            UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
                @Override
                public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                    updateUserInfo();
                    requestData();
                }

                @Override
                public void onCheckUserInfoFail() {
                    requestData();
                }
            });
        } else {
            requestData();
        }

    }

    /**
     * 请求推荐数据
     */
    private void requestRecommend() {
        APIManager.startRequest(homeService.getHomeRecommendData(pageOffset, pageSize), new BaseRequestListener<HomeRecommendDataBean>() {
            @Override
            public void onSuccess(HomeRecommendDataBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                if (result != null) {
                    if (result.getDatas() != null) {
                        if (pageOffset == 1) {
                            recommendDataList.clear();
                        }
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        recommendDataList.addAll(result.getDatas());

                        if (pageOffset == 1) {
                            recommendAdapter.setNewData(result.getDatas());
                        } else {
                            recommendAdapter.addData(result.getDatas());
                        }

                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        pageOffset++;
        requestRecommend();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        checkUserInfo();
    }

    @OnClick({R.id.btn_user, R.id.rel_search, R.id.btn_message, R.id.iv_headIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_user:
                if (UserManager.getInstance().isLogin(mContext)) {
                    if (UserManager.getInstance().getUserLevel() == 0) {
                        D3ialogTools.showAlertDialog(mContext, "请先实名认证当前商户信息", "去认证", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(mContext, RealAuthActivity.class));
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }else{
                        startActivity(new Intent(mContext,XLMemberCenterActivity.class));
                    }
                }
                break;
            case R.id.rel_search:
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_message:
                startActivity(new Intent(mContext, XLNewsGroupActivity.class));
                break;
            case R.id.iv_headIcon:
                //头像点击进入个人中心
                if (UserManager.getInstance().isLogin(mContext)) {
                    EventBus.getDefault().post(new EventMessage(viewCenter));
                }
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(EventMessage message) {
        switch (message.getEvent()) {
            case LOGIN_SUCCESS:
            case LOGIN_OUT:
                updateUserInfo();
                break;
            case UPDATE_AVATAR:
                GlideUtils.loadHead(mContext, ivHeadIcon, (String) message.getData());
                break;
            case UPDATE_NICK:
                btnLogin.setText((String) message.getData());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyStatus status) {
        if (status != null) {
            DLog.i("onEvent+MessageCount:" + status.messageCount);
            if (status.messageCount > 0) {
                ivMessage.setVisibility(View.VISIBLE);
            } else {
                ivMessage.setVisibility(View.INVISIBLE);
            }
        }
    }


    private void updateUserInfo() {
        //登录成功
        final NewUserBean user = UserManager.getInstance().getUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getNickName())) {
                btnLogin.setText(user.getNickName());
                tvGrade.setVisibility(View.VISIBLE);
                GlideUtils.loadHead(mContext, ivHeadIcon,"http://thirdwx.qlogo.cn/mmopen/vi_32/H4VAG1DFhicZyg1cicT9gXIQlzFvibR3Atd0kM9ibJqPN8ZtFv85Eecejxqdq182xYnfZnygFlXRFQTsAAfVZiaMOkQ/132");

                switch (UserManager.getInstance().getUserLevel()) {
                    case 0:
                        //注册用户
                        tvGrade.setBackgroundResource(R.drawable.bg_home_register);
                        break;
                    case 10:
                        tvGrade.setBackgroundResource(R.drawable.bg_home_user);
                        break;
                    case 20:
                        tvGrade.setBackgroundResource(R.drawable.bg_home_vip);
                        break;
                    case 30:
                        tvGrade.setBackgroundResource(R.drawable.bg_home_back);
                        break;
                }

            }
        } else {
            //退出登录
            btnLogin.setText("登录/注册");
            tvGrade.setVisibility(View.GONE);
            ivHeadIcon.setImageResource(R.drawable.bg_circle_head);
        }
    }


}
