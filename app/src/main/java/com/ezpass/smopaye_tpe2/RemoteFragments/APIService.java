package com.ezpass.smopaye_tpe2.RemoteFragments;


import com.ezpass.smopaye_tpe2.RemoteNotifications.MyResponse;
import com.ezpass.smopaye_tpe2.RemoteNotifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({"Authorization: key=AAAAeZCcmGU:APA91bE1sASgnUZn2Sn980h9q-ElhSCWrHwqFFKuu88rcRIcS6IVSsAbqPwavSgxXQf4P3zvx-XRAxoHBbogjRNBc-Fx0qEKUgfFMS1cN_4BH17YRBfHaKOFeD16qPKJYSz-t8lkpKVW",
            "Content-Type:application/json"})

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
