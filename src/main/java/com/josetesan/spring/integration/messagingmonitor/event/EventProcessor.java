package com.josetesan.spring.integration.messagingmonitor.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class EventProcessor  {

    private final GaugeService gaugeService;
    private final CounterService counterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

    public EventProcessor(GaugeService gaugeService,CounterService counterService) {

        this.gaugeService = gaugeService;
        this.counterService = counterService;

    }


    @ServiceActivator
    public void process(Message<Event> message) throws Exception{
        counterService.increment("counter.eventprocessor");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep((long)(Math.random()*5)*1000);
            LOGGER.info("Fecha es {}",message.getPayload().getFecha());
        } finally {
            stopWatch.stop();
            this.gaugeService.submit("histogram.eventprocessor",stopWatch.getLastTaskTimeMillis());
        }
    }
}
