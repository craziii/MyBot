package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.entity.Configuration;
import com.evilduck.enums.ConfigurationKeys;
import com.evilduck.repository.ConfigurationRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static net.dv8tion.jda.core.Permission.MANAGE_SERVER;

@IsACommand(description = "Used to change the prefix used for commands", tutorial = "Use !prefix and specify your prefix")
public class Prefix implements UnstableCommand, PrivateCommand {

    private final ConfigurationRepository configurationRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public Prefix(final ConfigurationRepository configurationRepository,
                  final CommandHelper commandHelper) {
        this.configurationRepository = configurationRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return requestingMember.hasPermission(MANAGE_SERVER);
    }

    @Override
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final Optional<Configuration> prefixConfig = configurationRepository.findById(ConfigurationKeys.PREFIX.toString());
        if (prefixConfig.isPresent() && prefixConfig.get().getValue().matches(args.get(0)))
            message.getTextChannel().sendMessage("That prefix has already been set!").queue();
        else {
            configurationRepository.save(new Configuration(ConfigurationKeys.PREFIX.toString(), args.get(0).trim()));
        }


    }


}
