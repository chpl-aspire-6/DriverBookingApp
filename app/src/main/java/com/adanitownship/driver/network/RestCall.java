package com.adanitownship.driver.network;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Single;

public interface RestCall {

    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverLogin(@Field("driverLogin") String driverLogin, @Field("mobile_number") String mobile_number);

    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverOtpVerification(@Field("driverOtpVerification") String driverOtpVerification, @Field("mobile_number") String mobile_number, @Field("otp") String otp, @Field("driver_token") String driver_token, @Field("driver_device") String driver_device,@Field("mac_address") String mac_address, @Field("app_version") String app_version);

    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverResendOtp(@Field("driverResendOtp") String driverResendOtp, @Field("mobile_number") String mobile_number);


    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverBookingList(@Field("driverBookingList") String driverBookingList, @Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverBookingDetails(@Field("driverBookingDetails") String driverBookingDetails, @Field("request_id") String request_id);

    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> acceptbooking(@Field("acceptbooking") String acceptbooking, @Field("request_id") String request_id);

@FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverDropUser(@Field("driverDropUser") String acceptbooking, @Field("request_id") String request_id);

@FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverPickupUser(@Field("driverPickupUser") String acceptbooking, @Field("request_id") String request_id);


    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> rejectbooking(@Field("rejectbooking") String rejectbooking, @Field("request_id") String request_id, @Field("reject_reason") String reject_reason);


    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverDutyUpdate(@Field("driverDutyUpdate") String driverDutyUpdate, @Field("driver_id") String driver_id, @Field("duty_status") int duty_status);



}

