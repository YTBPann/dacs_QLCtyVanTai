package com.ytbpann.quanlyvantai.location.dto;

import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.location.entity.LocationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class LocationForm {

    @NotBlank(message = "Mã địa điểm không được để trống")
    @Size(max = 20, message = "Mã địa điểm tối đa 20 ký tự")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]+$",
            message = "Mã địa điểm chỉ được chứa chữ, số, dấu gạch ngang hoặc gạch dưới"
    )
    private String code;

    @NotBlank(message = "Tên địa điểm không được để trống")
    @Size(max = 150, message = "Tên địa điểm tối đa 150 ký tự")
    private String name;

    @NotNull(message = "Vui lòng chọn loại địa điểm")
    private LocationType type;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @NotBlank(message = "Tỉnh/thành không được để trống")
    @Size(max = 100, message = "Tỉnh/thành tối đa 100 ký tự")
    private String province;

    @NotNull(message = "Latitude không được để trống")
    @DecimalMin(value = "-90.0", message = "Latitude phải lớn hơn hoặc bằng -90")
    @DecimalMax(value = "90.0", message = "Latitude phải nhỏ hơn hoặc bằng 90")
    private BigDecimal latitude;

    @NotNull(message = "Longitude không được để trống")
    @DecimalMin(value = "-180.0", message = "Longitude phải lớn hơn hoặc bằng -180")
    @DecimalMax(value = "180.0", message = "Longitude phải nhỏ hơn hoặc bằng 180")
    private BigDecimal longitude;

    @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
    private String note;

    public static LocationForm fromEntity(LocationPoint locationPoint) {
        LocationForm form = new LocationForm();
        form.setCode(locationPoint.getCode());
        form.setName(locationPoint.getName());
        form.setType(locationPoint.getType());
        form.setAddress(locationPoint.getAddress());
        form.setProvince(locationPoint.getProvince());
        form.setLatitude(locationPoint.getLatitude());
        form.setLongitude(locationPoint.getLongitude());
        form.setNote(locationPoint.getNote());
        return form;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = normalizeText(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalizeText(name);
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = normalizeText(address);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = normalizeText(province);
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = normalizeText(note);
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}