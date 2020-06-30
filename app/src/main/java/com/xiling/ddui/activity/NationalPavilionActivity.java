package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.ddui.fragment.NationalPavilionFragment;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.image.GlideUtils;
import com.xiling.module.MainActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.event.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.viewCart;

/**
 * @author pt
 * 国家馆
 */
public class NationalPavilionActivity extends BaseActivity {

    List<NationalPavilionBean> nationalPavilionBeanList;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();
    int mPosition = 0;
    @BindView(R.id.tv_cart_badge)
    TextView tvCartBadge;

    public static void jump(Context context, ArrayList<NationalPavilionBean> nationalPavilionBeanList, int position) {
        Intent intent = new Intent(context, NationalPavilionActivity.class);
        intent.putParcelableArrayListExtra("nationalPavilionBeanList", nationalPavilionBeanList);
        intent.putExtra("position", position);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_pavilion);
        ButterKnife.bind(this);
        setLeftBlack();
        setTitle("国家馆");

        if (getIntent() != null) {
            nationalPavilionBeanList = getIntent().getParcelableArrayListExtra("nationalPavilionBeanList");
            mPosition = getIntent().getIntExtra("position", 0);

            if (nationalPavilionBeanList.size() > 0) {
                for (NationalPavilionBean nationalPavilionBean : nationalPavilionBeanList) {
                    childNames.add(nationalPavilionBean.getCountryName());
                    fragments.add(NationalPavilionFragment.newInstance(nationalPavilionBean));
                }
                // viewpager.setOffscreenPageLimit(nationalPavilionBeanList.size());
                slidingTab.setViewPager(viewpager, childNames.toArray(new String[childNames.size()]), this, fragments);

                if (mPosition > 0) {
                    viewpager.setCurrentItem(mPosition);
                }

            }


        }

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
}
