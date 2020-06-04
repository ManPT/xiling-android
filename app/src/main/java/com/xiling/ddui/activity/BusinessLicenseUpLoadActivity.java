package com.xiling.ddui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.PhotoSelectDialog;
import com.xiling.ddui.tools.PhotoTools;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 账户认证-上传营业执照
 */
public class BusinessLicenseUpLoadActivity extends BaseActivity {

    @BindView(R.id.iv_just)
    SimpleDraweeView ivJust;
    PhotoSelectDialog photoSelectDialog;
    @BindView(R.id.iv_just_default)
    ImageView ivJustDefault;

    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_license);
        ButterKnife.bind(this);
        setTitle("上传营业执照");
        setLeftBlack();

        if (getIntent() != null) {
            String businessLicense = getIntent().getStringExtra("businessLicense");
            if (!TextUtils.isEmpty(businessLicense)) {
                imgUrl = businessLicense;
                ivJustDefault.setVisibility(View.GONE);
                ivJust.setImageURI(Uri.parse(businessLicense));
            }

        }
    }

    @OnClick({R.id.btn_next, R.id.btn_upload_just})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_upload_just:
                if (photoSelectDialog == null) {
                    photoSelectDialog = new PhotoSelectDialog(context);
                }
                photoSelectDialog.show();
                photoSelectDialog.setOnPhotoSelectListener(new PhotoSelectDialog.OnPhotoSelectListener() {
                    @Override
                    public void onPhoto() {
                        PhotoTools.photogaph(BusinessLicenseUpLoadActivity.this, 10000);
                    }

                    @Override
                    public void onAlbum() {
                        PhotoTools.openAlbum(BusinessLicenseUpLoadActivity.this, 10010);
                    }
                });
                break;
            case R.id.btn_next:
                if (!TextUtils.isEmpty(imgUrl)) {
                    Intent intent = new Intent();
                    intent.putExtra("businessLicense", imgUrl);
                    setResult(10002, intent);
                }
                finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10010 || requestCode == 10000) {
                if (data != null) {
                    ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                    String imagePath = resultPhotos.get(0).path;
                    updateImage(imagePath);
                }
            }
        }
    }

    private void updateImage(final String uri) {
        ToastUtil.showLoading(context);
        UploadManager.uploadIdCard(this, uri, 1, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                ivJustDefault.setVisibility(View.GONE);
                ivJust.setImageURI(result.url);
                imgUrl = result.url;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
                ToastUtil.error("图片上传失败");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                ToastUtil.hideLoading();
            }
        });
    }

}
