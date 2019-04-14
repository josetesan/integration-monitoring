package com.josetesan.spring.integration.messagingmonitor.event;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
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
    private DistributionSummary summary;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

    public EventProcessor(MeterRegistry registry) {

        counter = registry.counter("messages.processed","messages","processed","app","event");
        timer = registry.timer("message.processed","message","processed","app","event");
        summary = registry.summary("messages.summary","message","summary","what","who");
    }


    @ServiceActivator
    public void process(Message<Event> message) {


        counter.increment(1.0);
        timer.record(()-> {
            try {
                summary.takeSnapshot();
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 5) * 100);
                LOGGER.info("Fecha es {}", message.getPayload().getFecha());
            }  catch (Exception e) {
                LOGGER.error("Exception thrown ",e);
            }
        });
    }
}
