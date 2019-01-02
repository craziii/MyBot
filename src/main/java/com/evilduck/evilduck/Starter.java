package com.evilduck.evilduck;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

import static java.lang.String.format;
import static net.dv8tion.jda.core.AccountType.BOT;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private static final String TOKEN = "NTIxNzcxOTcwMzEyNzk4MjA4.DvBYaw.Vcj_1pknsezTXo_ZN2Nx8NKLER8";

    private final Environment environment;
    private final MessageListener messageListenerBean;

    @Autowired
    public Starter(final Environment environment,
                   final MessageListener messageListenerBean) {
        this.environment = environment;
        this.messageListenerBean = messageListenerBean;
    }

    @PostConstruct
    public void init() {
        final StringBuilder outputListString = new StringBuilder();
        for (final String defaultProfile : environment.getDefaultProfiles())
            outputListString.append(format("%s\n", defaultProfile));
        LOGGER.info("Environments:\n{}", outputListString.toString());

        final JDABuilder jdaBuilder = new JDABuilder(BOT)
                .setToken(TOKEN)
                .addEventListener(messageListenerBean);

        try {
            jdaBuilder.buildAsync();
        } catch (LoginException e) {
            LOGGER.error("Unable to build JDABuilder, cause:\n%s",
                    e.getMessage());
            e.printStackTrace();
        }
    }

}
