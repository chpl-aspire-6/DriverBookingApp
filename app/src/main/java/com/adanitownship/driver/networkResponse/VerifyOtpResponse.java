package com.adanitownship.driver.networkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("travel_agent_id")
    @Expose
    private String travelAgentId;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("alternate_number")
    @Expose
    private String alternateNumber;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("gender_name")
    @Expose
    private String genderName;
    @SerializedName("permanent_address")
    @Expose
    private String permanentAddress;
    @SerializedName("licence_number")
    @Expose
    private String licenceNumber;
    @SerializedName("driver_status")
    @Expose
    private String driverStatus;
    @SerializedName("driver_photo")
    @Expose
    private String driverPhoto;
    @SerializedName("licence_image")
    @Expose
    private String licenceImage;
    @SerializedName("id_proof")
    @Expose
    private String idProof;
    @SerializedName("duty_status")
    @Expose
    private String dutyStatus;
    @SerializedName("duty_status_name")
    @Expose
    private String dutyStatusName;

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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTravelAgentId() {
        return travelAgentId;
    }

    public void setTravelAgentId(String travelAgentId) {
        this.travelAgentId = travelAgentId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getLicenceImage() {
        return licenceImage;
    }

    public void setLicenceImage(String licenceImage) {
        this.licenceImage = licenceImage;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(String dutyStatus) {
        this.dutyStatus = dutyStatus;
    }

    public String getDutyStatusName() {
        return dutyStatusName;
    }

    public void setDutyStatusName(String dutyStatusName) {
        this.dutyStatusName = dutyStatusName;
    }
}
