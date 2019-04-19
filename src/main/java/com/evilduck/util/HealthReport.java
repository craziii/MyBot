package com.evilduck.util;

import net.dv8tion.jda.core.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthReport {

    private final static Logger LOGGER = LoggerFactory.getLogger(HealthReport.class);

    private final String token;
    private final JDA jda;

    @Autowired
    public HealthReport(@Value("${server.bot.token}") final String token,
                        final JDA jda) {
        this.token = token;
        this.jda = jda;
    }

    @Scheduled(fixedRate = 300000)
    public void healthReport() {
        final StringBuilder healthOutput = new StringBuilder()
                .append("Health Report:\n")
                .append(PrettyDate.now())
                .append("\nPing:\t\t\t")
                .append(jda.getPing())
                .append("ms");

        LOGGER.info(healthOutput.toString());
    }

}
