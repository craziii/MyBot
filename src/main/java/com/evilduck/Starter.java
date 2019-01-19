package com.evilduck;

import com.evilduck.Entity.CommandDetail;
import com.evilduck.Repository.CommandDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

@Component
public final class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private final Environment environment;
    private final CommandDetailRepository mongoRepository;
    private final String commandPackage;

    @Autowired
    public Starter(final Environment environment,
                   final CommandDetailRepository mongoRepository,
                   @Value("${command.package}")final String commandPackage) {
        this.environment = environment;
        this.mongoRepository = mongoRepository;
        this.commandPackage = commandPackage;
    }

    @PostConstruct
    public void init() throws IOException {
        final StringBuilder outputListString = new StringBuilder();
        for (final String defaultProfile : environment.getDefaultProfiles())
            outputListString.append(format("%s\n", defaultProfile));
        LOGGER.info("Environments:\n{}", outputListString.toString());

        setupCommandsRepo();
    }

    private void setupCommandsRepo() throws IOException {
        final Enumeration resources = currentThread()
                .getContextClassLoader()
                .getResources(commandPackage.replace(".", "/"));

        final List<CommandDetail> commandDetailList = new ArrayList<>();

        while(resources.hasMoreElements()) {
            final File shouldBeDirectory = new File(((URL) resources.nextElement()).getFile());

            if (shouldBeDirectory.isDirectory()) {
                final File[] files = shouldBeDirectory.listFiles();
                assert files != null;
                for (final File file : files) {
                    commandDetailList.add(
                            new CommandDetail(file.getName().replace(".class", ""))
                    );
                }
            }
            commandDetailList.forEach(CommandDetail::generateCamelCaseAlias);
            mongoRepository.deleteAll();
            mongoRepository.saveAll(commandDetailList);
        }

    }

}
