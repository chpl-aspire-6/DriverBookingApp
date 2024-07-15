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
    Single<String> acceptbooking(@Field("acceptbooking") String acceptbooking,
                                 @Field("request_id") String request_id,
                                 @Field("driver_id") String driver_id,
                                 @Field("travel_agent_id") String travel_agent_id,
                                 @Field("pickup_date") String pickup_date,
                                 @Field("pickup_time") String pickup_time,
                                 @Field("pickup_location_name") String pickup_location_name,
                                 @Field("drop_location_name") String drop_location_name,
                                 @Field("driver_mobile_no") String driver_mobile_no,
                                 @Field("driver_name") String driver_name,
                                 @Field("travel_agent_name") String travel_agent_name,
                                 @Field("travel_agent_phone_no") String travel_agent_phone_no,
                                 @Field("society_id") String society_id,
                                 @Field("unit_id") String unit_id,
                                 @Field("visitor_name") String visitor_name,
                                 @Field("visitor_mobile") String visitor_mobile,
                                 @Field("visit_date") String visitDate,
                                 @Field("visit_time") String visitTime,
                                 @Field("floor_id") String floor_id,
                                 @Field("company_name") String company_name,
                                 @Field("user_name") String user_name,
                                 @Field("user_id") String user_id,
                                 @Field("block_id") String block_id,
                                 @Field("vehicle_no") String vehicle_no,
                                 @Field("visit_from_company") String visit_from_company

                                 );

@FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverDropUser(@Field("driverDropUser") String driverDropUser, @Field("request_id") String request_id);

@FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverPickupUser(@Field("driverPickupUser") String driverPickupUser, @Field("request_id") String request_id);


    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> rejectbooking(@Field("rejectbooking") String rejectbooking, @Field("request_id") String request_id, @Field("reject_reason") String reject_reason ,@Field("driver_id") String driver_id,
                                 @Field("travel_agent_id") String travel_agent_id,
                                 @Field("pickup_date") String pickup_date,
                                 @Field("pickup_time") String pickup_time,
                                 @Field("pickup_location_name") String pickup_location_name,
                                 @Field("drop_location_name") String drop_location_name,
                                 @Field("driver_mobile_no") String driver_mobile_no,
                                 @Field("driver_name") String driver_name,
                                 @Field("travel_agent_name") String travel_agent_name,
                                 @Field("travel_agent_phone_no") String travel_agent_phone_no,
                                 @Field("society_id") String society_id,
                                 @Field("user_id") String user_id
                                 );


    @FormUrlEncoded
    @POST("driverController.php")
    Single<String> driverDutyUpdate(@Field("driverDutyUpdate") String driverDutyUpdate, @Field("driver_id") String driver_id, @Field("duty_status") int duty_status);


 @FormUrlEncoded
    @POST("driverController.php")
    Single<String> deleteNotification(@Field("deleteNotification") String deleteNotification, @Field("driver_id") String driver_id, @Field("notification_id") String notification_id);


 @FormUrlEncoded
    @POST("driverController.php")
    Single<String> getNotificationData(@Field("getNotificationData") String getNotificationData, @Field("driver_id") String driver_id);


 @FormUrlEncoded
    @POST("driverController.php")
    Single<String> deleteAllNotification(@Field("deleteAllNotification") String deleteAllNotification, @Field("driver_id") String driver_id);

    @FormUrlEncoded
        @POST("driverController.php")
        Single<String> driverDutyStatus(@Field("driverDutyStatus") String driverDutyStatus, @Field("driver_id") String driver_id);
    @FormUrlEncoded
    @POST("language_controller.php")
    Single<String> getLanguage(
            @Field("getLanguageNew") String getLanguage,
            @Field("country_id") String country_id);

    @FormUrlEncoded
    @POST("language_controller.php")
    Single<Object> getLanguageValues(@Field("getLanguageValues") String getLanguageValues,
                                     @Field("society_id") String society_id,
                                     @Field("country_id") String country_id,
                                     @Field("language_id") String language_id);



}

