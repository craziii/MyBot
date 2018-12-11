package com.evilduck.evilduck.Command;

import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.stereotype.Component;

@Component
public class CommandRouter {

    @Router
    public IntegrationFlow commandFlow() {
        return f -> f.handle()
    }


}
