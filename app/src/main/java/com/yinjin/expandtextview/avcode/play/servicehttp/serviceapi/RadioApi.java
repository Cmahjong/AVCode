package com.yinjin.expandtextview.avcode.play.servicehttp.serviceapi;

import com.yinjin.expandtextview.avcode.play.servicehttp.bean.RadioLiveChannelBean;
import com.yinjin.expandtextview.avcode.play.servicehttp.service.HttpMethod;
import com.yinjin.expandtextview.avcode.play.servicehttp.service.RadioBaseApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RadioApi extends RadioBaseApi {

    public static final String BASE_URL_PANDA = "http://pacc.radio.cn/";
    public static RadioApi radioApi;
    public RadioService radioService;

    public RadioApi() {
        radioService = HttpMethod.getInstance(BASE_URL_PANDA).createApi(RadioService.class);
    }

    public static RadioApi getInstance() {
        if (radioApi == null) {
            radioApi = new RadioApi();
        }
        return radioApi;
    }
    /*-------------------------------------获取的方法-------------------------------------*/

    public void getToken(Observer<Integer> subscriber) {
        Observable observable = radioService.getToken()
                .map(new HttpResultFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void getLiveByParam(String token, String channelPlaceId, int limit, int offset, Observer<ArrayList<RadioLiveChannelBean>> subscriber) {
        Observable observable = radioService.getLiveByParam(token, channelPlaceId, limit, offset)
                .map(new HttpResultFunc<ArrayList<RadioLiveChannelBean>>());

        toSubscribe(observable, subscriber);
    }

}
