package com.yinjin.expandtextview.avcode.play.servicehttp.serviceapi;


import com.yinjin.expandtextview.avcode.play.servicehttp.bean.PandaTvDataBean;
import com.yinjin.expandtextview.avcode.play.servicehttp.bean.PandaTvLiveDataBean;
import com.yinjin.expandtextview.avcode.play.servicehttp.httpentity.VideoHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface VideoService {

    @GET("ajax_get_live_list_by_cate")
    Observable<VideoHttpResult<PandaTvDataBean>> getVideList(@Query("cate") String cate,
                                                             @Query("pageno") int pageno,
                                                             @Query("pagenum") int pagenum,
                                                             @Query("room") int room,
                                                             @Query("version") String version);

    @GET("ajax_get_liveroom_baseinfo")
    Observable<VideoHttpResult<PandaTvLiveDataBean>> getLiveUrl(@Query("roomid") String roomid,
                                                                @Query("__version") String version,
                                                                @Query("slaveflag") int slaveflag,
                                                                @Query("type") String type,
                                                                @Query("__plat") String __plat);
}
