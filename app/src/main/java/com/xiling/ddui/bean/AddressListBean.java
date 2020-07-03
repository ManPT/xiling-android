package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

public class AddressListBean implements Parcelable {
    /**
     * datas : [{"addressId":"b85f99420cab434f9aadbf3e55fca9a4","memberId":"123qweasdzxc","contact":"白菜炒五花肉和","phone":"13475323377","provinceId":"370000","provinceName":"山东省","cityId":"370200","cityName":"青岛市","districtId":"370203","districtName":"市北区","detail":"卓越世纪中心1号楼2609教室开展一批优秀大学生创业计划和服务行业合作项目管理工作经验优先\u2026\u2026这里也很好吃。","isDefault":1},{"addressId":"fd0ddf95207e11ea8c6100163e0813d6","memberId":"123qweasdzxc","contact":"白菜","phone":"13475323377","provinceId":"370000","provinceName":"山东省","cityId":"370200","cityName":"青岛市","districtId":"370203","districtName":"市北区","detail":"卓越世纪中心1号楼2609","isDefault":0}]
     * pageOffset : 1
     * pageSize : 10
     * totalRecord : 7
     * totalPage : 1
     * ex : {}
     */

    private int pageOffset;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private ExBean ex;
    private List<DatasBean> datas;

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

    public ExBean getEx() {
        return ex;
    }

    public void setEx(ExBean ex) {
        this.ex = ex;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class ExBean implements Parcelable {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public ExBean() {
        }

        protected ExBean(Parcel in) {
        }

        public static final Creator<ExBean> CREATOR = new Creator<ExBean>() {
            @Override
            public ExBean createFromParcel(Parcel source) {
                return new ExBean(source);
            }

            @Override
            public ExBean[] newArray(int size) {
                return new ExBean[size];
            }
        };
    }

    public static class DatasBean implements Parcelable {
        /**
         * addressId : b85f99420cab434f9aadbf3e55fca9a4
         * memberId : 123qweasdzxc
         * contact : 白菜炒五花肉和
         * phone : 13475323377
         * provinceId : 370000
         * provinceName : 山东省
         * cityId : 370200
         * cityName : 青岛市
         * districtId : 370203
         * districtName : 市北区
         * detail : 卓越世纪中心1号楼2609教室开展一批优秀大学生创业计划和服务行业合作项目管理工作经验优先……这里也很好吃。
         * isDefault : 1
         */

        private String addressId;
        private String memberId;
        private String contact;
        private String phone;
        private String provinceId;
        private String provinceName;
        private String cityId;
        private String cityName;
        private String districtId;
        private String districtName;
        private String detail;
        private int isDefault;

        public String getFullRegion(){
            if (TextUtils.isEmpty(provinceName)){
                return "";
            }else if (TextUtils.isEmpty(cityName)){
                return provinceName;
            }else if (TextUtils.isEmpty(districtName)){
                return provinceName+" "+cityName;
            }else {
                return  provinceName+" "+cityName+" "+districtName;
            }
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getContact() {
            return contact;
        }

        public String getContactAndPhone(){
            return contact+"  "+phone;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addressId);
            dest.writeString(this.memberId);
            dest.writeString(this.contact);
            dest.writeString(this.phone);
            dest.writeString(this.provinceId);
            dest.writeString(this.provinceName);
            dest.writeString(this.cityId);
            dest.writeString(this.cityName);
            dest.writeString(this.districtId);
            dest.writeString(this.districtName);
            dest.writeString(this.detail);
            dest.writeInt(this.isDefault);
        }

        public DatasBean() {
        }

        protected DatasBean(Parcel in) {
            this.addressId = in.readString();
            this.memberId = in.readString();
            this.contact = in.readString();
            this.phone = in.readString();
            this.provinceId = in.readString();
            this.provinceName = in.readString();
            this.cityId = in.readString();
            this.cityName = in.readString();
            this.districtId = in.readString();
            this.districtName = in.readString();
            this.detail = in.readString();
            this.isDefault = in.readInt();
        }

        public static final Parcelable.Creator<DatasBean> CREATOR = new Parcelable.Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel source) {
                return new DatasBean(source);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pageOffset);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.totalRecord);
        dest.writeInt(this.totalPage);
        dest.writeParcelable(this.ex, flags);
        dest.writeTypedList(this.datas);
    }

    public AddressListBean() {
    }

    protected AddressListBean(Parcel in) {
        this.pageOffset = in.readInt();
        this.pageSize = in.readInt();
        this.totalRecord = in.readInt();
        this.totalPage = in.readInt();
        this.ex = in.readParcelable(ExBean.class.getClassLoader());
        this.datas = in.createTypedArrayList(DatasBean.CREATOR);
    }

    public static final Parcelable.Creator<AddressListBean> CREATOR = new Parcelable.Creator<AddressListBean>() {
        @Override
        public AddressListBean createFromParcel(Parcel source) {
            return new AddressListBean(source);
        }

        @Override
        public AddressListBean[] newArray(int size) {
            return new AddressListBean[size];
        }
    };
}
