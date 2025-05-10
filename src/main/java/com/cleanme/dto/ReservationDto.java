package com.cleanme.dto;

public class ReservationDto {
    private String date;
    private String time;
    private String location;
    private String status;
    private String comment;
    private String cleanerName;

    public ReservationDto(String date, String time, String location, String status, String comment, String cleanerName) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.status = status;
        this.comment = comment;
        this.cleanerName = cleanerName;
    }


    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getCleanerName() {
        return cleanerName;
    }
}
