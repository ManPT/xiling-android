package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.adapter.NationalPavilionBrandAdapter;
import com.xiling.ddui.bean.BrandListBean;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * 国家馆 fragment
 *
 * @author pt
 */
public class NationalPavilionFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    NationalPavilionBean mNationalPavilionBean;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    Unbinder unbinder;
    @BindView(R.id.recycler_brand)
    RecyclerView recyclerBrand;
    IProductService iProductService;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    NationalPavilionBrandAdapter adapter;
    List<BrandListBean.GroupsBean.BrandsBean> brandsBeanList = new ArrayList<>();
    @BindView(R.id.recycler_shop)
    RecyclerView recyclerShop;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    List<HomeRecommendDataBean.DatasBean> shopDataList = new ArrayList<>();
    ShopListAdapter shopAdapter;
    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;


    public static NationalPavilionFragment newInstance(NationalPavilionBean nationalPavilionBean) {
        NationalPavilionFragment fragment = new NationalPavilionFragment();
        Bundle args = new Bundle();
        args.putParcelable("nationalPavilionBean", nationalPavilionBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNationalPavilionBean = getArguments().getParcelable("nationalPavilionBean");
            iProductService = ServiceManager.getInstance().createService(IProductService.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_national_pavilion, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerBrand.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerBrand.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(mContext, 5), ScreenUtils.dip2px(mContext, 5)));
        adapter = new NationalPavilionBrandAdapter(brandsBeanList);
        recyclerBrand.setAdapter(adapter);
        getBrandList();
        return view;
    }


    private void initView() {
        String url =  mNationalPavilionBean.getCountryDetailBanner();
        GlideUtils.loadImage(mContext, ivBanner,url);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager shopLayoutManager = new LinearLayoutManager(getActivity());
        recyclerShop.setLayoutManager(shopLayoutManager);
        shopAdapter = new ShopListAdapter(R.layout.item_home_recommend, shopDataList);
        recyclerShop.setAdapter(shopAdapter);
        requestShop();

    }


    public void requestShop() {
        HashMap<String, String> requestMap = new HashMap<>();


        if (!TextUtils.isEmpty(mNationalPavilionBean.getCountryId())) {
            requestMap.put("countryId", mNationalPavilionBean.getCountryId());
        }



        requestMap.put("orderBy",  "4");
        requestMap.put("orderType", "0");

        requestMap.put("pageOffset", pageOffset + "");
        requestMap.put("pageSize", pageSize + "");
        Log.d("requestJSON", "request map = " + requestMap.toString());
        if (iProductService != null && getActivity() != null) {
            APIManager.startRequest(iProductService.getProductList(requestMap), new BaseRequestListener<HomeRecommendDataBean>() {
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



    private void getBrandList() {
        APIManager.startRequest(iProductService.getNationalPavilionDetail(mNationalPavilionBean.getCountryId()), new BaseRequestListener<List<BrandListBean.GroupsBean.BrandsBean>>() {

            @Override
            public void onSuccess(List<BrandListBean.GroupsBean.BrandsBean> result) {
                super.onSuccess(result);
                if (result.size() > 0) {
                    tvBrand.setVisibility(View.VISIBLE);
                    recyclerBrand.setVisibility(View.VISIBLE);
                    brandsBeanList = result;
                    adapter.setNewData(brandsBeanList);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            BrandActivity.jumpBrandActivity(mContext, "", brandsBeanList.get(position).getBrandId());
                        }
                    });
                } else {
                    tvBrand.setVisibility(View.GONE);
                    recyclerBrand.setVisibility(View.GONE);
                }
                initView();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                tvBrand.setVisibility(View.GONE);
                recyclerBrand.setVisibility(View.GONE);
                initView();
            }


        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        if (pageOffset < totalPage) {
            pageOffset++;
            requestShop();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        getBrandList();
    }

}
