package com.adanitownship.driver.networkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverDutyStatusResponse {


    @SerializedName("duty_status")
    @Expose
    private String duty_status;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getDuty_status() {
        return duty_status;
    }

    public void setDuty_status(String duty_status) {
        this.duty_status = duty_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
