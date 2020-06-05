package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.activity.CategorySecondActivity;
import com.xiling.ddui.activity.NationalPavilionActivity;
import com.xiling.ddui.adapter.CategoryAdapter;
import com.xiling.ddui.adapter.CategoryNavigationAdapter;
import com.xiling.ddui.adapter.NatonalPavilionAdapter;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.dduis.custom.NestRecyclerView;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author pt
 * 分类-一级页面
 */
public class ClassificationFragment extends BaseFragment  {


    @BindView(R.id.rv_category_nav)
    RecyclerView mRvCategoryNav;

    @BindView(R.id.rv_category)
    RecyclerView rvCategory;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    ArrayList<TopCategoryBean> topCategoryList;
    ArrayList<SecondCategoryBean.SecondCategoryListBean> secondCategoryList;

    @BindView(R.id.sdv_category_banner)
    ImageView sdvCategoryBanner;
    @BindView(R.id.tv_category)
    TextView tvCategory;

    LinearLayoutManager topLayoutMainager;

    @BindView(R.id.rv_national_pavilion)
    NestRecyclerView rvNationalPavilion;

    private IProductService mProductService;
    DDHomeService homeService;

    private CategoryNavigationAdapter mCategoryNavigationAdapter;
    private CategoryAdapter mCategoryAdapter;
    private NatonalPavilionAdapter natonalPavilionAdapter;
    private ArrayList<NationalPavilionBean> nationalPavilionBeanList = new ArrayList<>();

    private int childPosition = 0;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classification, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        getTopCategory();
        return view;
    }

    /**
     * 获取一级分类列表（左侧）
     */
    private void getTopCategory() {
        APIManager.startRequest(mProductService.getTopCategory(), new BaseRequestListener<ArrayList<TopCategoryBean>>(getActivity()) {
            @Override
            public void onSuccess(ArrayList<TopCategoryBean> result) {
                super.onSuccess(result);
                topCategoryList = result;
                TopCategoryBean nationalPavilionBean = new TopCategoryBean();
                nationalPavilionBean.setCategoryNameShort("国家馆");
                nationalPavilionBean.setCategoryId("12345");
                topCategoryList.add(nationalPavilionBean);
                mCategoryNavigationAdapter.setNewData(topCategoryList);
                childPosition = mCategoryNavigationAdapter.getmActiveIndex();

                if (topCategoryList.size() > childPosition) {
                    if (childPosition != topCategoryList.size() - 1) {
                        getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                    } else {
                        getNationalPavilionList();
                    }

                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 获取二级分类和品牌
     *
     * @param nodeId
     */
    private void getSecondCategory(String nodeId) {
        rvCategory.setVisibility(View.VISIBLE);
        rvNationalPavilion.setVisibility(View.GONE);
        APIManager.startRequest(mProductService.getSecondCategory(nodeId), new BaseRequestListener<SecondCategoryBean>(getActivity()) {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(SecondCategoryBean result) {
                super.onSuccess(result);
                mSmartRefreshLayout.finishRefresh();
                try {
                    if (result != null) {
                        GlideUtils.loadImage(mContext, sdvCategoryBanner, topCategoryList.get(childPosition).getBannerUrl());
                        tvCategory.setText(topCategoryList.get(childPosition).getCategoryName());
                        if (result.getSecondCategoryList() != null) {
                            secondCategoryList = result.getSecondCategoryList();
                            mCategoryAdapter.setNewData(result.getSecondCategoryList());
                        } else {
                            mCategoryAdapter.setNewData(new ArrayList<SecondCategoryBean.SecondCategoryListBean>());
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mSmartRefreshLayout.finishRefresh();
            }
        });
    }


    /**
     * 获取国家馆
     */
    private void getNationalPavilionList() {
        rvCategory.setVisibility(View.GONE);
        rvNationalPavilion.setVisibility(View.VISIBLE);

        APIManager.startRequest(homeService.getNationalPavilionList(), new BaseRequestListener<List<NationalPavilionBean>>() {

            @Override
            public void onSuccess(final List<NationalPavilionBean> result) {
                super.onSuccess(result);
                mSmartRefreshLayout.finishRefresh();
                if (result.size() > 0){
                    GlideUtils.loadImageNoDisk(mContext, sdvCategoryBanner, "http://oss.axiling.com/country/country.png");
                    tvCategory.setText("国家馆");
                    nationalPavilionBeanList.clear();
                    nationalPavilionBeanList.addAll(result);
                    natonalPavilionAdapter.setNewData(nationalPavilionBeanList);
                }


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mSmartRefreshLayout.finishRefresh();
            }

        });
    }

    private void initView() {

        mCategoryNavigationAdapter = new CategoryNavigationAdapter();
        mCategoryNavigationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCategoryNavigationAdapter.setmActiveIndex(position);
                childPosition = position;
                if (topCategoryList.size() > childPosition) {
                    if (childPosition != topCategoryList.size() - 1) {
                        getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                    } else {
                        getNationalPavilionList();
                    }
                }
            }
        });
        topLayoutMainager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvCategoryNav.setLayoutManager(topLayoutMainager);
        mRvCategoryNav.setAdapter(mCategoryNavigationAdapter);


        mCategoryAdapter = new CategoryAdapter();
        rvCategory.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 8), ScreenUtils.dip2px(getActivity(), 10)));
        rvCategory.setAdapter(mCategoryAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(mContext, 3));

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (topCategoryList != null && secondCategoryList != null) {
                    String parentId = topCategoryList.get(childPosition).getCategoryId();
                    String childId = secondCategoryList.get(position).getCategoryId();
                    CategorySecondActivity.jumpCategorySecondActivity(mContext, parentId, childId, topCategoryList.get(childPosition).getCategoryName());
                }
            }
        });

        natonalPavilionAdapter = new NatonalPavilionAdapter();
        rvNationalPavilion.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 8), ScreenUtils.dip2px(getActivity(), 10)));
        rvNationalPavilion.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvNationalPavilion.setAdapter(natonalPavilionAdapter);
        natonalPavilionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NationalPavilionActivity.jump(mContext,nationalPavilionBeanList,position);
            }
        });



        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                if (childPosition == topCategoryList.size() - 1){
                    getNationalPavilionList();
                }else if (childPosition < topCategoryList.size() -1){
                    getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                }

            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null) {
            getTopCategory();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
