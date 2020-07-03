package com.xiling.shared.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.ddui.tools.DLog;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.shared.Constants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.ArrayList;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/8.
 */
public class ShareUtilsNew {

    /**
     * 分享文本
     */
    public static void shareText(Activity activity, String text, SHARE_MEDIA share_media, UMShareListener listener) {
        new ShareAction(activity).setPlatform(share_media)
                .withText(text)
                .setCallback(listener)
                .share();
    }

    public static void share(Activity activity, String title, String linke) {
        share(activity, title, Constants.share_tile_text, "http://wechat.beautysecret.cn/assets/logo.png", linke, SHARE_MEDIA.WEIXIN);
    }

    public static void share(Activity activity, String title, String linke, String content, SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            ToastUtil.hideLoading();
            shareLink(activity, linke, title, "", content);
        } else {
            share(activity, title, Constants.share_tile_text, "http://wechat.beautysecret.cn/assets/logo.png", linke, share_media);
        }
        clipData(activity, content);
    }


    public static void share(Activity activity, String title, String desc, String logoUrl, String linke, SHARE_MEDIA share_media) {
        UMWeb web = new UMWeb(linke);
        web.setTitle(title);
        web.setDescription(desc);
        if (TextUtils.isEmpty(logoUrl)) {
            web.setThumb(new UMImage(activity, R.mipmap.ic_launcher));
        } else {
            web.setThumb(new UMImage(activity, logoUrl));
        }
        new ShareAction(activity).setPlatform(share_media).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                DLog.e("err", "start");
                ToastUtil.hideLoading();
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                DLog.e("err", "result");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                DLog.e("err", "err" + throwable.getMessage());
                ToastUtil.error(throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).withMedia(web).share();
    }

    public static void share(Activity activity, String title, String desc, int logoId, String linke, SHARE_MEDIA share_media) {
        UMWeb web = new UMWeb(linke);
        web.setTitle(title);
        web.setDescription(desc);
        if (logoId > 0) {
            web.setThumb(new UMImage(activity, logoId));
        } else {
            web.setThumb(new UMImage(activity, R.mipmap.ic_launcher));
        }
        new ShareAction(activity).setPlatform(share_media).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                DLog.e("err", "start");
                ToastUtil.hideLoading();
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                DLog.e("err", "result");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                DLog.e("err", "err" + throwable.getMessage());
                ToastUtil.error(throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).withMedia(web).share();
    }

    public static void shareMonenLink(Activity activity, String title, String content, String logoUrl, String linke) {
        String text = com.blankj.utilcode.utils.StringUtils.isEmpty(content) ? Constants.share_tile_text : content;
        share(activity, title, text, logoUrl, linke, SHARE_MEDIA.WEIXIN_CIRCLE);
    }


    public static void shareMultiplePictureToTimeLine(Context context, String content, ArrayList<File> files) {

        if (files == null) {
            DLog.i("文件数组为null");
            return;
        }

        if (files.size() == 0) {
            DLog.i("文件数组长度为0");
            return;
        }

        //微信封禁发多图到朋友圈，所以此处统一改成发第一张图
        boolean isSendFirst = true;
        if (files.size() == 1 || isSendFirst) {
            sharePicSingle(false, context, files.get(0));
            clipData(context, content);
            return;
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*;text/plain");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File file : files) {
            imageUris.add(getFileUri(context, file));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription", content);
        context.startActivity(intent);

        clipData(context, content);
    }

    private static Uri getFileUri(Context context, File file) {
        Uri uri;
  /*      if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {*/
        uri = Uri.fromFile(file);
        //        }
        return uri;
    }

    public static void shareMultiplePictureToQQ(Context context, ArrayList<File> files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(intent);
    }

    public static void shareMultiplePictureToUi(Context context, String content, ArrayList<File> files) {
        if (files != null && files.size() == 1) {
            clipData(context, content);
            sharePicSingle(true, context, files.get(0));
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File file : files) {
            imageUris.add(getFileUri(context, file));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(intent);

        //复制到粘贴板
        clipData(context, content);
    }

    private static void sharePicSingle(boolean isFriend, Context context, File file) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }


    public static void clipData(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        if (content != null) {
            ToastUtils.showShortToast("分享内容已在粘贴板，请粘贴");
        }
    }

    public static String getClipMessage(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setText("");
            } catch (Exception e) {

            }
        }
    }



    public static void shareVideo(Context context, String videourl, String title, String imgUrl, String content, SHARE_MEDIA share_media) {
        shareVideo(
                context,
                videourl,
                title,
                imgUrl,
                content,
                share_media,
                new UMShareListener() {

                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        ToastUtil.hideLoading();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Log.e("err", "err" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }
        );
    }

    public static void shareVideo(Context context, String videourl, String title, String imgUrl, String content, SHARE_MEDIA share_media, UMShareListener umShareListener) {
        //        UMVideo video = new UMVideo(videourl);
        UMVideo video = new UMVideo(videourl);
        if (TextUtils.isEmpty(title)) {
            title = content;
        }
        //视频的标题
        video.setTitle(title);
        //视频的缩略图
        video.setThumb(new UMImage(context, imgUrl));
        //视频的描述
        video.setDescription(content);
        new ShareAction((Activity) context).setPlatform(share_media).withMedia(video).setCallback(umShareListener).share();

        //复制到粘贴板
        clipData(context, content);
    }


    public static void shareLink(Context context, String shareUrl, String title, String image, String desc) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID);
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = desc;
        clipData(context, desc);
        //图片加载是使用的ImageLoader.loadImageSync() 同步方法
        //并且还要创建图片的缩略图，因为微信限制了图片的大小
        Bitmap thumbBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumbBmp);
        thumbBmp.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        //好友
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口发送数据到微信
        boolean result = api.sendReq(req);
    }
}
