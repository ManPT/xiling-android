package com.xiling.module.community;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * @author bigbyto
 * @date 19/03/2017
 * fragment
 * stone：新添加基类方法
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    private View rootView;
    protected Unbinder mBind;
    protected LayoutInflater mInflater;
    private boolean isVisiable = false;
    protected boolean isDataInitiated;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int fragmentResId = getFragmentResId();
        rootView = inflater.inflate(fragmentResId, container, false);
        mBind = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isVisiable = true;
        initViewConfig();
        initDataNew();
        initListener();
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData();
        }
    }

    private void loadData() {
        if (getUserVisibleHint() && isVisiable && !isDataInitiated) {
            isDataInitiated = true;
            lazyLoadData();
        }
    }

    public boolean needLogin() {
        return false;
    }

    protected void lazyLoadData() {

    }

    protected void initListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    protected void initViewConfig() {

    }

    protected void initDataNew() {

    }

    protected int getFragmentResId() {
        return -1;
    }

/*    protected boolean checkLogin() {
        boolean isLogin = UserDao.getInstance().isLogin();
        if (isLogin) {
            return true;
        }

        toLogin();

        return false;
    }*/

    protected void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    protected void showLoading() {
     /*   if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }*/
    }

    protected void hideLoading() {
        ToastUtil.hideLoading();
    }

    public void showToast(String message) {
        //        ToastUtils.show(mActivity, message);
    }

    public void showError(Throwable e) {
        ToastUtil.error(e.getMessage());
    }

    public void onCheck() {

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void requestPermission(final PermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && listener != null) {
                    listener.onSuccess();
                } else {
                    ToastUtils.showShortToast("请打开权限");
                }
            }
        });
    }

    public interface PermissionListener {
        void onSuccess();
    }

    public void gotoLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
    }
}
