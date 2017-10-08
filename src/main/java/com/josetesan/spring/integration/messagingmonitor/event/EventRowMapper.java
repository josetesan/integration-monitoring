package com.josetesan.spring.integration.messagingmonitor.event;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        return Event.builder()
                .evento(resultSet.getString("evento"))
                .fecha(resultSet.getTimestamp("fecha"))
                .id(resultSet.getLong("id"))
                .sec(resultSet.getLong("sec"))
                .build();
    }
}
