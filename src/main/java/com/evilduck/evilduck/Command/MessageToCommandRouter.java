package com.evilduck.evilduck.Command;

import org.springframework.integration.router.HeaderValueRouter;

public class MessageToCommandRouter extends HeaderValueRouter {

    public MessageToCommandRouter(final String headerName) {
        super(headerName);
    }
}
