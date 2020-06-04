package com.xiling.ddui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.camera.DDIDCardActivity;
import com.xiling.ddui.custom.popupwindow.PhotoSelectDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.PhotoTools;
import com.xiling.ddui.tools.ViewUtil;
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
 * 账户认证-上传店铺照片
 */
public class StorePhotoUploadActivity extends BaseActivity {
    private static final int REQUEST_CODE_ID_CARD_FRONT = 1;
    private static final int REQUEST_CODE_ID_CARD_BEHIND = 2;

    private int mType = UploadManager.IDENTITY_CARD_FRONT;

    @BindView(R.id.iv_just)
    SimpleDraweeView ivJust;
    @BindView(R.id.iv_just_default)
    ImageView ivJustDefault;
    @BindView(R.id.iv_back)
    SimpleDraweeView ivBack;
    @BindView(R.id.iv_back_default)
    ImageView ivBackDefault;
    PhotoSelectDialog photoSelectDialog;
    private String[] mImgURL = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_photo_upload);
        ButterKnife.bind(this);
        setTitle("上传店铺照片");
        setLeftBlack();

        if (getIntent() != null) {
            String idcardFrontImg = getIntent().getStringExtra("idcardFrontImg");
            String idcardBackImg = getIntent().getStringExtra("idcardBackImg");
            if (!TextUtils.isEmpty(idcardFrontImg)) {
                mImgURL[0] = idcardFrontImg;

                ivJustDefault.setVisibility(View.GONE);
                ivJust.setImageURI(Uri.parse(idcardFrontImg));
            }

            if (!TextUtils.isEmpty(idcardBackImg)) {
                mImgURL[1] = idcardBackImg;
                ivBackDefault.setVisibility(View.GONE);
                ivBack.setImageURI(Uri.parse(idcardBackImg));
            }

        }

    }

    @OnClick({R.id.btn_upload_just, R.id.btn_upload_back, R.id.btn_next})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_upload_just:
                if (photoSelectDialog == null) {
                    photoSelectDialog = new PhotoSelectDialog(context);
                }
                photoSelectDialog.show();
                photoSelectDialog.setOnPhotoSelectListener(new PhotoSelectDialog.OnPhotoSelectListener() {
                    @Override
                    public void onPhoto() {
                        mType = UploadManager.IDENTITY_CARD_FRONT;
                        //  selectImage(REQUEST_CODE_ID_CARD_FRONT);
                        PhotoTools.photogaph(StorePhotoUploadActivity.this, 10000);
                    }

                    @Override
                    public void onAlbum() {
                        mType = UploadManager.IDENTITY_CARD_FRONT;
                        PhotoTools.openAlbum(StorePhotoUploadActivity.this, 10010);
                    }
                });
                break;
            case R.id.btn_upload_back:
                if (photoSelectDialog == null) {
                    photoSelectDialog = new PhotoSelectDialog(context);
                }
                photoSelectDialog.show();
                photoSelectDialog.setOnPhotoSelectListener(new PhotoSelectDialog.OnPhotoSelectListener() {
                    @Override
                    public void onPhoto() {
                        mType = UploadManager.IDENTITY_CARD_BEHIND;
                        //selectImage(REQUEST_CODE_ID_CARD_BEHIND);
                        PhotoTools.photogaph(StorePhotoUploadActivity.this, 10000);
                    }

                    @Override
                    public void onAlbum() {
                        mType = UploadManager.IDENTITY_CARD_BEHIND;
                        PhotoTools.openAlbum(StorePhotoUploadActivity.this, 10010);
                    }
                });

                break;
            case R.id.btn_next:
                if (checkNotNullImages()) {
                    Intent intent = new Intent();
                    intent.putExtra("idcardFrontImg", mImgURL[0]);
                    intent.putExtra("idcardBackImg", mImgURL[1]);
                    setResult(10001, intent);
                }
                finish();
                break;
        }
    }

    private void updateImage(final int request, final String uri) {
        ToastUtil.showLoading(context);
        UploadManager.uploadIdCard(this, uri, mType, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case REQUEST_CODE_ID_CARD_FRONT:
                        ivJustDefault.setVisibility(View.GONE);
                        ivJust.setImageURI(result.url);
                        mImgURL[0] = result.url;
                        break;
                    case REQUEST_CODE_ID_CARD_BEHIND:
                        ivBackDefault.setVisibility(View.GONE);
                        ivBack.setImageURI(Uri.parse(result.url));
                        mImgURL[1] = result.url;
                        break;
                }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10010 || requestCode == 10000) {
                if (data != null) {
                    ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                    String imagePath = resultPhotos.get(0).path;
                    switch (mType) {
                        case UploadManager.IDENTITY_CARD_FRONT:
                            updateImage(REQUEST_CODE_ID_CARD_FRONT, imagePath);
                            break;
                        case UploadManager.IDENTITY_CARD_BEHIND:
                            updateImage(REQUEST_CODE_ID_CARD_BEHIND, imagePath);
                            break;
                    }
                }
            }
        }
    }

    private boolean checkNotNullImages() {
        return !TextUtils.isEmpty(mImgURL[0]) && !TextUtils.isEmpty(mImgURL[1]);
    }



}
