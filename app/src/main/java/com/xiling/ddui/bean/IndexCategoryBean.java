package com.xiling.ddui.bean;

import java.util.List;

/**
 * 首页分类
 */
public class IndexCategoryBean {
    /**
     * categoryId : cb1bd38e380c4cd9a233c6a8d701f35f
     * initial :
     * categoryName : 测试首页分类2
     * iconUrl : https://oss.xilingbm.com/brand/2020-05/20200522083323076W2.png
     * categoryUrl : https://oss.xilingbm.com/brand/2020-05/20200522083325617DN.png
     * content : <p>测试首页分类2</p>
     * remark :
     * createDate :
     * updateDate :
     * showIndex :
     * indexUrl : https://oss.xilingbm.com/brand/2020-05/20200522083327601A4.png
     * indexSort :
     * deleteFlag :
     */

    private String categoryId;
    private String initial;
    private String categoryName;
    private String iconUrl;
    private String categoryUrl;
    private String content;
    private String remark;
    private String createDate;
    private String updateDate;
    private String showIndex;
    private String indexUrl;
    private String indexSort;
    private String deleteFlag;

    public List<IndexBrandBean.IndexBrandBeanListBean> getIndexCategoryBeanList() {
        return indexCategoryBeanList;
    }

    public void setIndexCategoryBeanList(List<IndexBrandBean.IndexBrandBeanListBean> indexCategoryBeanList) {
        this.indexCategoryBeanList = indexCategoryBeanList;
    }

    private List<IndexBrandBean.IndexBrandBeanListBean> indexCategoryBeanList;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(String showIndex) {
        this.showIndex = showIndex;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getIndexSort() {
        return indexSort;
    }

    public void setIndexSort(String indexSort) {
        this.indexSort = indexSort;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
