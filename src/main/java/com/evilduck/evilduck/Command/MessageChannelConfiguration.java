package com.evilduck.evilduck.Command;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageChannel;

public class MessageChannelConfiguration {

    @Bean
    public IntegrationFlow pingFlow() {
        return IntegrationFlowDefinition::log;
    }
}
