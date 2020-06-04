package com.xiling.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;

import java.util.concurrent.ExecutionException;

import cn.bumptech.xnglide.request.RequestOptions;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GlideUtils {


    public static void loadHead(Context context, ImageView imageView, String url) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform();
        Glide.with(context)
                .load(url)
                //此处深坑，glide bug 不加此属性，app第一次进入，图片加载不出来
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.bg_image_def)
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.bg_image_def)
                .fitCenter()
                .into(imageView);
    }


    public static void loadImageALL(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.bg_image_def)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadImageALL(Context context, ImageView imageView, String url,final RequestListener requestListener) {
        Glide.with(context).load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.bg_image_def)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        requestListener.onException(e,model,target,isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        requestListener.onResourceReady(resource,model,target,isFromMemoryCache,isFirstResource);
                        return false;
                    }
                })
                .into(imageView);
    }



    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(R.drawable.bg_image_def)
                .into(imageView);
    }


    public static void getBitmap(final Context context, final String url, final OnBitmapGet bitmapGet) {

        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {

                new AsyncTask<Void, String, Bitmap>() {

                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Bitmap myBitmap = null;
                        try {
                            myBitmap = Glide.with(context)
                                    .load(url)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return myBitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap aVoid) {
                        super.onPostExecute(aVoid);
                        bitmapGet.getBitmap(aVoid);
                    }
                }.execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnBitmapGet {
        void getBitmap(Bitmap bitmap);
    }


    /**
     * 清除缓存
     */
    public static void clearImageCache(final Context context) {
        clearImageMemoryCache(context);
        clearImageDiskCache(context);
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadImage(Context context, ImageView imageView, int url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.bg_image_def)
                .into(imageView);
    }

    public static void loadImageNoDisk(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.bg_image_def)
                .into(imageView);
    }


}
