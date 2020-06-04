package com.xiling.ddui.bean;

import java.util.List;

public class ProductNewBean {
    /**
     * productId : 4a129f6891204fd88676e8197326e8c2
     * badgeImg :
     * thumbUrl : https://oss.dodomall.com/product/2019-11/201911190944156477V.jpg
     * productName : 袋鼠妈妈·星河化妆套刷（5支装）
     * productTags : ["质量保证  假一赔十"]
     * minMarketPrice : 12800
     * minPrice : 8800
     * content : <p><img src="https://oss.dodomall.com/ueditor/1574128808008.jpg" title="1574128808008.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128808053.jpg" title="1574128808053.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128808133.jpg" title="1574128808133.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128808465.jpg" title="1574128808465.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128808640.jpg" title="1574128808640.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128808579.jpg" title="1574128808579.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809463.jpg" title="1574128809463.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809061.jpg" title="1574128809061.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809109.jpg" title="1574128809109.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809509.jpg" title="1574128809509.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809528.jpg" title="1574128809528.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128810001.jpg" title="1574128810001.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128809941.jpg" title="1574128809941.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128810252.jpg" title="1574128810252.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128810470.jpg" title="1574128810470.jpg"/></p><p><img src="https://oss.dodomall.com/ueditor/1574128810850.png" title="1574128810850.png"/></p><p><br/></p>
     * saleCount : 6791
     * stock : 9998
     * status : 1
     * tagsStr : 质量保证  假一赔十
     * consumerNoticeUrl : http://xiling-test.oss-cn-qingdao.aliyuncs.com/member/2019-12/201912090529063474B.png
     * properties : [{"propertyId":"70ea853b8aa34ccaba3c19687b5a85ce","propertyName":"规格","propertyValues":[{"propertyValueId":"3b95810936c84085a19d7435655d267d","propertyValue":"5支","noSelectFlag":0}]}]
     * skus : [{"skuId":"e9c4b727a0614848834e23180d3ab1bb","propertyValueIds":"3b95810936c84085a19d7435655d267d","stock":9998,"status":1,"retailPrice":8800,"marketPrice":12800,"skuName":"0051袋鼠妈妈·星河化妆套刷（5支装）","intro":"","saleCount":2,"thumbUrlForShopNow":"https://oss.dodomall.com/product/2019-11/20191119015921676G2.jpg","propertyValues":"规格:5支 ","propertyIds":"70ea853b8aa34ccaba3c19687b5a85ce","costPrice":0,"level10Price":8800,"level20Price":8700,"level30Price":8600}]
     * images : ["https://oss.dodomall.com/product/2019-11/2019111901592963354.jpg","https://oss.dodomall.com/product/2019-11/20191119015932685SP.jpg","https://oss.dodomall.com/product/2019-11/201911190159353357K.jpg","https://oss.dodomall.com/product/2019-11/20191119015938495X3.jpg","https://oss.dodomall.com/product/2019-11/20191119015941814BB.jpg"]
     * imageStr : ["https://oss.dodomall.com/product/2019-11/2019111901592963354.jpg","https://oss.dodomall.com/product/2019-11/20191119015932685SP.jpg","https://oss.dodomall.com/product/2019-11/201911190159353357K.jpg","https://oss.dodomall.com/product/2019-11/20191119015938495X3.jpg","https://oss.dodomall.com/product/2019-11/20191119015941814BB.jpg"]
     * storeId : 0270f3a9ba0545e3bc62be5ea00ac48d
     * storeName : 喜领
     * level10Price : 8800
     * level20Price : 8700
     * level30Price : 8600
     */

    private String productId;
    private String badgeImg;
    private String thumbUrl;
    private String productName;
    private float minMarketPrice;
    private float minPrice;
    private String content;
    private double saleCount;
    private int stock;
    private int status;
    private String tagsStr;
    private String consumerNoticeUrl;
    private String imageStr;
    private String storeId;
    private String storeName;
    private double level10Price;
    private double level20Price;
    private double level30Price;
    private List<String> productTags;
    private List<PropertiesBean> properties;
    private List<SkusBean> skus;
    private String tradeType;
    private String saleType;
    private String countryName;
    private String countryIcon;

    public int getIsCross() {
        return isCross;
    }

    public void setIsCross(int isCross) {
        this.isCross = isCross;
    }

    private int isCross;
    private List<String> images;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBadgeImg() {
        return badgeImg;
    }

    public void setBadgeImg(String badgeImg) {
        this.badgeImg = badgeImg;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getMinMarketPrice() {
        return minMarketPrice / 100;
    }

    public void setMinMarketPrice(float minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public float getMinPrice() {
        return minPrice / 100;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(double saleCount) {
        this.saleCount = saleCount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTagsStr() {
        return tagsStr;
    }

    public void setTagsStr(String tagsStr) {
        this.tagsStr = tagsStr;
    }

    public String getConsumerNoticeUrl() {
        return consumerNoticeUrl;
    }

    public void setConsumerNoticeUrl(String consumerNoticeUrl) {
        this.consumerNoticeUrl = consumerNoticeUrl;
    }

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getLevel10Price() {
        return level10Price / 100;
    }

    public void setLevel10Price(int level10Price) {
        this.level10Price = level10Price;
    }

    public double getLevel20Price() {
        return level20Price / 100;
    }

    public void setLevel20Price(int level20Price) {
        this.level20Price = level20Price;
    }

    public double getLevel30Price() {
        return level30Price / 100;
    }

    public void setLevel30Price(int level30Price) {
        this.level30Price = level30Price;
    }

    public List<String> getProductTags() {
        return productTags;
    }

    public void setProductTags(List<String> productTags) {
        this.productTags = productTags;
    }

    public List<PropertiesBean> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertiesBean> properties) {
        this.properties = properties;
    }

    public List<SkusBean> getSkus() {
        return skus;
    }

    public void setSkus(List<SkusBean> skus) {
        this.skus = skus;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
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

    public static class PropertiesBean {
        /**
         * propertyId : 70ea853b8aa34ccaba3c19687b5a85ce
         * propertyName : 规格
         * propertyValues : [{"propertyValueId":"3b95810936c84085a19d7435655d267d","propertyValue":"5支","noSelectFlag":0}]
         */

        private String propertyId;
        private String propertyName;
        private List<PropertyValuesBean> propertyValues;

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public List<PropertyValuesBean> getPropertyValues() {
            return propertyValues;
        }

        public void setPropertyValues(List<PropertyValuesBean> propertyValues) {
            this.propertyValues = propertyValues;
        }

        public static class PropertyValuesBean {
            /**
             * propertyValueId : 3b95810936c84085a19d7435655d267d
             * propertyValue : 5支
             * noSelectFlag : 0
             */

            private String propertyValueId;
            private String propertyValue;
            private int noSelectFlag;
            private boolean needSelect = true;


            public boolean isNeedSelect() {
                return needSelect;
            }

            public void setNeedSelect(boolean needSelect) {
                this.needSelect = needSelect;
            }


            public String getPropertyValueId() {
                return propertyValueId;
            }

            public void setPropertyValueId(String propertyValueId) {
                this.propertyValueId = propertyValueId;
            }

            public String getPropertyValue() {
                return propertyValue;
            }

            public void setPropertyValue(String propertyValue) {
                this.propertyValue = propertyValue;
            }

            public int getNoSelectFlag() {
                return noSelectFlag;
            }

            public void setNoSelectFlag(int noSelectFlag) {
                this.noSelectFlag = noSelectFlag;
            }
        }
    }

    public static class SkusBean {
        /**
         * skuId : e9c4b727a0614848834e23180d3ab1bb
         * propertyValueIds : 3b95810936c84085a19d7435655d267d
         * stock : 9998
         * status : 1
         * retailPrice : 8800
         * marketPrice : 12800
         * skuName : 0051袋鼠妈妈·星河化妆套刷（5支装）
         * intro :
         * saleCount : 2
         * thumbUrlForShopNow : https://oss.dodomall.com/product/2019-11/20191119015921676G2.jpg
         * propertyValues : 规格:5支
         * propertyIds : 70ea853b8aa34ccaba3c19687b5a85ce
         * costPrice : 0
         * level10Price : 8800
         * level20Price : 8700
         * level30Price : 8600
         */

        private String skuId;
        private String propertyValueIds;
        private int stock;
        private int status;
        private double retailPrice;
        private double marketPrice;
        private String skuName;
        private String intro;
        private int saleCount;
        private String thumbUrlForShopNow;
        private String propertyValues;
        private String propertyIds;
        private int costPrice;
        private double level10Price;
        private double level20Price;

        public double getShowShippingFree() {
            return showShippingFree/100;
        }

        private double showShippingFree;

        public double getRetailTax() {
            return retailTax / 100;
        }

        public double getLevel10Tax() {
            return level10Tax / 100;
        }

        public double getLevel20Tax() {
            return level20Tax / 100;
        }

        public double getLevel30Tax() {
            return level30Tax / 100;
        }

        private double level30Price;
        private int step;
        private double retailTax;
        private double level10Tax;
        private double level20Tax;
        private double level30Tax;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getPropertyValueIds() {
            return propertyValueIds;
        }

        public void setPropertyValueIds(String propertyValueIds) {
            this.propertyValueIds = propertyValueIds;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getRetailPrice() {
            return retailPrice / 100;
        }

        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }

        public double getMarketPrice() {
            return marketPrice / 100;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public String getThumbUrlForShopNow() {
            return thumbUrlForShopNow;
        }

        public void setThumbUrlForShopNow(String thumbUrlForShopNow) {
            this.thumbUrlForShopNow = thumbUrlForShopNow;
        }

        public String getPropertyValues() {
            return propertyValues;
        }

        public void setPropertyValues(String propertyValues) {
            this.propertyValues = propertyValues;
        }

        public String getPropertyIds() {
            return propertyIds;
        }

        public void setPropertyIds(String propertyIds) {
            this.propertyIds = propertyIds;
        }

        public int getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(int costPrice) {
            this.costPrice = costPrice;
        }

        public double getLevel10Price() {
            return level10Price / 100;
        }

        public void setLevel10Price(int level10Price) {
            this.level10Price = level10Price;
        }

        public double getLevel20Price() {
            return level20Price / 100;
        }

        public void setLevel20Price(int level20Price) {
            this.level20Price = level20Price;
        }

        public double getLevel30Price() {
            return level30Price / 100;
        }

        public void setLevel30Price(int level30Price) {
            this.level30Price = level30Price;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

    }
}
