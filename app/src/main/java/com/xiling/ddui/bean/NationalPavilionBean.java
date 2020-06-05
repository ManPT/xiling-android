package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NationalPavilionBean implements Parcelable {
    /**
     * countryId : cc3e477550b743efadda77384e2dfa12
     * countryName : 中国馆
     * countryIcon : http://oss.xilingbm.com/brand/2020-02/20200227091653393Y2.png
     * countryExplain :
     * countryBanner : http://oss.xilingbm.com/brand/2020-02/20200227091639978AU.png
     * countryListBanner : http://oss.xilingbm.com/brand/2020-02/20200227091639978AU.png
     * createDate : 2020-01-08 17:18:46
     * updateDate : 2020-01-08 17:18:46
     * deleteFlag : 1
     */

    private String countryId;
    private String countryName;
    private String countryIcon;
    private String countryExplain;
    private String countryBanner;
    private String countryListBanner;
    private String createDate;
    private String updateDate;
    private int deleteFlag;

    public String getCountryDetailBanner() {
        return countryDetailBanner;
    }

    public void setCountryDetailBanner(String countryDetailBanner) {
        this.countryDetailBanner = countryDetailBanner;
    }

    private String countryDetailBanner;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIcon() {
        return countryIcon;
    }

    public void setCountryIcon(String countryIcon) {
        this.countryIcon = countryIcon;
    }

    public String getCountryExplain() {
        return countryExplain;
    }

    public void setCountryExplain(String countryExplain) {
        this.countryExplain = countryExplain;
    }

    public String getCountryBanner() {
        return countryBanner;
    }

    public void setCountryBanner(String countryBanner) {
        this.countryBanner = countryBanner;
    }

    public String getCountryListBanner() {
        return countryListBanner;
    }

    public void setCountryListBanner(String countryListBanner) {
        this.countryListBanner = countryListBanner;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryId);
        dest.writeString(this.countryName);
        dest.writeString(this.countryIcon);
        dest.writeString(this.countryExplain);
        dest.writeString(this.countryBanner);
        dest.writeString(this.countryListBanner);
        dest.writeString(this.createDate);
        dest.writeString(this.updateDate);
        dest.writeInt(this.deleteFlag);
        dest.writeString(this.countryDetailBanner);
    }

    public NationalPavilionBean() {
    }

    protected NationalPavilionBean(Parcel in) {
        this.countryId = in.readString();
        this.countryName = in.readString();
        this.countryIcon = in.readString();
        this.countryExplain = in.readString();
        this.countryBanner = in.readString();
        this.countryListBanner = in.readString();
        this.createDate = in.readString();
        this.updateDate = in.readString();
        this.deleteFlag = in.readInt();
        this.countryDetailBanner = in.readString();
    }

    public static final Parcelable.Creator<NationalPavilionBean> CREATOR = new Parcelable.Creator<NationalPavilionBean>() {
        @Override
        public NationalPavilionBean createFromParcel(Parcel source) {
            return new NationalPavilionBean(source);
        }

        @Override
        public NationalPavilionBean[] newArray(int size) {
            return new NationalPavilionBean[size];
        }
    };
}
