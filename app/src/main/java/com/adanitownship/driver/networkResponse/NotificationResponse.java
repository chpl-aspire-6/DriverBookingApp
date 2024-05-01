package com.adanitownship.driver.networkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {

    @SerializedName("notification")
    @Expose
    private List<Notification> notification;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Notification {

        @SerializedName("notification_id")
        @Expose
        private String notificationId;
        @SerializedName("driver_id")
        @Expose
        private String driverId;
        @SerializedName("society_id")
        @Expose
        private String societyId;
        @SerializedName("notification_tile")
        @Expose
        private String notificationTile;
        @SerializedName("notification_description")
        @Expose
        private String notificationDescription;
        @SerializedName("notification_status")
        @Expose
        private String notificationStatus;
        @SerializedName("notification_action")
        @Expose
        private String notificationAction;
        @SerializedName("read_status")
        @Expose
        private String readStatus;
        @SerializedName("notification_logo")
        @Expose
        private String notificationLogo;

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getSocietyId() {
            return societyId;
        }

        public void setSocietyId(String societyId) {
            this.societyId = societyId;
        }

        public String getNotificationTile() {
            return notificationTile;
        }

        public void setNotificationTile(String notificationTile) {
            this.notificationTile = notificationTile;
        }

        public String getNotificationDescription() {
            return notificationDescription;
        }

        public void setNotificationDescription(String notificationDescription) {
            this.notificationDescription = notificationDescription;
        }

        public String getNotificationStatus() {
            return notificationStatus;
        }

        public void setNotificationStatus(String notificationStatus) {
            this.notificationStatus = notificationStatus;
        }

        public String getNotificationAction() {
            return notificationAction;
        }

        public void setNotificationAction(String notificationAction) {
            this.notificationAction = notificationAction;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }

        public String getNotificationLogo() {
            return notificationLogo;
        }

        public void setNotificationLogo(String notificationLogo) {
            this.notificationLogo = notificationLogo;
        }

    }

}
