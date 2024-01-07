package com.example.fminder.helpers;

import java.time.LocalDateTime;

public class ErrorObj {

    private String message;
    private int status;
    private LocalDateTime dataAndTime;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDataAndTime(LocalDateTime dataAndTime) {
        this.dataAndTime = dataAndTime;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getDataAndTime() {
        return dataAndTime;
    }
}