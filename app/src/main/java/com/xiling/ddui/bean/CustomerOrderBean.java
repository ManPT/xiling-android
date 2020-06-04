package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CustomerOrderBean {

    int pageOffset;
    int pageSize;
    int totalRecord;
    int totalPage;
    private List<OrderDetailsBean> datas;

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<OrderDetailsBean> getRecords() {
        return datas;
    }

    public void setRecords(List<OrderDetailsBean> records) {
        this.datas = records;
    }

    public static class OrderDetailsBean implements Parcelable {
        String orderId;
        String orderCode;
        String orderStatus;
        String headImage;
        String nickName;
        String createDate;
        String payDate;
        String receivedDate;

        public int getTotalQuantoity() {
            return totalQuantoity;
        }

        int totalQuantoity;

        public double getFreight() {
            return freight/100;
        }

        public double getTotalTaxes() {
            return totalTaxes/100;
        }

        double freight;
        double totalTaxes;
        double receiptsIndices;
        List<DetailsBean> clientOrderDetailList;

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

        public double getReceiptsIndices() {
            return receiptsIndices/100;
        }

        public void setReceiptsIndices(double receiptsIndices) {
            this.receiptsIndices = receiptsIndices;
        }

        public List<DetailsBean> getClientOrderDetailList() {
            return clientOrderDetailList;
        }

        public void setClientOrderDetailList(List<DetailsBean> clientOrderDetailList) {
            this.clientOrderDetailList = clientOrderDetailList;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderId);
            dest.writeString(this.orderCode);
            dest.writeString(this.orderStatus);
            dest.writeString(this.headImage);
            dest.writeString(this.nickName);
            dest.writeString(this.createDate);
            dest.writeString(this.payDate);
            dest.writeString(this.receivedDate);
            dest.writeDouble(this.receiptsIndices);
            dest.writeTypedList(this.clientOrderDetailList);
            dest.writeDouble(this.freight);
            dest.writeDouble(this.totalTaxes);
            dest.writeInt(this.totalQuantoity);
        }

        public OrderDetailsBean() {
        }

        protected OrderDetailsBean(Parcel in) {
            this.orderId = in.readString();
            this.orderCode = in.readString();
            this.orderStatus = in.readString();
            this.headImage = in.readString();
            this.nickName = in.readString();
            this.createDate = in.readString();
            this.payDate = in.readString();
            this.receivedDate = in.readString();
            this.receiptsIndices = in.readDouble();
            this.clientOrderDetailList = in.createTypedArrayList(DetailsBean.CREATOR);
            this.freight = in.readDouble();
            this.totalTaxes = in.readDouble();
            this.totalQuantoity = in.readInt();
        }

        public static final Parcelable.Creator<OrderDetailsBean> CREATOR = new Parcelable.Creator<OrderDetailsBean>() {
            @Override
            public OrderDetailsBean createFromParcel(Parcel source) {
                return new OrderDetailsBean(source);
            }

            @Override
            public OrderDetailsBean[] newArray(int size) {
                return new OrderDetailsBean[size];
            }
        };
    }
}
