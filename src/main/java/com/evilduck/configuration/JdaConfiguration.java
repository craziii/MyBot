package com.evilduck.configuration;

import com.evilduck.MessageListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.core.AccountType.BOT;

@Configuration
public class JdaConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdaConfiguration.class);

    @Bean
    public JDA jda(final MessageListener messageListener,
                   @Value("${server.bot.token}") final String token) {
        try {
            return new JDABuilder(BOT)
                    .setToken(token)
                    .addEventListener(messageListener)
                    .buildAsync();
        } catch (final LoginException e) {
            LOGGER.error("Unable to build JDABuilder, cause,\n{}",
                    e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
