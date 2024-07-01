package com.adanitownship.driver.networkResponse;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LanguageResponse implements Serializable {

    @SerializedName("language")
    @Expose
    private List<Language> language = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;


    public List<Language> getLanguage() {
        return language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
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


    public class Language implements Serializable {

        @SerializedName("language_id")
        @Expose
        private String languageId;
        @SerializedName("language_name")
        @Expose
        private String languageName;
        @SerializedName("language_name_1")
        @Expose
        private String languageName1;
        @SerializedName("language_file")
        @Expose
        private String languageFile;
        @SerializedName("language_icon")
        @Expose
        private String languageIcon;
        @SerializedName("continue_btn_name")
        @Expose
        private String continueBtnName;
        private Drawable background;
        private String color;
        private boolean isClicked = false;

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public String getLanguageName1() {
            return languageName1;
        }

        public void setLanguageName1(String languageName1) {
            this.languageName1 = languageName1;
        }

        public String getLanguageId() {
            return languageId;
        }

        public void setLanguageId(String languageId) {
            this.languageId = languageId;
        }

        public String getLanguageName() {
            return languageName;
        }

        public void setLanguageName(String languageName) {
            this.languageName = languageName;
        }

        public String getLanguageFile() {
            return languageFile;
        }

        public void setLanguageFile(String languageFile) {
            this.languageFile = languageFile;
        }
        public Drawable getBackground() {
            return background;
        }

        public void setBackground(Drawable background) {
            this.background = background;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getContinueBtnName() {
            return continueBtnName;
        }

        public void setContinueBtnName(String continueBtnName) {
            this.continueBtnName = continueBtnName;
        }

        public String getLanguageIcon() {
            return languageIcon;
        }

        public void setLanguageIcon(String languageIcon) {
            this.languageIcon = languageIcon;
        }
    }
}
