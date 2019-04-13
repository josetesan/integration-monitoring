package com.josetesan.spring.integration.messagingmonitor;

import com.josetesan.spring.integration.messagingmonitor.event.EventHeaderEnricher;
import com.josetesan.spring.integration.messagingmonitor.event.EventProcessor;
import com.josetesan.spring.integration.messagingmonitor.event.EventRowMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

@Configuration
public class IntegrationConfiguration {


    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageSource<Object> jdbcMessageSource(DataSource datasource) {

        JdbcPollingChannelAdapter ms =
                new JdbcPollingChannelAdapter(datasource, "SELECT * FROM MOCK_DATA where PROCESSED is false");

        ms.setRowMapper(new EventRowMapper());
        ms.setUpdatePerRow(true);
        ms.setUpdateSql("UPDATE MOCK_DATA SET PROCESSED=TRUE");
        return ms;
    }

    @Bean
    public AmqpOutboundEndpoint amqpOutboundEndpoint( AmqpTemplate amqpTemplate) {

        AmqpOutboundEndpoint endpoint = new AmqpOutboundEndpoint(amqpTemplate);
        endpoint.setExchangeName("josetest");
        endpoint.setRequiresReply(false);
        endpoint.setRoutingKey("#");
        endpoint.setOutputChannel(outputChannel());
        return endpoint;
    }

    @Bean
    public IntegrationFlow pollingFlow(DataSource datasource,EventProcessor eventProcessor,EventHeaderEnricher eventHeaderEnricher,AmqpTemplate amqpTemplate) {
        return IntegrationFlows
                .from(jdbcMessageSource(datasource),
                        c -> c.poller(Pollers.fixedRate(100).maxMessagesPerPoll(20)))
                .channel(inputChannel())
                .split()
                .enrichHeaders(eventHeaderEnricher::sign)
                .handle(eventProcessor)
                .aggregate(a -> a.releaseStrategy( g-> g.size()==10))
                .handle(amqpOutboundEndpoint(amqpTemplate))
                .get();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler  task = new ThreadPoolTaskScheduler();
        task.setPoolSize(16);
        return task;
    }

}
