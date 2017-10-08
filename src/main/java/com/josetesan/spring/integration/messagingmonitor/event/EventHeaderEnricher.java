package com.josetesan.spring.integration.messagingmonitor.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.messaging.Message;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;


@Service
public class EventHeaderEnricher {

    private String salt = null;
    private static final String password = "myS3cr3tPassw0rd";
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHeaderEnricher.class);

    private final GaugeService gaugeService;

    @Autowired
    public EventHeaderEnricher(GaugeService gaugeService) {
        this.gaugeService = gaugeService;
        this.salt = KeyGenerators.string().generateKey();
    }

    public void sign(HeaderEnricherSpec h) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        h.headerFunction("sign", this::signMessage);
        stopWatch.stop();

        gaugeService.submit("histogram.enricher", stopWatch.getLastTaskTimeMillis());
    }

    private Object signMessage(Message<Event> message) {
        final String sign =
                Encryptors
                .text(password,salt)
                .encrypt(String.valueOf(message.getPayload().hashCode()));
        LOGGER.info("Signed {}", sign );
        return sign;
    }

}
