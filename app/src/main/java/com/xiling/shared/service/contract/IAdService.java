package com.xiling.shared.service.contract;

import com.xiling.shared.bean.MainAdModel;
import com.xiling.shared.bean.Splash;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-03
 */
public interface IAdService {

    @GET("splash-screen/list")
    Observable<RequestResult<List<Splash>>> getSplashAd(@Query("type") int type);

    @GET("popupWindows/get")
    Observable<RequestResult<MainAdModel>> getMainAd();
}
