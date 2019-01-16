package com.evilduck.evilduck;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class MessageChannelConfiguration {

    @Bean
    public MessageChannel commandInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel pingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel penisChannel() {
        return new DirectChannel();
    }
}
