package com.evilduck;

import com.evilduck.Command.Tools.IsACommand;
import com.evilduck.Entity.CommandDetail;
import com.evilduck.Repository.CommandDetailRepository;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private final Environment environment;
    private final CommandDetailRepository commandDetailRepository;
    private final String commandPackagePath;
    private final MessageChannelConfiguration messageChannelConfiguration;

    @Autowired
    public Starter(final Environment environment,
                   final CommandDetailRepository commandDetailRepository,
                   @Value("${command.package}") final String commandPackagePath,
                   final MessageChannelConfiguration messageChannelConfiguration) {
        this.environment = environment;
        this.commandDetailRepository = commandDetailRepository;
        this.commandPackagePath = commandPackagePath;
        this.messageChannelConfiguration = messageChannelConfiguration;
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
        final Reflections reflections = new Reflections(commandPackagePath);
        final Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(IsACommand.class);
        final List<CommandDetail> commandDetailList = new ArrayList<>();
        commandClasses.forEach(commandClass -> {
            final Optional<IsACommand> isACommand = ofNullable(commandClass.getAnnotation(IsACommand.class));
            if (isACommand.isPresent()) {
                final String commandName = commandClass.getSimpleName();
                final CommandDetail commandDetail = new CommandDetail(commandName.toLowerCase().charAt(0) + commandName.substring(1));
                commandDetail.setDescription(isACommand.get().description());

                commandDetailList.add(commandDetail);
            }
        });

        commandDetailRepository.deleteAll();
        commandDetailRepository.saveAll(commandDetailList);
    }


    private static String loadStartupText() {
        final Try<String> introAsciiArt = TryFactory.attempt(() -> new Scanner(ResourceUtils.getFile("classpath:jeff_intro.txt")).useDelimiter("\\Z").next()).orElse(null);
        return introAsciiArt.isSuccess() ? introAsciiArt.get() : "";
    }


}
