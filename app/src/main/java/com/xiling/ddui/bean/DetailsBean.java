package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public  class DetailsBean implements Parcelable {
    /**
     * productId : e55c5e96342948019631f5dc13f204ec
     * skuName : 汤臣倍健健力多牛乳钙片压片糖果 儿童补钙应首选牛奶 好的营养来自好的原料
     * productImage : http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/2020010811134320566.jpg
     * productSpecification : 60片/瓶;
     * price : 4000
     * retailPrice : 4300
     * skuId : 861be74350144192a14b8106634f06d1
     * skuCode : 5464456456f4dg
     * quantity : 1
     */

    private String productId;
    private String skuName;
    private String productImage;
    private String productSpecification;

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    private String properties;
    private double price;
    private double retailPrice;
    private String skuId;
    private String skuCode;
    private int quantity;

    private double taxes;
    boolean cross;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public double getPrice() {
        return price/100;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRetailPrice() {
        return retailPrice/100;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTaxes() {
        return taxes/100;
    }

    public boolean isCross() {
        return cross;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.skuName);
        dest.writeString(this.productImage);
        dest.writeString(this.productSpecification);
        dest.writeDouble(this.price);
        dest.writeDouble(this.retailPrice);
        dest.writeString(this.skuId);
        dest.writeString(this.skuCode);
        dest.writeInt(this.quantity);
        dest.writeDouble(this.taxes);
        dest.writeString(this.properties);
        dest.writeByte(this.cross ? (byte) 1 : (byte) 0);
    }

    public DetailsBean() {
    }

    protected DetailsBean(Parcel in) {
        this.productId = in.readString();
        this.skuName = in.readString();
        this.productImage = in.readString();
        this.productSpecification = in.readString();
        this.properties = in.readString();
        this.price = in.readDouble();
        this.retailPrice = in.readDouble();
        this.skuId = in.readString();
        this.skuCode = in.readString();
        this.quantity = in.readInt();
        this.taxes = in.readDouble();
        this.cross = in.readByte() != 0;
    }

    public static final Creator<DetailsBean> CREATOR = new Creator<DetailsBean>() {
        @Override
        public DetailsBean createFromParcel(Parcel source) {
            return new DetailsBean(source);
        }

        @Override
        public DetailsBean[] newArray(int size) {
            return new DetailsBean[size];
        }
    };
}
