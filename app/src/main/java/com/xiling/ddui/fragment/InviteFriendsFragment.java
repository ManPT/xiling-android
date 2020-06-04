package com.xiling.ddui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @auth 逄涛
 * 邀请好友viewpager搭载的fregment
 */
public class InviteFriendsFragment extends BaseFragment {

    String url = "";
    String shareUrl = "";
    @BindView(R.id.iv_image)
    ImageView ivImage;
    Unbinder unbinder;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.parentView)
    View parentView;
    int index;

    public static InviteFriendsFragment newInstance(String url, int index) {
        InviteFriendsFragment fragment = new InviteFriendsFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
            index = getArguments().getInt("index");
            switch (index) {
                case 0:
                    shareUrl =  BuildConfig.BASE_URL + "main?inviteCode="+UserManager.getInstance().getUser().getInviteCode();
                    break;
                case 1:
                    shareUrl = BuildConfig.BASE_URL+"download?xl_from=2";
                    break;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_friends, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        GlideUtils.loadImageALL(mContext, ivImage, url);
        NewUserBean userBean = UserManager.getInstance().getUser();
        if (userBean != null) {
            GlideUtils.loadHead(mContext, ivHead, userBean.getHeadImage());
            String mName = userBean.getNickName();
            if (mName.length() > 6) {
                mName = mName.substring(0, 6);
            }
            mName = mName + "邀请你注册喜领商城";
            tvUserName.setText(mName);
            tvInviteCode.setText(userBean.getInviteCode());
        }
        ShareUtils.createQRImage(mContext, shareUrl, new ShareUtils.OnQRImageListener() {
            @Override
            public void onCreatQR(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQr.setImageBitmap(bitmap);
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        });

    }

    /**
     * 截图，分享到微信或者下载到本地
     *
     * @param way
     */
    public void shareWechat(final SHARE_MEDIA way) {
        if (way != null) {
           // ShareUtils.shareTo3rdPlatform(getActivity(), parentView, way, "friend");
            if (index == 0){
                ShareUtils.share(getActivity(),"精彩全球优质好货",UserManager.getInstance().getUser().getNickName()+"邀请您注册喜领商城",R.mipmap.ic_launcher,shareUrl,way,null);
            }else if (index == 1){
                ShareUtils.share(getActivity(),"汇聚全球优质品牌",UserManager.getInstance().getUser().getNickName()+"邀请您下载喜领APP",R.mipmap.ic_launcher,shareUrl,way,null);
            }

        } else {
            ShareUtils.saveDiskShare(getActivity(), parentView, "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
