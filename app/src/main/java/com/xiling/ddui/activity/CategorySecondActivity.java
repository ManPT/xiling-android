package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.bean.SecondClassificationBean;
import com.xiling.ddui.custom.ScreeningView;
import com.xiling.ddui.fragment.ShopFragment;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.module.MainActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xiling.shared.constant.Event.viewCart;

/**
 * @author 逄涛
 * 二级分类页面
 */
public class CategorySecondActivity extends BaseActivity {
    IProductService iProductService;

    @BindView(R.id.headerLayout)
    HeaderLayout mHeaderLayout;
    Unbinder unbinder;

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_shop)
    ViewPager viewpagerShop;
    @BindView(R.id.screenView)
    ScreeningView screenView;
    @BindView(R.id.tv_cart_badge)
    TextView tvCartBadge;


    private ArrayList<Fragment> fragments = new ArrayList<>();


    private String categoryId;

    // 当前在第几个分类
    private int mPosition = 0;
    private String mParentId;
    private String mParentName;
    private List<SecondCategoryBean.SecondCategoryListBean> secondCategoryList = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();

    private String minPrice, maxPrice;

    /**
     * 排序属性 0-价格,1-上新,2-销量，3-利润
     */
    private int orderBy = 4;

    /**
     * 排序方式 0-降序(Desc), 1-升序(Asc)
     */
    private int orderType = 2;
    /**
     * 售卖类型(1-一件代发,2-批采)
     */
    private String saleType = "";

    /**
     * 贸易类型(1-国内品牌-国内OEM,2-跨境保税,3-一般贸易,4-海外直邮)
     */
    private String tradeType = "";


    private String keyWord = "";


    public static void jumpCategorySecondActivity(Context mContext, String parentId, String categoryId, String parentName) {
        Intent intent = new Intent(mContext, CategorySecondActivity.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("parentId", parentId);
        intent.putExtra("parentName", parentName);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_second);
        unbinder = ButterKnife.bind(this);
        iProductService = ServiceManager.getInstance().createService(IProductService.class);
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            mParentId = getIntent().getStringExtra("parentId");
            mParentName = getIntent().getStringExtra("parentName");
        }
        getSecondClassification();

    }


    private void getSecondClassification() {
        APIManager.startRequest(iProductService.getSecondClassification(mParentId), new BaseRequestListener<SecondClassificationBean>() {
            @Override
            public void onSuccess(SecondClassificationBean result) {
                super.onSuccess(result);
                secondCategoryList = result.getSecondCategoryList();
                if (secondCategoryList != null) {
                    fragments.clear();
                    childNames.clear();
                    for (int i = 0; i < secondCategoryList.size(); i++) {
                        SecondCategoryBean.SecondCategoryListBean secondCategoryListBean = secondCategoryList.get(i);
                        if (secondCategoryListBean.getCategoryId().equals(categoryId)) {
                            mPosition = i;
                        }
                        childNames.add(secondCategoryListBean.getCategoryName());
                        fragments.add(ShopFragment.newInstance(mParentId, secondCategoryListBean.getCategoryId(), "", minPrice, maxPrice, orderBy, orderType, saleType, tradeType, ""));
                    }
                }

                initView();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    public void initView() {
        mHeaderLayout.setTitle(mParentName);
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
        mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        slidingTab.setViewPager(viewpagerShop, childNames.toArray(new String[childNames.size()]), this, fragments);
        viewpagerShop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewpagerShop.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (mPosition > 0) {
            viewpagerShop.setCurrentItem(mPosition);
        }

        screenView.setOnItemClickLisener(new ScreeningView.OnItemClickLisener() {
            @Override
            public void onSort(int orderBy, int orderType) {
                CategorySecondActivity.this.orderBy = orderBy;
                CategorySecondActivity.this.orderType = orderType;
                for (Fragment fragment : fragments) {
                    ((ShopFragment) fragment).requestShopFill(minPrice, maxPrice, orderBy, orderType, saleType, tradeType, keyWord);
                }
            }

            @Override
            public void onFilter(String tradeType, String saleType, String minPrice, String maxPrice) {
                CategorySecondActivity.this.minPrice = minPrice;
                CategorySecondActivity.this.maxPrice = maxPrice;
                CategorySecondActivity.this.tradeType = tradeType;
                CategorySecondActivity.this.saleType = saleType;
                for (Fragment fragment : fragments) {
                    ((ShopFragment) fragment).requestShopFill(minPrice, maxPrice, orderBy, orderType, saleType, tradeType, keyWord);
                }
            }
        });

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

    @OnClick(R.id.btn_go_card)
    public void onViewClicked() {
        startActivity(new Intent(context, MainActivity.class));
        EventBus.getDefault().post(new EventMessage(viewCart));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
