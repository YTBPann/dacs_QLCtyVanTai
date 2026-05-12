package com.ytbpann.quanlyvantai.trip.entity;

public enum TripStatus {
    PLANNED("Đã lên kế hoạch"),
    IN_PROGRESS("Đang vận chuyển"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");

    private final String displayName;

    TripStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}