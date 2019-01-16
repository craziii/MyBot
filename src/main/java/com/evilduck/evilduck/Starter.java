package com.evilduck.evilduck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.lang.String.format;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private final Environment environment;

    @Autowired
    public Starter(final Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        final StringBuilder outputListString = new StringBuilder();
        for (final String defaultProfile : environment.getDefaultProfiles())
            outputListString.append(format("%s\n", defaultProfile));
        LOGGER.info("Environments:\n{}", outputListString.toString());


    }

}
