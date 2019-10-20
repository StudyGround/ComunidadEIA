package com.futureapp.studyground.fcm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface WS {


    @Headers({"Content-type:application/json","Authorization: key=AAAAJVqYKRs:APA91bHiGfoWwpLR7POqCnkwBeSEaSiYNL7KShQtVJcgar5lUP26GpRHMq_Qx2wQ6HgESoTn-Mxf03fo_htk4rEoqlC3Qt0h2tvc463kTOAlxUbVLv_eO6EH04ywxo2Hw-iXFLsLGcRH"})
        @POST("fcm/send")
    Call<Response> enviarNotificacion(@Body Sender body);
}
