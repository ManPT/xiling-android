package com.xiling.ddui.bean;

import java.util.List;

public class ClentOrderDetailBean {
    /**
     * orderId : be324e55f5244edaa5e5bd06955f1f2f
     * orderCode : 202003436911
     * orderStatus : 15
     * headImage : http://oss.xilingbm.com/app/2020-03/20200302013720971MH.jpg
     * nickName : xiling
     * receiptsIndices : 13640
     * createDate : 2020-03-02 17:28:26
     * payDate : 2020-03-02 17:28:51
     * receivedDate :
     * clientOrderDetailList : [{"productImage":"http://oss.xilingbm.com/product/2020-01/2020011307512148682.jpg","skuName":"青岛-Swisse 葡萄籽风味饮料 300毫升","properties":"300ml;","price":10700,"retailPrice":11900,"marketPrice":15600,"quantity":10}]
     */

    private String orderId;
    private String orderCode;
    private String orderStatus;
    private String headImage;
    private String nickName;
    private double receiptsIndices;
    private String createDate;

    public String getStoreName() {
        return storeName;
    }

    private String storeName;
    private String payDate;
    private String receivedDate;
    private List<DetailsBean> clientOrderDetailList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public double getReceiptsIndices() {
        return receiptsIndices/100;
    }

    public void setReceiptsIndices(double receiptsIndices) {
        this.receiptsIndices = receiptsIndices;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public List<DetailsBean> getClientOrderDetailList() {
        return clientOrderDetailList;
    }

    public void setClientOrderDetailList(List<DetailsBean> clientOrderDetailList) {
        this.clientOrderDetailList = clientOrderDetailList;
    }


}
