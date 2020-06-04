package com.xiling.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.module.search.SearchActivity;
import com.xiling.shared.basic.BaseFragment;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author 逄涛
 * 首页-分类
 */
public class DDCategoryFragment extends BaseFragment {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_shop)
    ViewPager viewpagerShop;
    Unbinder unbinder;
    private List<String> childNames = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    Fragment currentFragment;
    public DDCategoryFragment() {
    }

    public static DDCategoryFragment newInstance() {
        DDCategoryFragment fragment = new DDCategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ddcategory, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView(){
        childNames.clear();
        childNames.add("分类");
        childNames.add("品牌");

        fragments.clear();
        fragments.add(new ClassificationFragment());
        fragments.add(new BrandFragemnt());
        currentFragment = fragments.get(0);

        slidingTab.setViewPager(viewpagerShop, childNames.toArray(new String[childNames.size()]), getActivity(), fragments);

        viewpagerShop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = fragments.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }






    @OnClick({R.id.tv_search})
    void onClickSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null) {
            currentFragment.setUserVisibleHint(isVisibleToUser);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
