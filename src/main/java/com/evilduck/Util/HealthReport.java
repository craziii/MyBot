package com.evilduck.Util;

import net.dv8tion.jda.core.JDA;
import org.joda.time.DateTime;
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

    @Scheduled(fixedRate = 60000)
    public void healthReport() {
        LOGGER.info("\n\n");
        LOGGER.info("Health Report:\t{}", new PrettyDate(DateTime.now()));
        LOGGER.info("Ping:\t\t\t{}ms", jda.getPing());
    }

}
