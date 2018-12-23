package com.evilduck.evilduck;

import com.evilduck.evilduck.Command.CommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessageListenerProvider {

    @Bean
    public MessageListener messageListenerBean(final CommandGateway commandGateway) {
        return new MessageListener(commandGateway);
    }
}
