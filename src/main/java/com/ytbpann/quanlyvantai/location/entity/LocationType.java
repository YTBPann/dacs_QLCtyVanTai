package com.ytbpann.quanlyvantai.location.entity;

public enum LocationType {
    PICKUP_POINT("Điểm lấy hàng"),
    DELIVERY_POINT("Điểm giao hàng"),
    BOTH("Lấy/Giao hàng");

    private final String displayName;

    LocationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}