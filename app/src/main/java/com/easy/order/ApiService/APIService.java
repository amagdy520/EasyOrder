package com.easy.order.ApiService;

import com.easy.order.Notifications.MyResponse;
import com.easy.order.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" +
                            "AAAAfmTZ-XQ:APA91bHnfoYCHrqrZ4Hk6Z1knG900gyxVrQM1Mlh1JEkmG6YLt5aD1FwAYh2-KnROlAf1ax_H8llNz3sWI17ujbOYq0Ye6Co8jBaDHSCqwICA1NBXO8F1aO27uwC8VhWS0Sn4rjfnnao"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
