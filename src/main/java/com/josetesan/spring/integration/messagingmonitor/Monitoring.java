package com.josetesan.spring.integration.messagingmonitor;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Monitoring {

    @Autowired
    private MeterRegistry registry;
}
