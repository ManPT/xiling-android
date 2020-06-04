package com.xiling.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.manager.ShopCardManager;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.module.MainActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;
import static com.xiling.shared.constant.Event.viewCart;

/**
 * @author pt
 * 商品Fragment
 */
public class ShopFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recycler_shop)
    RecyclerView recyclerShop;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    Unbinder unbinder;
    @BindView(R.id.tv_cart_badge)
    TextView tvCartBadge;
    @BindView(R.id.btn_go_card)
    RelativeLayout btnGoCard;
    private IProductService mProductService;

    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;
    List<HomeRecommendDataBean.DatasBean> shopDataList = new ArrayList<>();
    ShopListAdapter shopAdapter;

    private String indexCategoryId;
    private String categoryId;
    private String secondCategoryId;
    private String counrtyId;

    private String minPrice, maxPrice;
    private int orderBy;
    private int orderType;
    private String saleType;
    private String tradeType;
    private String keyWord;
    private String brandId;

    HashMap<String, String> requestMap = new HashMap<>();

    public static ShopFragment newInstance(String categoryId, String secondCategoryId, String brandId, String minPrice, String maxPrice, int orderBy, int orderType, String saleType, String tradeType, String keyWord) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("secondCategoryId", secondCategoryId);
        args.putString("brandId", brandId);
        args.putString("minPrice", minPrice);
        args.putString("maxPrice", maxPrice);
        args.putInt("orderBy", orderBy);
        args.putInt("orderType", orderType);
        args.putString("keyWord", keyWord);
        args.putString("saleType", saleType);
        args.putString("tradeType", tradeType);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShopFragment newInstance(String indexCategoryId, String brandId, String minPrice, String maxPrice, int orderBy, int orderType, String saleType, String tradeType, String keyWord) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString("indexCategoryId", indexCategoryId);
        args.putString("brandId", brandId);
        args.putString("minPrice", minPrice);
        args.putString("maxPrice", maxPrice);
        args.putInt("orderBy", orderBy);
        args.putInt("orderType", orderType);
        args.putString("keyWord", keyWord);
        args.putString("saleType", saleType);
        args.putString("tradeType", tradeType);
        fragment.setArguments(args);
        return fragment;
    }


    public static ShopFragment newInstance(String countryId, String minPrice, String maxPrice, int orderBy, int orderType, String saleType, String tradeType, String keyWord) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString("countryId", countryId);
        args.putString("minPrice", minPrice);
        args.putString("maxPrice", maxPrice);
        args.putInt("orderBy", orderBy);
        args.putInt("orderType", orderType);
        args.putString("keyWord", keyWord);
        args.putString("saleType", saleType);
        args.putString("tradeType", tradeType);
        fragment.setArguments(args);
        return fragment;
    }


    private void getArgumentsData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            indexCategoryId = arguments.getString("indexCategoryId");
            categoryId = arguments.getString("categoryId");
            secondCategoryId = arguments.getString("secondCategoryId");
            brandId = arguments.getString("brandId");
            minPrice = arguments.getString("minPrice");
            maxPrice = arguments.getString("maxPrice");
            keyWord = arguments.getString("keyWord");
            orderBy = arguments.getInt("orderBy");
            orderType = arguments.getInt("orderType");
            saleType = arguments.getString("saleType");
            tradeType = arguments.getString("tradeType");
            counrtyId = arguments.getString("counrtyId");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentsData();
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ShopCardManager.getInstance().requestUpDataShopCardCount();
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        // btnGoCard.setVisibility(TextUtils.isEmpty(secondCategoryId)?View.VISIBLE:View.GONE);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager shopLayoutManager = new LinearLayoutManager(getActivity());

        recyclerShop.setLayoutManager(shopLayoutManager);
        shopAdapter = new ShopListAdapter(R.layout.item_home_recommend, shopDataList);
        recyclerShop.setAdapter(shopAdapter);
        requestShop(minPrice, maxPrice, orderBy, orderType, saleType, tradeType, keyWord);
        return view;
    }

    /**
     * 请求数据
     */
    public void requestShopFill(String minPrice, String maxPrice, int orderBy, int orderType, String saleType, String tradeType, String keyWord) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("minPrice", minPrice);
            arguments.putString("maxPrice", maxPrice);
            arguments.putString("keyWord", keyWord);
            arguments.putInt("orderBy", orderBy);
            if (orderType > 0){
                arguments.putInt("orderType", orderType);
            }

            arguments.putString("saleType", saleType);
            arguments.putString("tradeType", tradeType);

        }
        this.pageOffset = 1;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.orderBy = orderBy;
        this.orderType = orderType;
        this.saleType = saleType;
        this.tradeType = tradeType;
        this.keyWord = keyWord;
        requestMap.clear();
        if (!TextUtils.isEmpty(categoryId)) {
            requestMap.put("categoryId", categoryId);
        }

        if (!TextUtils.isEmpty(secondCategoryId)) {
            requestMap.put("secondCategoryIdList", secondCategoryId + "");
        }

        if (!TextUtils.isEmpty(brandId)) {
            requestMap.put("brandId", brandId);
        }

        if (!TextUtils.isEmpty(this.minPrice)) {
            requestMap.put("minPrice", this.minPrice + "00");
        }

        if (!TextUtils.isEmpty(this.maxPrice)) {
            requestMap.put("maxPrice", this.maxPrice + "00");
        }
        if (!TextUtils.isEmpty(keyWord)) {
            requestMap.put("keyWord", keyWord);
        }

        if (!TextUtils.isEmpty(saleType)) {
            requestMap.put("saleType", saleType);
        }

        if (!TextUtils.isEmpty(tradeType)) {
            requestMap.put("tradeType", tradeType);
        }

        requestMap.put("orderBy", orderBy + "");
        requestMap.put("orderType", orderType + "");
        requestMap.put("pageOffset", pageOffset + "");
        requestMap.put("pageSize", pageSize + "");
        Log.d("requestJSON", "request map = " + requestMap.toString());
        if (mProductService != null && getActivity() != null) {
            APIManager.startRequest(mProductService.getProductList(requestMap), new BaseRequestListener<HomeRecommendDataBean>() {
                @Override
                public void onSuccess(HomeRecommendDataBean result) {
                    super.onSuccess(result);
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();

                    if (result != null) {
                        if (result.getDatas() != null) {
                            if (pageOffset == 1) {
                                shopDataList.clear();
                            }
                            totalPage = result.getTotalPage();
                            // 如果已经到最后一页了，关闭上拉加载
                            if (pageOffset >= totalPage) {
                                smartRefreshLayout.setEnableLoadMore(false);
                            } else {
                                smartRefreshLayout.setEnableLoadMore(true);
                            }

                            shopDataList.addAll(result.getDatas());

                            if (pageOffset == 1) {
                                shopAdapter.setNewData(result.getDatas());
                            } else {
                                shopAdapter.addData(result.getDatas());
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
    }

    public void requestShop(String minPrice, String maxPrice, int orderBy, int orderType, String saleType, String tradeType, String keyWord) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("minPrice", minPrice);
            arguments.putString("maxPrice", maxPrice);
            arguments.putString("keyWord", keyWord);
            arguments.putInt("orderBy", orderBy);
            arguments.putInt("orderType", orderType);
            arguments.putString("saleType", saleType);
            arguments.putString("tradeType", tradeType);
        }
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.orderBy = orderBy;
        this.orderType = orderType;
        this.keyWord = keyWord;
        this.saleType = saleType;
        this.tradeType = tradeType;
        requestMap.clear();

        if (!TextUtils.isEmpty(indexCategoryId)) {
            requestMap.put("indexCategoryId", indexCategoryId);
        }

        if (!TextUtils.isEmpty(categoryId)) {
            requestMap.put("categoryId", categoryId);
        }

        if (!TextUtils.isEmpty(secondCategoryId)) {
            requestMap.put("secondCategoryIdList", secondCategoryId + "");
        }

        if (!TextUtils.isEmpty(brandId)) {
            requestMap.put("brandId", brandId);
        }

        if (!TextUtils.isEmpty(minPrice)) {
            requestMap.put("minPrice", minPrice + "00");
        }

        if (!TextUtils.isEmpty(maxPrice)) {
            requestMap.put("maxPrice", maxPrice + "00");
        }

        if (!TextUtils.isEmpty(keyWord)) {
            requestMap.put("keyWord", keyWord);
        }

        if (!TextUtils.isEmpty(saleType)) {
            requestMap.put("saleType", saleType);
        }

        if (!TextUtils.isEmpty(tradeType)) {
            requestMap.put("tradeType", tradeType);
        }

        if (!TextUtils.isEmpty(counrtyId)) {
            requestMap.put("counrtyId", counrtyId);
        }



        requestMap.put("orderBy", orderBy + "");
        requestMap.put("orderType", orderType + "");

        requestMap.put("pageOffset", pageOffset + "");
        requestMap.put("pageSize", pageSize + "");
        Log.d("requestJSON", "request map = " + requestMap.toString());
        if (mProductService != null && getActivity() != null) {
            APIManager.startRequest(mProductService.getProductList(requestMap), new BaseRequestListener<HomeRecommendDataBean>() {
                @Override
                public void onSuccess(HomeRecommendDataBean result) {
                    super.onSuccess(result);
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();

                    if (result != null) {
                        if (result.getDatas() != null) {
                            if (pageOffset == 1) {
                                shopDataList.clear();
                            }
                            totalPage = result.getTotalPage();
                            // 如果已经到最后一页了，关闭上拉加载
                            if (pageOffset >= totalPage) {
                                smartRefreshLayout.setEnableLoadMore(false);
                            } else {
                                smartRefreshLayout.setEnableLoadMore(true);
                            }

                            shopDataList.addAll(result.getDatas());

                            if (pageOffset == 1) {
                                shopAdapter.setNewData(result.getDatas());
                            } else {
                                shopAdapter.addData(result.getDatas());
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
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        if (pageOffset < totalPage) {
            pageOffset++;
            requestShop(minPrice, maxPrice, orderBy, orderType, saleType, tradeType, keyWord);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        requestShop(minPrice, maxPrice, orderBy, orderType, saleType, tradeType, keyWord);
    }

    /**
     * 接收购物车数量变更
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EventMessage message) {
        switch (message.getEvent()) {
            case cartAmountUpdate:
                int total = (int) message.getData();
                ViewUtil.setCartBadge(total, tvCartBadge);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_go_card)
    public void onViewClicked() {
        startActivity(new Intent(mContext, MainActivity.class));
        EventBus.getDefault().post(new EventMessage(viewCart));
    }
}
