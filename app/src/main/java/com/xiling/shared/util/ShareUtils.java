package com.xiling.shared.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddui.manager.PosterMaker;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.dduis.dialog.DDSaveImageResponseDialog;
import com.xiling.shared.component.dialog.DDMProductQrCodeDialog;
import com.xiling.shared.component.dialog.DDMShareDialog;
import com.xiling.shared.component.dialog.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.just.library.AgentWebUtils.dismiss;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/8.
 */
public class ShareUtils {

    public static void showDDMShareDialog(final Activity activity, DDMShareDialog.Share shareObject) {
        if (StringUtils.isEmpty(shareObject.getDesc())) {
            shareObject.setDesc("    ");
        }
        DDMShareDialog dialog = new DDMShareDialog(activity, shareObject, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.e("onStart   ");
                ToastUtil.showLoading(activity);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                LogUtils.e("onResult   ");
                ToastUtil.hideLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                LogUtils.e("onError   ");
                ToastUtil.hideLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                LogUtils.e("onCancel   ");
                ToastUtil.hideLoading();
            }

        });
        dialog.show();
    }

    public static void showShareDialog(final Activity activity, String title, String desc, String logoUrl, String linke) {
        if (StringUtils.isEmpty(desc)) {
            desc = "     ";
        }
        new ShareDialog(activity, title, desc, logoUrl, linke, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.e("onStart   ");

                ToastUtil.showLoading(activity);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                LogUtils.e("onResult   ");

                ToastUtil.hideLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                LogUtils.e("onError   ");

                ToastUtil.hideLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                LogUtils.e("onCancel   ");

                ToastUtil.hideLoading();
            }

        }).show();
    }

    public static void share(Activity activity, String title, String desc, String logoUrl, String link) {
        share(activity, title, desc, logoUrl, link, null);
    }

    public static void share(Activity activity, String title, String desc, String logoUrl, String link, final UMShareListener listener) {
        UMWeb web = new UMWeb(link);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(new UMImage(activity, logoUrl));
//        showLoading();
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("err", "start");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e("err", "result");
//                hideLoading();
                if (listener != null) {
                    listener.onResult(share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e("err", "err" + throwable.getMessage());
//                hideLoading();
                if (listener != null) {
                    listener.onError(share_media, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
//                hideLoading();
                if (listener != null) {
                    listener.onCancel(share_media);
                }
            }
        }).withMedia(web).share();
    }

    public static void share(Activity activity, String title, String desc, String logoUrl, String link,SHARE_MEDIA way, final UMShareListener listener) {
        UMWeb web = new UMWeb(link);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(new UMImage(activity, logoUrl));
//        showLoading();
        new ShareAction(activity).setPlatform(way).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("err", "start");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e("err", "result");
//                hideLoading();
                if (listener != null) {
                    listener.onResult(share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e("err", "err" + throwable.getMessage());
//                hideLoading();
                if (listener != null) {
                    listener.onError(share_media, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
//                hideLoading();
                if (listener != null) {
                    listener.onCancel(share_media);
                }
            }
        }).withMedia(web).share();
    }

    public static void share(Activity activity, String title, String desc, int logoUrl, String link,SHARE_MEDIA way, final UMShareListener listener) {
        UMWeb web = new UMWeb(link);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(new UMImage(activity, logoUrl));
//        showLoading();
        new ShareAction(activity).setPlatform(way).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("err", "start");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e("err", "result");
//                hideLoading();
                if (listener != null) {
                    listener.onResult(share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e("err", "err" + throwable.getMessage());
//                hideLoading();
                if (listener != null) {
                    listener.onError(share_media, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
//                hideLoading();
                if (listener != null) {
                    listener.onCancel(share_media);
                }
            }
        }).withMedia(web).share();
    }

    /**
     * 分享图片到指定平台
     */
    public static void shareImg(Activity activity, File file, SHARE_MEDIA media, UMShareListener umShareListener) {
        UMImage image = new UMImage(activity, file);

        //添加压缩方式
//        image.compressStyle = UMImage.CompressStyle.QUALITY;

        //增加缩略图
        Bitmap bitmap = ImageTools.getSmallBitmap(file.getPath());
        UMImage thumb = new UMImage(activity, bitmap);
        image.setThumb(thumb);

        new ShareAction(activity).setPlatform(media).setCallback(umShareListener).withMedia(image).share();
    }

    /**
     * 分享网络图片到指定平台
     *
     * @param activity        界面
     * @param imgUrl          图片地址
     * @param media           分享渠道
     * @param umShareListener 回调事件
     */
    public static void shareWebImg(Activity activity, String imgUrl, SHARE_MEDIA media, UMShareListener umShareListener) {
        UMImage image = new UMImage(activity, imgUrl);
//        UMImage thumb = new UMImage(activity, imgUrl);
//        image.setThumb(thumb);
        image.setThumb(image);
        new ShareAction(activity).setPlatform(media).setCallback(umShareListener).withMedia(image).share();
    }

    /**
     * 截屏保存到本地后分享
     */
    public static void saveDiskShare(final Activity context, View relScreen, final String productId) {
        ToastUtil.showLoading(context);
        makeScreenShot(context, relScreen, new DDMProductQrCodeDialog.ScreenShotListener() {
            @Override
            public void onScreenShotEnd(Bitmap bitmap) {
                String name = "" + System.currentTimeMillis();
                if (!TextUtils.isEmpty(productId)) {
                    name = productId;
                }

                ImageTools.saveBitmapToAlbum(context, bitmap, "" + name+".png");

                String pathToFile = PosterMaker.getPosterFile("" + name);
                DLog.i("saveBitmapToSD：" + pathToFile);
                ImageTools.saveBitmapToSD(bitmap, pathToFile, 80, false);

                ToastUtil.hideLoading();
                ToastUtil.success("保存成功");
            }
        });
    }

    /**
     * 执行截屏
     */
    public static void makeScreenShot(Context context, View relScreen, DDMProductQrCodeDialog.ScreenShotListener listener) {
        DLog.i("makeScreenShot");
        Bitmap bitmap = loadBitmapFromView(relScreen);
        if (listener != null) {
            if (bitmap != null) {
                listener.onScreenShotEnd(bitmap);
            } else {
                ToastUtil.error("生成文件失败");
//                listener.onScreenShotError();
            }
        }
    }


    public static Bitmap loadBitmapFromView(View view) {
        if (view== null) {
            return null;
        }
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.TRANSPARENT);
        view.draw(c);
        return bitmap;
    }

    public static void shareTo3rdPlatform(final Activity activity, View relScreen, final SHARE_MEDIA way, final String productId) {
        DLog.i("shareTo3rdPlatform：" + way);
        makeScreenShot(activity, relScreen, new DDMProductQrCodeDialog.ScreenShotListener() {
            @Override
            public void onScreenShotEnd(Bitmap bitmap) {
                String name = "" + System.currentTimeMillis();
                if (!TextUtils.isEmpty(productId)) {
                    name = productId;
                }

                String pathToFile = PosterMaker.getPosterFile("" + name);
                File file = new File(pathToFile);

                DLog.i("saveBitmapToSD：" + pathToFile);
                ImageTools.saveBitmapToSD(bitmap, pathToFile, 80, false);

                ToastUtil.hideLoading();
                ShareUtils.shareImg(activity, file, way, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        DLog.d("share image onStart：" + share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        DLog.d("share image onResult：" + share_media);

                        if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            ToastUtil.success("分享成功");
                        }

                        dismiss();

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        DLog.d("share image onError：" + share_media);
                        ToastUtil.error("分享失败");

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        DLog.d("share image onCancel：" + share_media);
                        ToastUtil.error("分享已取消");
                    }
                });
            }
        });
    }

    /**
     * 生成商品二维码图片
     */
    @SuppressLint("StaticFieldLeak")
    public static Bitmap createQRImage(final Context context, final String url, final OnQRImageListener onQRImageListener) {
        ToastUtil.showLoading(context);
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(context, 100));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ToastUtil.hideLoading();
                if (onQRImageListener != null) {
                    onQRImageListener.onCreatQR(bitmap);
                }
            }
        }.execute();
        return null;
    }

    public interface OnQRImageListener {
        void onCreatQR(Bitmap bitmap);
    }


}
