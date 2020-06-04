package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.BrandBean;
import com.xiling.ddui.bean.BrandListBean;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.DDSkuListBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.bean.ProductParameterBean;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.bean.SecondClassificationBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.dduis.bean.DDProductBean;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.module.groupBuy.GroupBuyModel;
import com.xiling.module.order.body.AddCommentBody;
import com.xiling.module.push.PushSkuDetailModel;
import com.xiling.shared.bean.Category;
import com.xiling.shared.bean.InstantData;
import com.xiling.shared.bean.Keyword;
import com.xiling.shared.bean.PopupOrderList;
import com.xiling.shared.bean.Product;
import com.xiling.shared.bean.ProductComment;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.ViewHistory;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IProductService {

    // 获取商品税率
    @GET("product/getProductTax")
    Observable<RequestResult<Double>> getProductTax(@Query("spuId") String spuId);

    // 品牌列表--电话簿
    @GET("brand/list")
    Observable<RequestResult<BrandListBean>> getBrandList();

    // 一级分类接口
    @GET("home/getTopCategory")
    Observable<RequestResult<ArrayList<TopCategoryBean>>> getTopCategory();

    // 二级分类和品牌接口
    @FormUrlEncoded
    @POST("secondLevel/getSecondCategory")
    Observable<RequestResult<SecondCategoryBean>> getSecondCategory(@Field("nodeId") String nodeId);

    //获取二级类目列表
    @FormUrlEncoded
    @POST("secondLevel/getSecondCategory")
    Observable<RequestResult<SecondClassificationBean>> getSecondClassification(@Field("nodeId") String nodeId);


    // 获取商品列表接口
    @FormUrlEncoded
    @POST("secondLevel/getProductList")
    Observable<RequestResult<HomeRecommendDataBean>> getProductList(@FieldMap HashMap<String, String> map);


    // 获取品牌详情
    @FormUrlEncoded
    @POST("secondLevel/getBrandDetail")
    Observable<RequestResult<BrandBean>> getBrandDetail(@Field("brandId") String brandId);

    // 获取指定国家馆对应的品牌馆列表
    @GET("national-pavilion/detail")
    Observable<RequestResult<List<BrandListBean.GroupsBean.BrandsBean>>> getNationalPavilionDetail(@Query("id") String brandId);




    //  获取商品详情（新）
    @GET("product/spuDetail")
    Observable<RequestResult<ProductNewBean>> getProductDetail(@Query("spuId") String productId);


    //  获取商品参数
    @GET("product/spuParameter")
    Observable<RequestResult<List<ProductParameterBean>>> getSpuParameter(@Query("spuId") String productId);


    //  获取商品详情（旧）
    @GET("product/spuDetail")
    Observable<RequestResult<Product>> getDetailById(@Query("spuId") String productId);


    @Deprecated
    @GET("category/list")
    Observable<RequestResult<ArrayList<Category>>> getCategoryList();

    @GET("product/list/{categoryId}")
    Observable<RequestResult<PaginationEntity<Product, Object>>> getProductList(@Path("categoryId") String categoryId, @Query("pageOffset") int page, @Query("pageSize") int pageSize);

    @GET("product/skulist")
    Observable<RequestResult<ArrayList<SkuInfo>>> getListBySkuIds(@Query("skuIds") String skuIds);

    @GET("product/skuDetail")
    Observable<RequestResult<SkuInfo>> getSkuById(@Query("skuId") String skuId);


    @GET("product/skuDetailByProperty")
    Observable<RequestResult<SkuInfo>> getSkuByPropertyValueIds(@Query("productId") String productId, @Query("propertyValueIds") String propertyValueIds);

    @GET("secondKill/getIndexSecondKill")
    Observable<RequestResult<InstantData>> getInstantComponentData();

    @GET("product/getSpuListByCategory")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getCategoryProductList(@Query("categoryId") String mCategoryId, @Query("pageOffset") int page);

    @GET("product/searchSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> search(@Query("skuName") String keyword, @Query("pageOffset") int page);

    @GET("productPush/searchSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> searchPush(@Query("skuName") String keyword, @Query("pageOffset") int page);

    @FormUrlEncoded
    @POST("viewHistory/addViewRecord")
    Observable<Object> addViewRecord(
            @Field("memberId") String memberId,
            @Field("productId") String productId
    );

    @GET("viewHistory/getViewHistory")
    Observable<RequestResult<ViewHistory>> getViewHistory();

    @GET("product/getKeyWordList")
    Observable<RequestResult<List<Keyword>>> getHotKeywords();

    @GET("popupOrder/getPopupOrderList")
    Observable<RequestResult<PopupOrderList>> getPopupOrderList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @POST("productComment/saveProductComment")
    Observable<RequestResult<Object>> saveProductComment(
            @Body AddCommentBody body
    );

    @GET("productComment/getProductComment")
    Observable<RequestResult<PaginationEntity<ProductComment, Object>>> getProductComment(
            @Query("productId") String productId,
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("product/getNewUpSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getNewUpSkuList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("product/getSaleUpSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getSaleSkuList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("groupOrder/getGroupCode")
    Observable<RequestResult<GroupBuyModel>> getGroupInfo(
            @Query("groupCode") String id
    );

    @GET
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getPushHotProductList(
            @Url String url,
            @Query("pageSize") int pageSize,
            @Query("pageOffset") int page,
            @Query("sort") int sort
    );

    @GET
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getPushCategoryProductList(
            @Url String url,
            @Query("pageSize") int pageSize,
            @Query("pageOffset") int page,
            @Query("sort") int sort,
            @Query("categoryId") String categoryId
    );

    @GET("productPush/skuPushDetail")
    Observable<RequestResult<PushSkuDetailModel>> getPushSkuDetail(
            @Query("skuId") String skuId
    );

    /**
     * 根据关键字搜索SKU列表
     */
    @GET("product/getSkuPagerLikeName")
    Observable<RequestResult<DDSkuListBean>> searchSkuByKeyword(
            @Query("skuName") String keyword,
            @Query("sortField") String sortType,
            @Query("pageSize") int size,
            @Query("pageOffset") int page
    );

    // 猜你喜欢
    @GET("product/thoughtULike")
    Observable<RequestResult<ListResultBean<DDProductBean>>> getSuggestProduct(@Query("pageOffset") int pageOffset,
                                                                               @Query("pageSize") int pageSize);


    /**
     * 开抢提醒/取消提醒
     *
     * @param productId   产品ID
     * @param flashSaleId 抢购场次ID
     */
    @GET("v20/activity/panic-buying/rush-push")
    Observable<RequestResult<Object>> noticeFlashSale(@Query("flashSpuId") String productId, @Query("flashSaleId") String flashSaleId);

}
