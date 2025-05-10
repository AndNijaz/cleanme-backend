package com.cleanme.dto;

import java.util.UUID;

public class ReservationDto {
    private final UUID rid;
    private String date;
    private String time;
    private String location;
    private String status;
    private String comment;
    private String cleanerName;

    public ReservationDto(UUID rid, String date, String time, String location, String status, String comment, String cleanerName) {
        this.rid = rid;
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

    public UUID getRid() {
        return rid;
    }
}
