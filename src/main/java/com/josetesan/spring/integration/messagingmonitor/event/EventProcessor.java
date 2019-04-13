package com.josetesan.spring.integration.messagingmonitor.event;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EventProcessor  {

    private Counter counter;
    private Timer timer;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

    public EventProcessor(MeterRegistry registry) {

        counter = registry.counter("counter.eventprocessor");
        timer = registry.timer("timer.eventprocessor");
    }


    @ServiceActivator
    public void process(Message<Event> message) {

        counter.increment(1.0);
        timer.record(()-> {
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 5) * 1000);
                LOGGER.info("Fecha es {}", message.getPayload().getFecha());
            }  catch (Exception e) {
                LOGGER.error("Exception thrown ",e);
            }
        });
    }
}
