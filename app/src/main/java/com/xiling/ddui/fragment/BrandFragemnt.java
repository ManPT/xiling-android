package com.xiling.ddui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.az.lettersort.azList.AZSideBarView;
import com.az.lettersort.azList.AZTitleDecoration;
import com.xiling.R;
import com.xiling.ddui.adapter.BrandAdapter;
import com.xiling.ddui.bean.BrandListBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author pt
 * 品牌
 */
public class BrandFragemnt extends BaseFragment {

    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;
    @BindView(R.id.bar_list)
    AZSideBarView barList;
    Unbinder unbinder;
    private IProductService mProductService;
    BrandAdapter brandAdapter;

    List<BrandListBean.GroupsBean.BrandsBean> brandsBeanList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand_fragemnt, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerList.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(mContext).setBackgroundColor(Color.parseColor("#f5f5f5"))));

        barList.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = brandAdapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (recyclerList.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerList.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        recyclerList.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });


        brandAdapter = new BrandAdapter(mContext,brandsBeanList);
        recyclerList.setAdapter(brandAdapter);

        getBrandList();
        return view;
    }

    private void getBrandList() {
        APIManager.startRequest(mProductService.getBrandList(), new BaseRequestListener<BrandListBean>(getActivity()) {
            @Override
            public void onSuccess(BrandListBean result) {
                super.onSuccess(result);

                barList.setmLetters(result.getInitialList());

                brandsBeanList.clear();
                for (BrandListBean.GroupsBean groupsBean : result.getGroups()){
                    brandsBeanList.addAll(groupsBean.getBrands());
                }

                brandAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null) {
            getBrandList();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
