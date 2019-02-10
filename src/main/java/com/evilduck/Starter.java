package com.evilduck;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Entity.CommandDetail;
import com.evilduck.Repository.CommandDetailRepository;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import net.dv8tion.jda.core.JDA;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.springframework.util.ResourceUtils.getFile;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    private final Environment environment;
    private final CommandDetailRepository commandDetailRepository;
    private final String commandPackagePath;
    private final MessageChannelConfiguration messageChannelConfiguration;
    private final JDA jda;

    @Autowired
    public Starter(final Environment environment,
                   final CommandDetailRepository commandDetailRepository,
                   @Value("${command.package}") final String commandPackagePath,
                   final MessageChannelConfiguration messageChannelConfiguration,
                   final JDA jda) {
        this.environment = environment;
        this.commandDetailRepository = commandDetailRepository;
        this.commandPackagePath = commandPackagePath;
        this.messageChannelConfiguration = messageChannelConfiguration;
        this.jda = jda;
    }

    @PostConstruct
    public void init() throws IOException {

        LOGGER.info("JeffBot is starting...");
        System.out.println(loadStartupText());

        messageChannelConfiguration.instantiateMessageChannels();

        final StringBuilder outputListString = new StringBuilder();
        for (final String defaultProfile : environment.getDefaultProfiles())
            outputListString.append(format("%s\n", defaultProfile));
        LOGGER.info("Environments:\n{}", outputListString.toString());

        getCommandClasses();
    }

    private void getCommandClasses() {
        System.out.println("\n ========== ========== ========== ========== ==========\n");
        LOGGER.info("Loading Command Classes from path: \'{}\'", commandPackagePath);

        final Set<Class<?>> commandClasses = new Reflections(commandPackagePath).getTypesAnnotatedWith(IsACommand.class);
        final List<CommandDetail> commandDetailList = new ArrayList<>();

        commandClasses.forEach(commandClass -> {
            final Optional<IsACommand> isACommand = ofNullable(commandClass.getAnnotation(IsACommand.class));
            if (isACommand.isPresent()) {
                final String commandName = commandClass.getSimpleName();
                final CommandDetail commandDetail = new CommandDetail(commandName.toLowerCase().charAt(0) + commandName.substring(1));
                commandDetail.setDescription(isACommand.get().description());

                LOGGER.info("Found command \'{}\' from class, generated aliases: \'{}\'",
                        commandDetail.getFullCommand(),
                        commandDetail.getAliases());

                commandDetailList.add(commandDetail);
            }
        });

        LOGGER.info("Saving all commands to CommandDetailRepository");
        commandDetailRepository.deleteAll();
        commandDetailRepository.saveAll(commandDetailList);

        System.out.println("\n ========== ========== ========== ========== ==========\n");
    }

    private static String loadStartupText() {
        final Try<String> introAsciiArt = TryFactory.attempt(() ->
                new Scanner(getFile("classpath:jeff_intro.txt"))
                        .useDelimiter("\\Z").next())
                .orElse(null);
        return introAsciiArt.isSuccess() ? introAsciiArt.get() : "";
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("JeffBot is Shutting down...");
    }

}
