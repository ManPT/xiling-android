package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class XLOrderDetailsBean implements Parcelable {
    /**
     * orderId : 9678a3a97f3a4e2283835ed990d4cd6b
     * orderCode : 2331579106044692
     * orderStatus : 待支付
     * orderStatusUs : WAIT_PAY
     * details : [{"productId":"e55c5e96342948019631f5dc13f204ec","skuName":"汤臣倍健健力多牛乳钙片压片糖果 儿童补钙应首选牛奶 好的营养来自好的原料","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/2020010811134320566.jpg","productSpecification":"60片/瓶;","price":4000,"retailPrice":4300,"skuId":"861be74350144192a14b8106634f06d1","skuCode":"5464456456f4dg","quantity":1}]
     * totalQuantity : 1
     * totalPrice : 4000
     * goodsTotalRetailPrice : 4300
     * createTime : 2020-01-13 09:24:27
     * updateTime : 2020-01-13 09:24:27
     * canRemindDelivery : false
     * contactUsername : 收货
     * contactPhone : 18346578965
     * contactProvince : 北京
     * contactCity : 北京市
     * contactDistrict : 东城区
     * contactDetail : 多看看书
     * freight : 0
     * discountCoupon : 0
     * discountPrice : 300
     * taxes : 0
     * expressId : 0
     * expressCode :
     * expressName :
     * doneTime :
     * shipDate :
     * payType : 未支付
     * payMoney : 4000
     * balance : 0
     * waitPayTimeMilli : 1989391
     */

    private String orderId;
    private String orderCode;
    private String orderStatus;
    private String orderStatusUs;
    private int totalQuantity;
    private double totalPrice;
    private double goodsTotalRetailPrice;

    public double getGoodsTotalPrice() {
        return goodsTotalPrice / 100;
    }

    private double goodsTotalPrice;
    private String createTime;
    private String updateTime;
    private boolean canRemindDelivery;

    public boolean isCanRemindAudit() {
        return canRemindAudit;
    }

    public void setCanRemindAudit(boolean canRemindAudit) {
        this.canRemindAudit = canRemindAudit;
    }

    private boolean canRemindAudit;
    private String contactUsername;
    private String contactPhone;
    private String contactProvince;
    private String contactCity;
    private String contactDistrict;
    private String contactDetail;
    private double freight;
    private double discountCoupon;
    private double discountPrice;
    private int expressId;
    private String expressCode;
    private String expressName;
    private String doneTime;
    private String shipDate;
    private String payType;
    private double payMoney;
    private double balance;
    private long waitPayTimeMilli;
    private double receiptsIndices;
    private List<DetailsBean> details;
    private String storeName;
    private int isCross;
    private double taxes;

    public String getOrderer() {
        return orderer;
    }

    private String orderer;

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    private String identityCard;

    public int getIsCross() {
        return isCross;
    }

    public double getTaxes() {
        return taxes/100;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    private String buyerRemark;

    public String getAddress() {
        return contactProvince + " " + contactCity + " " + contactDistrict;
    }

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

    public String getOrderStatusUs() {
        return orderStatusUs;
    }

    public void setOrderStatusUs(String orderStatusUs) {
        this.orderStatusUs = orderStatusUs;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice / 100;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getGoodsTotalRetailPrice() {
        return goodsTotalRetailPrice / 100;
    }

    public void setGoodsTotalRetailPrice(double goodsTotalRetailPrice) {
        this.goodsTotalRetailPrice = goodsTotalRetailPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isCanRemindDelivery() {
        return canRemindDelivery;
    }

    public void setCanRemindDelivery(boolean canRemindDelivery) {
        this.canRemindDelivery = canRemindDelivery;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactProvince() {
        return contactProvince;
    }

    public void setContactProvince(String contactProvince) {
        this.contactProvince = contactProvince;
    }

    public String getContactCity() {
        return contactCity;
    }

    public void setContactCity(String contactCity) {
        this.contactCity = contactCity;
    }

    public String getContactDistrict() {
        return contactDistrict;
    }

    public void setContactDistrict(String contactDistrict) {
        this.contactDistrict = contactDistrict;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public double getFreight() {
        return freight / 100;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getDiscountCoupon() {
        return discountCoupon / 100;
    }

    public void setDiscountCoupon(double discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public double getDiscountPrice() {
        return discountPrice / 100;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getExpressId() {
        return expressId;
    }

    public void setExpressId(int expressId) {
        this.expressId = expressId;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getPayMoney() {
        return payMoney / 100;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public double getBalance() {
        return balance / 100;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getWaitPayTimeMilli() {
        return waitPayTimeMilli;
    }

    public void setWaitPayTimeMilli(long waitPayTimeMilli) {
        this.waitPayTimeMilli = waitPayTimeMilli;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public double getReceiptsIndices() {
        return receiptsIndices;
    }

    public void setReceiptsIndices(double receiptsIndices) {
        this.receiptsIndices = receiptsIndices;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.orderCode);
        dest.writeString(this.orderStatus);
        dest.writeString(this.orderStatusUs);
        dest.writeInt(this.totalQuantity);
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.goodsTotalRetailPrice);
        dest.writeDouble(this.goodsTotalPrice);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeByte(this.canRemindDelivery ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canRemindAudit ? (byte) 1 : (byte) 0);
        dest.writeString(this.contactUsername);
        dest.writeString(this.contactPhone);
        dest.writeString(this.contactProvince);
        dest.writeString(this.contactCity);
        dest.writeString(this.contactDistrict);
        dest.writeString(this.contactDetail);
        dest.writeDouble(this.freight);
        dest.writeDouble(this.discountCoupon);
        dest.writeDouble(this.discountPrice);
        dest.writeInt(this.expressId);
        dest.writeString(this.expressCode);
        dest.writeString(this.expressName);
        dest.writeString(this.doneTime);
        dest.writeString(this.shipDate);
        dest.writeString(this.payType);
        dest.writeDouble(this.payMoney);
        dest.writeDouble(this.balance);
        dest.writeLong(this.waitPayTimeMilli);
        dest.writeDouble(this.receiptsIndices);
        dest.writeTypedList(this.details);
        dest.writeString(this.storeName);
        dest.writeInt(this.isCross);
        dest.writeDouble(this.taxes);
        dest.writeString(this.orderer);
        dest.writeString(this.identityCard);
        dest.writeString(this.buyerRemark);
    }

    public XLOrderDetailsBean() {
    }

    protected XLOrderDetailsBean(Parcel in) {
        this.orderId = in.readString();
        this.orderCode = in.readString();
        this.orderStatus = in.readString();
        this.orderStatusUs = in.readString();
        this.totalQuantity = in.readInt();
        this.totalPrice = in.readDouble();
        this.goodsTotalRetailPrice = in.readDouble();
        this.goodsTotalPrice = in.readDouble();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.canRemindDelivery = in.readByte() != 0;
        this.canRemindAudit = in.readByte() != 0;
        this.contactUsername = in.readString();
        this.contactPhone = in.readString();
        this.contactProvince = in.readString();
        this.contactCity = in.readString();
        this.contactDistrict = in.readString();
        this.contactDetail = in.readString();
        this.freight = in.readDouble();
        this.discountCoupon = in.readDouble();
        this.discountPrice = in.readDouble();
        this.expressId = in.readInt();
        this.expressCode = in.readString();
        this.expressName = in.readString();
        this.doneTime = in.readString();
        this.shipDate = in.readString();
        this.payType = in.readString();
        this.payMoney = in.readDouble();
        this.balance = in.readDouble();
        this.waitPayTimeMilli = in.readLong();
        this.receiptsIndices = in.readDouble();
        this.details = in.createTypedArrayList(DetailsBean.CREATOR);
        this.storeName = in.readString();
        this.isCross = in.readInt();
        this.taxes = in.readDouble();
        this.orderer = in.readString();
        this.identityCard = in.readString();
        this.buyerRemark = in.readString();
    }

    public static final Creator<XLOrderDetailsBean> CREATOR = new Creator<XLOrderDetailsBean>() {
        @Override
        public XLOrderDetailsBean createFromParcel(Parcel source) {
            return new XLOrderDetailsBean(source);
        }

        @Override
        public XLOrderDetailsBean[] newArray(int size) {
            return new XLOrderDetailsBean[size];
        }
    };
}
