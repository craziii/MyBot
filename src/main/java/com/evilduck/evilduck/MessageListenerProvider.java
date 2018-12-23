package com.evilduck.evilduck;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessageListenerProvider {

    @Bean
    public MessageListener messageListenerBean(final MessageChannel commandInputChannel) {
        return new MessageListener(commandInputChannel);
    }
}
