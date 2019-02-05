package com.evilduck;

import com.evilduck.Configuration.MessageHandling.MessageGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerProvider {

    @Bean
    public MessageListener messageListenerBean(final MessageGateway messageGateway) {
        return new MessageListener(messageGateway);
    }
    
}
