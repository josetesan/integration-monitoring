package com.josetesan.spring.integration.messagingmonitor.event;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.messaging.Message;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;


@Service
public class EventHeaderEnricher {

    private String salt = null;
    private Timer timer;
    private static final String password = "myS3cr3tPassw0rd";
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHeaderEnricher.class);


    @Autowired
    public EventHeaderEnricher(MeterRegistry registry) {
        this.salt = KeyGenerators.string().generateKey();
        this.timer = registry.timer("timer.sign","timer=event");
    }

    public void sign(HeaderEnricherSpec h) {
        this.timer.record(()->h.headerFunction("sign", this::signMessage));
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
