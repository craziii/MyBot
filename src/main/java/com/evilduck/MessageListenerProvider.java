package com.evilduck;

import com.evilduck.Configuration.MessageHandling.CommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerProvider {

    @Bean
    public MessageListener messageListenerBean(final CommandGateway commandGateway) {
        return new MessageListener(commandGateway);
    }
    
}
