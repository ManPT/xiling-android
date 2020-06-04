package com.xiling.dduis.bean;

import java.util.List;

public class HomeRecommendDataBean {
    /**
     * datas : [{"productId":"8b29b107700a457098046d1575d11e8c","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191119094628130WP.jpg","productName":"BOB口红唇膏持久保湿不易脱色不易沾杯豆沙姨正品初学者防水唇膏","productTags":["质量保证  假一赔十"],"minMarketPrice":4990,"minPrice":2890,"stock":407,"sellOut":"","status":"","tagsStr":"质量保证  假一赔十","level10Price":2890,"level20Price":2790,"level30Price":2690},{"productId":"2fb3cb9a78924768959d19bf4ecbf20a","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191119094505233QQ.jpg","productName":"六只装色娜娜淡雅玫瑰香/百合香/洋甘菊护手霜补水保湿防干燥去死皮秋冬手部护理30g*6支装","productTags":["质量保证 假一赔十"],"minMarketPrice":3900,"minPrice":2090,"stock":19978,"sellOut":"","status":"","tagsStr":"质量保证 假一赔十","level10Price":2090,"level20Price":1990,"level30Price":1890},{"productId":"4a129f6891204fd88676e8197326e8c2","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/201911190944156477V.jpg","productName":"袋鼠妈妈·星河化妆套刷（5支装）","productTags":["质量保证  假一赔十"],"minMarketPrice":12800,"minPrice":8800,"stock":10000,"sellOut":"","status":"","tagsStr":"质量保证  假一赔十","level10Price":8800,"level20Price":8700,"level30Price":8600},{"productId":"7dd133b39a4c4506af2a86dde899b08e","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191119094337241C5.jpg","productName":"袋鼠妈妈燕窝深润亮皙滋养手膜（6对）","productTags":["质量保证 假一赔十"],"minMarketPrice":6900,"minPrice":4790,"stock":4999,"sellOut":"","status":"","tagsStr":"质量保证 假一赔十","level10Price":4790,"level20Price":4690,"level30Price":4590},{"productId":"269a548ffc344e85883c702ef0a90f8d","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191114092539286FK.jpg","productName":"24年老国货依风凡士林润肤霜250g 防干防裂 软化死皮","productTags":["质量保证 假一赔十"],"minMarketPrice":2900,"minPrice":1790,"stock":9975,"sellOut":"","status":"","tagsStr":"质量保证 假一赔十","level10Price":1790,"level20Price":1690,"level30Price":1590},{"productId":"0751da8bca5c4826b98ad7cb04ab90f8","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/2019111409245814546.jpg","productName":"BOB隔离霜女妆前打底保湿补水遮瑕定妆隐形毛孔妆前乳裸妆学生","productTags":["质量保证  假一赔十"],"minMarketPrice":6990,"minPrice":3790,"stock":993,"sellOut":"","status":"","tagsStr":"质量保证  假一赔十","level10Price":3790,"level20Price":3690,"level30Price":3590},{"productId":"539980dcd1df4f87bc3c2923dde2574f","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191114092422006UU.jpg","productName":"bofayalo便携绘画涂鸦本  水粉笔 走到哪里画到哪里","productTags":["质量保证 假一赔十"],"minMarketPrice":3900,"minPrice":1490,"stock":29998,"sellOut":"","status":"","tagsStr":"质量保证 假一赔十","level10Price":1490,"level20Price":1390,"level30Price":1290},{"productId":"858a709b9bfd4a50bd5622409a2f2b2e","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/201911140923371087M.jpg","productName":"bofayalo儿童玩具分类垃圾桶基础款/背带款 认知融入玩乐","productTags":["质量保证 假一赔十"],"minMarketPrice":4900,"minPrice":2690,"stock":10000,"sellOut":"","status":"","tagsStr":"质量保证 假一赔十","level10Price":2690,"level20Price":2590,"level30Price":2490},{"productId":"70ccaa1136364514b984190ce0269430","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/20191114092235153EH.jpg","productName":"恒源祥 楚楚花颜全棉印花被套（红）","productTags":["质量保证  假一赔十"],"minMarketPrice":39900,"minPrice":25900,"stock":10000,"sellOut":"","status":"","tagsStr":"质量保证  假一赔十","level10Price":25900,"level20Price":25800,"level30Price":25700},{"productId":"01cff951a602430994a399a27804f843","badgeImg":"","thumbUrl":"https://oss.dodomall.com/product/2019-11/201911140921591986B.jpg","productName":"法国进口黑科技  梵斯萃丽素颜CC霜","productTags":["质量保证  假一赔十"],"minMarketPrice":29900,"minPrice":22900,"stock":9999,"sellOut":"","status":"","tagsStr":"质量保证  假一赔十","level10Price":22900,"level20Price":22800,"level30Price":22700}]
     * pageOffset : 1
     * pageSize : 10
     * totalRecord : 22
     * totalPage : 3
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

    public static class ExBean {
    }

    public static class DatasBean {
        /**
         * productId : 8b29b107700a457098046d1575d11e8c
         * badgeImg :
         * thumbUrl : https://oss.dodomall.com/product/2019-11/20191119094628130WP.jpg
         * productName : BOB口红唇膏持久保湿不易脱色不易沾杯豆沙姨正品初学者防水唇膏
         * productTags : ["质量保证  假一赔十"]
         * minMarketPrice : 4990
         * minPrice : 2890
         * stock : 407
         * sellOut :
         * status :
         * tagsStr : 质量保证  假一赔十
         * level10Price : 2890
         * level20Price : 2790
         * level30Price : 2690
         */

        private String productId;
        private String badgeImg;
        private String thumbUrl;
        private String productName;
        private double minMarketPrice;
        private double minPrice;
        private int stock;
        private String sellOut;
        private int status;
        private String tagsStr;
        private float level10Price;
        private float level20Price;
        private float level30Price;
        int isCross;
        int step;
        String tradeType;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        private String skuId;
        private List<String> productTags;

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

        public double getMinMarketPrice() {
            return minMarketPrice / 100;
        }

        public void setMinMarketPrice(double minMarketPrice) {
            this.minMarketPrice = minMarketPrice;
        }

        public double getMinPrice() {
            return minPrice / 100;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getSellOut() {
            return sellOut;
        }

        public void setSellOut(String sellOut) {
            this.sellOut = sellOut;
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

        public float getLevel10Price() {
            return level10Price / 100;
        }

        public void setLevel10Price(float level10Price) {
            this.level10Price = level10Price;
        }

        public float getLevel20Price() {
            return level20Price / 100;
        }

        public void setLevel20Price(float level20Price) {
            this.level20Price = level20Price;
        }

        public float getLevel30Price() {
            return level30Price / 100;
        }

        public void setLevel30Price(float level30Price) {
            this.level30Price = level30Price;
        }

        public List<String> getProductTags() {
            return productTags;
        }

        public void setProductTags(List<String> productTags) {
            this.productTags = productTags;
        }

        public int getIsCross() {
            return isCross;
        }

        public void setIsCross(int isCross) {
            this.isCross = isCross;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }
    }
}
