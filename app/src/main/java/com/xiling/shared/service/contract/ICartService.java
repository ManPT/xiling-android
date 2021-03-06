package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.DDSuggestListBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.shared.bean.CartAmount;
import com.xiling.shared.bean.CartStore;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-09
 */
public interface ICartService {
    /**
     * 购物车-列表
     * @return
     */
    @GET("shopping-cart/list")
    Observable<RequestResult<List<XLCardListBean>>> getAllList();

    /**
     * 添加购物车
     * @param requestBody
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("shopping-cart/add")
    Observable<RequestResult<Boolean>> addShopCart(@Body RequestBody requestBody);

    /**
     * 删除购物车商品
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("shopping-cart/delete")
    Observable<RequestResult<Boolean>>deleteShopCart(@Body RequestBody requestBody);

    /**
     * 获取购物车数量
     * @return
     */
    @GET("shopping-cart/count")
    Observable<RequestResult<Integer>> getCardCount();


    /**
     * 结算前校验商品箱规是否正确
     * @param requestBody
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("order/pre-check")
    Observable<RequestResult<Boolean>> preCheck(@Body RequestBody requestBody);

    @GET("cart/getAccountList")
    Observable<RequestResult<List<CartStore>>> getListBySkuIds(@Query("skuIds") String skuIds);

    @FormUrlEncoded
    @POST("cart/operateCart")
    Observable<RequestResult<CartAmount>> updateCartItem(@Field("skuId") String skuId, @Field("quantity") int amount);

    @FormUrlEncoded
    @POST("cart/operateCartAdd")
    Observable<RequestResult<CartAmount>> addToCart(@Field("skuId") String skuId, @Field("quantity") int amount);

    @FormUrlEncoded
    @POST("cart/removeSkuId")
    Observable<RequestResult<Object>> removeItem(@Field("skuIds") String skuIds);

    @GET("cart/getCartQuantity")
    Observable<RequestResult<CartAmount>> getTotal();

}
