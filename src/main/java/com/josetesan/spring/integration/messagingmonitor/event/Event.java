package com.josetesan.spring.integration.messagingmonitor.event;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@Builder
public class Event implements Serializable {

    private Long id;
    private Long sec;
    private String evento;
    private Date fecha;

}
