package com.xiling.ddui.bean;

public class RechareConfigBean {
    String svipLable;
    double svipPrice;
    String blackLable;
    double blackPrice;


    public String getSvipLable() {
        return svipLable;
    }

    public double getSvipPrice() {
        return svipPrice/100;
    }

    public String getBlackLable() {
        return blackLable;
    }

    public double getBlackPrice() {
        return blackPrice/100;
    }
}
