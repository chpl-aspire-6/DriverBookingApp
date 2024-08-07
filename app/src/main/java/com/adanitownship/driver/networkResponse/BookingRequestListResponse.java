package com.adanitownship.driver.networkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingRequestListResponse {

    @SerializedName("bookingList")
    @Expose
    private List<Booking> bookingList;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
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


    public class Booking {

        @SerializedName("request_id")
        @Expose
        private String requestId;
        @SerializedName("display_request_id")
        @Expose
        private String displayRequestId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_full_name")
        @Expose
        private String userFullName;
        @SerializedName("user_photo")
        @Expose
        private String userPhoto;
        @SerializedName("society_id")
        @Expose
        private String societyId;
        @SerializedName("pickup_point_id")
        @Expose
        private String pickupPointId;
        @SerializedName("pickup_location_name")
        @Expose
        private String pickupLocationName;
        @SerializedName("pickup_location_latitude")
        @Expose
        private String pickupLocationLatitude;
        @SerializedName("pickup_location_longitude")
        @Expose
        private String pickupLocationLongitude;
        @SerializedName("drop_point_id")
        @Expose
        private String dropPointId;
        @SerializedName("drop_location_name")
        @Expose
        private String dropLocationName;
        @SerializedName("drop_location_latitude")
        @Expose
        private String dropLocationLatitude;
        @SerializedName("drop_location_longitude")
        @Expose
        private String dropLocationLongitude;
        @SerializedName("pickup_date")
        @Expose
        private String pickupDate;
        @SerializedName("pickup_time")
        @Expose
        private String pickupTime;
        @SerializedName("company_payment_amount")
        @Expose
        private String companyPaymentAmount;
        @SerializedName("user_payment_amount")
        @Expose
        private String userPaymentAmount;
        @SerializedName("travel_agent_name")
        @Expose
        private String travelAgentName;
        @SerializedName("society_name")
        @Expose
        private String societyName;
        @SerializedName("flat_number")
        @Expose
        private String flatNumber;
        @SerializedName("travel_agent_phone_no")
        @Expose
        private String travelAgentPhoneNo;
        @SerializedName("travel_agent_alternate_mobile_no")
        @Expose
        private String travelAgentAlternateMobileNo;
        @SerializedName("travel_agent_email")
        @Expose
        private String travelAgentEmail;
        @SerializedName("driver_name")
        @Expose
        private String driverName;
        @SerializedName("driver_mobile_no")
        @Expose
        private String driverMobileNo;
        @SerializedName("driver_alternate_mobile_no")
        @Expose
        private String driverAlternateMobileNo;
        @SerializedName("driver_email_id")
        @Expose
        private String driverEmailId;
        @SerializedName("duty_status")
        @Expose
        private String dutyStatus;
        @SerializedName("driver_image")
        @Expose
        private String driverImage;
        @SerializedName("vehicle_name")
        @Expose
        private String vehicleName;
        @SerializedName("vehicle_photo")
        @Expose
        private String vehiclePhoto;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("passenger_count")
        @Expose
        private String passengerCount;
        @SerializedName("ride_type")
        @Expose
        private String rideType;
        @SerializedName("ride_type_name")
        @Expose
        private String rideTypeName;
        @SerializedName("mode_of_payment")
        @Expose
        private String modeOfPayment;
        @SerializedName("mode_of_payment_name")
        @Expose
        private String modeOfPaymentName;
        @SerializedName("request_status")
        @Expose
        private String requestStatus;
        @SerializedName("request_status_name")
        @Expose
        private String requestStatusName;
        @SerializedName("user_request_date")
        @Expose
        private String userRequestDate;
        @SerializedName("reject_reason")
        @Expose
        private String rejectReason;
        @SerializedName("user_contact_number")
        @Expose
        private String userContactNumber;
        @SerializedName("user_alternate_number")
        @Expose
        private String userAlternateNumber;
        @SerializedName("user_booking_remarks")
        @Expose
        private String userBookingRemarks;
        @SerializedName("employee_number")
        @Expose
        private String employeeNumber;
        @SerializedName("transport_details")
        @Expose
        private String transportDetails;

        @SerializedName("confirm_message")
        @Expose
        private String confirm_message;

        @SerializedName("block_id")
        @Expose
        private String block_id;

        @SerializedName("floor_id")
        @Expose
        private String floor_id;

        @SerializedName("company_name")
        @Expose
        private String company_name;

        @SerializedName("unit_id")
        @Expose
        private String unit_id;
        @SerializedName("driver_otp")
        @Expose
        private String driver_otp;

        public String getDriver_otp() {
            return driver_otp;
        }

        public void setDriver_otp(String driver_otp) {
            this.driver_otp = driver_otp;
        }

        public String getUnit_id() {
            return unit_id;
        }

        public void setUnit_id(String unit_id) {
            this.unit_id = unit_id;
        }

        public String getConfirm_message() {
            return confirm_message;
        }

        public void setConfirm_message(String confirm_message) {
            this.confirm_message = confirm_message;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getDisplayRequestId() {
            return displayRequestId;
        }

        public void setDisplayRequestId(String displayRequestId) {
            this.displayRequestId = displayRequestId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getSocietyId() {
            return societyId;
        }

        public void setSocietyId(String societyId) {
            this.societyId = societyId;
        }

        public String getPickupPointId() {
            return pickupPointId;
        }

        public void setPickupPointId(String pickupPointId) {
            this.pickupPointId = pickupPointId;
        }

        public String getPickupLocationName() {
            return pickupLocationName;
        }

        public void setPickupLocationName(String pickupLocationName) {
            this.pickupLocationName = pickupLocationName;
        }

        public String getPickupLocationLatitude() {
            return pickupLocationLatitude;
        }

        public void setPickupLocationLatitude(String pickupLocationLatitude) {
            this.pickupLocationLatitude = pickupLocationLatitude;
        }

        public String getPickupLocationLongitude() {
            return pickupLocationLongitude;
        }

        public void setPickupLocationLongitude(String pickupLocationLongitude) {
            this.pickupLocationLongitude = pickupLocationLongitude;
        }

        public String getDropPointId() {
            return dropPointId;
        }

        public void setDropPointId(String dropPointId) {
            this.dropPointId = dropPointId;
        }

        public String getDropLocationName() {
            return dropLocationName;
        }

        public void setDropLocationName(String dropLocationName) {
            this.dropLocationName = dropLocationName;
        }

        public String getDropLocationLatitude() {
            return dropLocationLatitude;
        }

        public void setDropLocationLatitude(String dropLocationLatitude) {
            this.dropLocationLatitude = dropLocationLatitude;
        }

        public String getDropLocationLongitude() {
            return dropLocationLongitude;
        }

        public void setDropLocationLongitude(String dropLocationLongitude) {
            this.dropLocationLongitude = dropLocationLongitude;
        }

        public String getPickupDate() {
            return pickupDate;
        }

        public void setPickupDate(String pickupDate) {
            this.pickupDate = pickupDate;
        }

        public String getPickupTime() {
            return pickupTime;
        }

        public void setPickupTime(String pickupTime) {
            this.pickupTime = pickupTime;
        }

        public String getCompanyPaymentAmount() {
            return companyPaymentAmount;
        }

        public void setCompanyPaymentAmount(String companyPaymentAmount) {
            this.companyPaymentAmount = companyPaymentAmount;
        }

        public String getUserPaymentAmount() {
            return userPaymentAmount;
        }

        public void setUserPaymentAmount(String userPaymentAmount) {
            this.userPaymentAmount = userPaymentAmount;
        }

        public String getTravelAgentName() {
            return travelAgentName;
        }

        public void setTravelAgentName(String travelAgentName) {
            this.travelAgentName = travelAgentName;
        }

        public String getSocietyName() {
            return societyName;
        }

        public void setSocietyName(String societyName) {
            this.societyName = societyName;
        }

        public String getFlatNumber() {
            return flatNumber;
        }

        public void setFlatNumber(String flatNumber) {
            this.flatNumber = flatNumber;
        }

        public String getTravelAgentPhoneNo() {
            return travelAgentPhoneNo;
        }

        public void setTravelAgentPhoneNo(String travelAgentPhoneNo) {
            this.travelAgentPhoneNo = travelAgentPhoneNo;
        }

        public String getTravelAgentAlternateMobileNo() {
            return travelAgentAlternateMobileNo;
        }

        public void setTravelAgentAlternateMobileNo(String travelAgentAlternateMobileNo) {
            this.travelAgentAlternateMobileNo = travelAgentAlternateMobileNo;
        }

        public String getTravelAgentEmail() {
            return travelAgentEmail;
        }

        public void setTravelAgentEmail(String travelAgentEmail) {
            this.travelAgentEmail = travelAgentEmail;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverMobileNo() {
            return driverMobileNo;
        }

        public void setDriverMobileNo(String driverMobileNo) {
            this.driverMobileNo = driverMobileNo;
        }

        public String getDriverAlternateMobileNo() {
            return driverAlternateMobileNo;
        }

        public void setDriverAlternateMobileNo(String driverAlternateMobileNo) {
            this.driverAlternateMobileNo = driverAlternateMobileNo;
        }

        public String getDriverEmailId() {
            return driverEmailId;
        }

        public void setDriverEmailId(String driverEmailId) {
            this.driverEmailId = driverEmailId;
        }

        public String getDutyStatus() {
            return dutyStatus;
        }

        public void setDutyStatus(String dutyStatus) {
            this.dutyStatus = dutyStatus;
        }

        public String getDriverImage() {
            return driverImage;
        }

        public void setDriverImage(String driverImage) {
            this.driverImage = driverImage;
        }

        public String getVehicleName() {
            return vehicleName;
        }

        public void setVehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
        }

        public String getVehiclePhoto() {
            return vehiclePhoto;
        }

        public void setVehiclePhoto(String vehiclePhoto) {
            this.vehiclePhoto = vehiclePhoto;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getPassengerCount() {
            return passengerCount;
        }

        public void setPassengerCount(String passengerCount) {
            this.passengerCount = passengerCount;
        }

        public String getRideType() {
            return rideType;
        }

        public void setRideType(String rideType) {
            this.rideType = rideType;
        }

        public String getRideTypeName() {
            return rideTypeName;
        }

        public void setRideTypeName(String rideTypeName) {
            this.rideTypeName = rideTypeName;
        }

        public String getModeOfPayment() {
            return modeOfPayment;
        }

        public void setModeOfPayment(String modeOfPayment) {
            this.modeOfPayment = modeOfPayment;
        }

        public String getModeOfPaymentName() {
            return modeOfPaymentName;
        }

        public void setModeOfPaymentName(String modeOfPaymentName) {
            this.modeOfPaymentName = modeOfPaymentName;
        }

        public String getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(String requestStatus) {
            this.requestStatus = requestStatus;
        }

        public String getRequestStatusName() {
            return requestStatusName;
        }

        public void setRequestStatusName(String requestStatusName) {
            this.requestStatusName = requestStatusName;
        }

        public String getUserRequestDate() {
            return userRequestDate;
        }

        public void setUserRequestDate(String userRequestDate) {
            this.userRequestDate = userRequestDate;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getUserContactNumber() {
            return userContactNumber;
        }

        public void setUserContactNumber(String userContactNumber) {
            this.userContactNumber = userContactNumber;
        }

        public String getUserAlternateNumber() {
            return userAlternateNumber;
        }

        public void setUserAlternateNumber(String userAlternateNumber) {
            this.userAlternateNumber = userAlternateNumber;
        }

        public String getUserBookingRemarks() {
            return userBookingRemarks;
        }

        public void setUserBookingRemarks(String userBookingRemarks) {
            this.userBookingRemarks = userBookingRemarks;
        }

        public String getEmployeeNumber() {
            return employeeNumber;
        }

        public void setEmployeeNumber(String employeeNumber) {
            this.employeeNumber = employeeNumber;
        }

        public String getTransportDetails() {
            return transportDetails;
        }

        public void setTransportDetails(String transportDetails) {
            this.transportDetails = transportDetails;
        }

        public String getBlock_id() {
            return block_id;
        }

        public void setBlock_id(String block_id) {
            this.block_id = block_id;
        }

        public String getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(String floor_id) {
            this.floor_id = floor_id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }
}
