package com.ytbpann.quanlyvantai.trip.service;

import com.ytbpann.quanlyvantai.trip.dto.TripGpsRoutePointResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TripGpsRouteService {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    public TripGpsRouteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TripGpsRoutePointResponse> findRoutePointsByTripId(Long tripId) {
        String sql = """
                SELECT
                    id,
                    trip_id,
                    driver_profile_id,
                    latitude,
                    longitude,
                    accuracy_meter,
                    speed_meter_per_second,
                    heading_degree,
                    source,
                    recorded_at
                FROM gps_location_logs
                WHERE trip_id = ?
                  AND latitude IS NOT NULL
                  AND longitude IS NOT NULL
                ORDER BY recorded_at ASC, id ASC
                """;

        return jdbcTemplate.query(sql, this::mapRow, tripId);
    }

    private TripGpsRoutePointResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TripGpsRoutePointResponse(
                rs.getLong("id"),
                getNullableLong(rs, "trip_id"),
                getNullableLong(rs, "driver_profile_id"),
                getNullableDouble(rs, "latitude"),
                getNullableDouble(rs, "longitude"),
                getNullableDouble(rs, "accuracy_meter"),
                getNullableDouble(rs, "speed_meter_per_second"),
                getNullableDouble(rs, "heading_degree"),
                rs.getString("source"),
                formatTimestamp(rs.getTimestamp("recorded_at"))
        );
    }

    private Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    private Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "-";
        }

        return timestamp.toLocalDateTime().format(DISPLAY_FORMATTER);
    }
}