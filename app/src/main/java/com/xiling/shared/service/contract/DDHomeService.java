package com.xiling.shared.service.contract;


import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.IndexBrandBean;
import com.xiling.ddui.bean.IndexCategoryBean;
import com.xiling.ddui.bean.NationalPavilionBean;
import com.xiling.dduis.bean.DDHomeDataBean;
import com.xiling.dduis.bean.DDHomeRushDataBean;
import com.xiling.dduis.bean.DDProductPageBean;
import com.xiling.dduis.bean.DDRushSpuPageBean;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.shared.bean.api.RequestResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 2.0相关的首页接口
 */
public interface DDHomeService {

    /**
     * 获取首页数据
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("home/index")
    Observable<RequestResult<HomeDataBean>> getHomeData();


    /**
     * 获取国家馆列表
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("national-pavilion/list")
    Observable<RequestResult<List<NationalPavilionBean>>> getNationalPavilionList();

    /**
     * 首页分类
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("home/getIndexCategory")
    Observable<RequestResult<List<IndexCategoryBean>>> getIndexCategory();



    /**
     * 获取首页推荐数据
     *
     * @param pageNo   页码数
     * @param pageSize 每页条目数
     */
    @GET("home/getProductRecommend")
    Observable<RequestResult<HomeRecommendDataBean>> getHomeRecommendData(@Query("pageOffset") int pageNo, @Query("pageSize") int pageSize);


    /**
     * 精选品牌
     *
     */
    @GET("home/getIndexBrand")
    Observable<RequestResult<List<IndexBrandBean>>> getIndexBrand();





    /**
     * 获取首页秒杀数据
     */
    @GET("v20/activity/panic-buying/get-time-list")
    Observable<RequestResult<DDHomeRushDataBean>> getHomeRushData();

    /**
     * 获取首页今日必推数据
     *
     * @param pageNo   页码数
     * @param pageSize 每页条目数
     */
    @GET("v20/productRecommend/getProductRecommend")
    Observable<RequestResult<DDProductPageBean>> getHomeSuggestData(@Query("pageOffset") int pageNo, @Query("pageSize") int pageSize);

    /**
     * 获取抢购商品列表
     * <p>
     * 首页获取需要自己指定条数
     * 列表请求按照正常条数请求
     *
     * @param flashSaleId 场次ID
     * @param pageNo      页码数
     * @param pageSize    每页条目数
     */
    @GET("v20/activity/panic-buying/get-list-by-time")
    Observable<RequestResult<DDRushSpuPageBean>> getRushListData(@Query("flashSaleId") String flashSaleId, @Query("page") int pageNo, @Query("size") int pageSize);


    /**
     * 获取二级分类列表
     */
    @POST("v20/secondLevel/getSecondCategory")
    @FormUrlEncoded
    Observable<RequestResult<ArrayList<CategoryBean>>> getSubCategory(@Field("nodeId") String categoryId);

    /**
     * 获取一级分类的所有商品
     * <p>
     * 此接口适用于，一级分类所有商品，二级分类所有商品，搜索所有商品，带删选条件和分页
     *
     * @param categoryId           一级分类 为了全部搜索
     * @param secondCategoryIdList 二级分类 可以搜索单个分类的商品
     * @param page                 页码数
     * @param pageSize             单页数据条数
     * @param isRush               是否仅显示抢购商品 1 抢购 0 不抢购
     * @param isFreeShip           是否仅显示包邮 1 包邮 0 不包邮
     * @param minPrice             价格区间 - 最小价格
     * @param maxPrice             价格区间 - 最大价格
     * @param orderType            排序方式 0 降序 1 升序
     * @param orderBy              排序字段 0 价格 1 上新2 销量 3 佣金
     */
    @POST("v20/secondLevel/getProductList")
    @FormUrlEncoded
    Observable<RequestResult<DDProductPageBean>> getCategoryProductByFilter(
            @Field("categoryId") String categoryId,
            @Field("secondCategoryIdList") String secondCategoryIdList,
            @Field("pageOffset") int page,
            @Field("pageSize") int pageSize,
            @Field("isFlashSale") int isRush,
            @Field("isShippingFree") int isFreeShip,
            @Field("minPrice") String minPrice,
            @Field("maxPrice") String maxPrice,
            @Field("orderType") int orderType,
            @Field("orderBy") int orderBy
    );

    /**
     * 获取一级分类的所有商品
     * <p>
     * 此接口适用于，一级分类所有商品，二级分类所有商品，搜索所有商品，带删选条件和分页
     *
     * @param keyWord    关键字
     * @param page       页码数
     * @param pageSize   单页数据条数
     * @param isRush     是否仅显示抢购商品 1 抢购 0 不抢购
     * @param isFreeShip 是否仅显示包邮 1 包邮 0 不包邮
     * @param minPrice   价格区间 - 最小价格
     * @param maxPrice   价格区间 - 最大价格
     * @param orderType  排序方式 0 降序 1 升序
     * @param orderBy    排序字段 0 价格 1 上新2 销量 3 佣金
     */
    @POST("v20/secondLevel/getProductList")
    @FormUrlEncoded
    Observable<RequestResult<DDProductPageBean>> searchProductByKeyword(
            @Field("keyWord") String keyWord,
            @Field("pageOffset") int page,
            @Field("pageSize") int pageSize,
            @Field("isFlashSale") int isRush,
            @Field("isShippingFree") int isFreeShip,
            @Field("minPrice") String minPrice,
            @Field("maxPrice") String maxPrice,
            @Field("orderType") int orderType,
            @Field("orderBy") int orderBy
    );

    /**
     * 开抢提醒/取消提醒
     *
     * @param productId   产品ID
     * @param flashSaleId 抢购场次ID
     */
    @GET("v20/activity/panic-buying/rush-push")
    Observable<RequestResult<Object>> targetNotice(@Query("flashSpuId") String productId, @Query("flashSaleId") String flashSaleId);

    /**
     * 增加推广次数
     *
     * @param productId 产品ID
     */
    @POST("v20/secondLevel/extension")
    @FormUrlEncoded
    Observable<RequestResult<Object>> addShareCount(@Field("productId") String productId);


}
