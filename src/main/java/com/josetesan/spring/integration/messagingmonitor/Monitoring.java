package com.josetesan.spring.integration.messagingmonitor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class Monitoring {

    @Bean
    GraphiteReporter graphiteReporter(
            @Value("${graphite.host}") String host,
            @Value("${graphite.port}") int port ,
            MetricRegistry metricRegistry)
    {

        GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry)
                .build(new Graphite(host,port));

        reporter.start(1, TimeUnit.SECONDS);
        return reporter;

    }
}
