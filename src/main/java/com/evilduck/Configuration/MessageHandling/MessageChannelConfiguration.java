package com.evilduck.Configuration.MessageHandling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
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
    public PublishSubscribeChannel autoFireCommandChannel() {
        return new PublishSubscribeChannel();
    }


}
