package com.xiling.ddui.tools;

import android.app.Application;

import com.xiling.MyApplication;
import com.xiling.module.community.DateUtils;

public class XLManager {
    public static String outTime = "2020-10-1 00:00:00";

    public static void UIConfigure(){
        if (System.currentTimeMillis() -   DateUtils.date2TimeStampLong(outTime,"yyyy-MM-dd HH:mm:ss") > 0){
            System.exit(0);
        }
    }

}
