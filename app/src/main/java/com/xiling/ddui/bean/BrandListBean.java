package com.xiling.ddui.bean;

import com.az.lettersort.azList.BaseSortBean;

import java.util.List;

public class BrandListBean {


    private List<String> initials;
    private List<GroupsBean> groups;

    public List<String> getInitialList() {
        return initials;
    }

    public void setInitials(List<String> initials) {
        this.initials = initials;
    }

    public List<GroupsBean> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsBean> groups) {
        this.groups = groups;
    }

    public static class GroupsBean {
        /**
         * initial : A
         * brands : [{"brandId":"4139becaf7cd44969319d96ec608dcae","initial":"A","brandName":"敖东","iconUrl":"http://oss.xilingbm.com/brand/2020-03/2020031009145297739.jpg","categoryUrl":"https://oss.xilingbm.com/brand/2020-03/20200328084145863AC.png","content":"","remark":"","createDate":"2020-03-10 17:15:08","updateDate":"2020-04-22 14:59:35","showIndex":0,"indexUrl":"https://oss.xilingbm.com/brand/2020-04/2020042206593378127.png","indexSort":0,"deleteFlag":1}]
         */

        private String initial;
        private List<BrandsBean> brands;

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public List<BrandsBean> getBrands() {
            return brands;
        }

        public void setBrands(List<BrandsBean> brands) {
            this.brands = brands;
        }

        public static class BrandsBean extends BaseSortBean {
            /**
             * brandId : 4139becaf7cd44969319d96ec608dcae
             * initial : A
             * brandName : 敖东
             * iconUrl : http://oss.xilingbm.com/brand/2020-03/2020031009145297739.jpg
             * categoryUrl : https://oss.xilingbm.com/brand/2020-03/20200328084145863AC.png
             * content :
             * remark :
             * createDate : 2020-03-10 17:15:08
             * updateDate : 2020-04-22 14:59:35
             * showIndex : 0
             * indexUrl : https://oss.xilingbm.com/brand/2020-04/2020042206593378127.png
             * indexSort : 0
             * deleteFlag : 1
             */

            private String brandId;
            private String initial;
            private String brandName;
            private String iconUrl;
            private String categoryUrl;
            private String content;
            private String remark;
            private String createDate;
            private String updateDate;
            private int showIndex;
            private String indexUrl;
            private int indexSort;
            private int deleteFlag;

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getInitial() {
                return initial;
            }

            public void setInitial(String initial) {
                this.initial = initial;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getCategoryUrl() {
                return categoryUrl;
            }

            public void setCategoryUrl(String categoryUrl) {
                this.categoryUrl = categoryUrl;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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

            public int getShowIndex() {
                return showIndex;
            }

            public void setShowIndex(int showIndex) {
                this.showIndex = showIndex;
            }

            public String getIndexUrl() {
                return indexUrl;
            }

            public void setIndexUrl(String indexUrl) {
                this.indexUrl = indexUrl;
            }

            public int getIndexSort() {
                return indexSort;
            }

            public void setIndexSort(int indexSort) {
                this.indexSort = indexSort;
            }

            public int getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(int deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            @Override
            public String getName() {
                return brandName;
            }

            @Override
            public String getInitials() {
                return initial;
            }

            @Override
            public String toString() {
                return "BrandsBean{" +
                        "initial='" + initial + '\'' +
                        ", brandName='" + brandName + '\'' +
                        '}';
            }
        }
    }
}
