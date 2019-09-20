package com.evilduck;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.entity.BannedPhraseEntity;
import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.BannedPhraseRepository;
import com.evilduck.repository.CommandDetailRepository;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.springframework.util.ResourceUtils.getFile;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    private final Environment environment;
    private final CommandDetailRepository commandDetailRepository;
    private final BannedPhraseRepository bannedPhraseRepository;
    private final CacheableAudioContextProvider audioContextProvider;
    private final String commandPackagePath;
    private final JDA jda;

    @Autowired
    public Starter(final Environment environment,
                   final CommandDetailRepository commandDetailRepository,
                   final BannedPhraseRepository bannedPhraseRepository,
                   final CacheableAudioContextProvider audioContextProvider,
                   @Value("${command.package}") final String commandPackagePath,
                   final JDA jda) {
        this.environment = environment;
        this.commandDetailRepository = commandDetailRepository;
        this.bannedPhraseRepository = bannedPhraseRepository;
        this.audioContextProvider = audioContextProvider;
        this.commandPackagePath = commandPackagePath;
        this.jda = jda;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Deployment [15/09/2019 | 03:38]JeffBot is starting...");
        System.out.println(loadStartupText());
        final StringBuilder outputListString = new StringBuilder();
        for (final String defaultProfile : environment.getDefaultProfiles())
            outputListString.append(format("%s\n", defaultProfile));
        LOGGER.info("Environments:\n{}", outputListString.toString());

        saveCommandDetailsFromClasses();
        saveBannedPhrases();
    }

    @PreDestroy
    public void finishHooks() {
//        audioContextProvider.
//        jda.getGuilds().forEach(guild -> audioContextProvider.persistAudioContextStateForGuild(guild));
    }

    private void saveCommandDetailsFromClasses() {
        System.out.println("\n ========== ========== ========== ========== ==========\n");
        LOGGER.info("Loading command Classes from path: \'{}\'", commandPackagePath);

        final Set<Class<?>> commandClasses = new Reflections(commandPackagePath).getTypesAnnotatedWith(IsACommand.class);
        final List<CommandDetail> commandDetailList = new ArrayList<>();

        commandClasses.forEach(commandClass -> {
            final Optional<IsACommand> isACommand = ofNullable(commandClass.getAnnotation(IsACommand.class));
            if (isACommand.isPresent()) {
                final CommandDetail commandDetail = buildCommandDetail(commandClass, isACommand.get());
                commandDetailList.add(commandDetail);
            }
        });

        LOGGER.info("Saving all commands to CommandDetailRepository");
        commandDetailRepository.deleteAll();
        commandDetailRepository.saveAll(commandDetailList);

        System.out.println("\n ========== ========== ========== ========== ==========\n");
    }

    private void saveBannedPhrases() {
        final Optional<TextChannel> rulesChannel = jda.getTextChannels().stream()
                .filter(textChannel -> textChannel.getName().toLowerCase().matches("rules")).findFirst();
        if (rulesChannel.isPresent()) {
            final List<Message> messages = rulesChannel.map(textChannel -> textChannel.getPinnedMessages().complete()).get();
            final Optional<Message> bannedPhraseMessage = messages.stream().filter(message -> message.getContentRaw().toLowerCase().contains(":"))
                    .findFirst();
            if (bannedPhraseMessage.isPresent()) {
                final String bannedPhraseList = bannedPhraseMessage
                        .map(message -> message.getContentRaw().replace("```", "").substring(message.getContentRaw().indexOf(":") + 3)).get();
                final List<String> split = asList(bannedPhraseList.split("\n"));
                split.forEach(bannedPhrase -> bannedPhraseRepository.save(new BannedPhraseEntity(bannedPhrase.toLowerCase(), "testGuild")));
            }
        }
    }

    private static CommandDetail buildCommandDetail(final Class<?> commandClass,
                                                    final IsACommand isACommand) {
        final String commandName = commandClass.getSimpleName();
        final CommandDetail commandDetail = new CommandDetail(commandName.toLowerCase().charAt(0) + commandName.substring(1));
        commandDetail.setAliases(asList(isACommand.aliases()));
        commandDetail.setDescription(isACommand.description());
        commandDetail.setTutorial(isACommand.tutorial());

        LOGGER.info("Found command \'{}\' from class, generated aliases: \'{}\'",
                commandDetail.getFullCommand(),
                commandDetail.getAliases());
        return commandDetail;
    }

    private static String loadStartupText() {
        final Try<String> introAsciiArt = TryFactory.attempt(() ->
                new Scanner(getFile("classpath:jeff_intro.txt"))
                        .useDelimiter("\\Z").next());
        return introAsciiArt.isSuccess() ? introAsciiArt.get() : "";
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("JeffBot is Shutting down...");
    }

}
