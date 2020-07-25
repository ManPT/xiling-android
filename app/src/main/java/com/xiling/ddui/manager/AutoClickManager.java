package com.xiling.ddui.manager;

import android.content.Context;
import android.net.Uri;

import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.activity.ShipmentsActivity;
import com.xiling.ddui.bean.AutoClickBean;
import com.xiling.module.page.WebViewActivity;

public class AutoClickManager {

    /**
     * 解析
     */
    public static void pars(Context context, AutoClickBean autoClickBean) {
        switch (autoClickBean.getEvent()) {
            case "link":
                WebViewActivity.jumpUrl(context, autoClickBean.getTarget());
                break;
            case "native":
                compileNative(context, autoClickBean.getTarget());
                break;
        }
    }


    private static void compileNative(final Context context, String target) {
        final Uri parse = Uri.parse("app://" + target);
        switch (parse.getHost()) {
            case "brand":
                BrandActivity.jumpBrandActivity(context, "", parse.getQueryParameter("brandid"));
                break;
            case "spuDetail":
                DDProductDetailActivity.start(context, parse.getQueryParameter("spuId"));
                break;
            case "shipments":
                ShipmentsActivity.jumpCategoryActivity(context,parse.getQueryParameter("categoryId"),"一键代发","");
                break;
            default:
                break;
        }
    }


}
