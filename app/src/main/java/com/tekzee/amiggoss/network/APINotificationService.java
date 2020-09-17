package com.tekzee.amiggoss.network;

import com.tekzee.amiggoss.base.model.MyResponse;
import com.tekzee.amiggoss.base.model.NotificationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APINotificationService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAUYm6Qvo:APA91bH64ki0WzDhtLmeOB3-rP5NuJsouLaPOBrlZpDU34326qsVzdrPuEP4FNqJ654GzKlcOsk2qmAxV_UXxxBx3bC31kb6dVBJnPDBmBaIxZXvKLLdcJ22dp4uqqcppiJ2mrRRXdEr"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationData body);
}