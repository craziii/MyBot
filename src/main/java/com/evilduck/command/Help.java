package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.CommandDetailRepository;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@IsACommand(description = "Displays information about every command this bot has", aliases = "Use \'!h\' or \'!help\' to use")
public class Help implements PublicCommand {

    private final CommandDetailRepository commandDetailRepository;

    @Autowired
    public Help(final CommandDetailRepository commandDetailRepository) {
        this.commandDetailRepository = commandDetailRepository;
    }

    @Override
    @ServiceActivator(inputChannel = "helpChannel")
    public void execute(final Message message) throws IOException {
        final List<CommandDetail> commandDetailList = commandDetailRepository.findAll();

        final EmbedBuilder helpEmbed = new EmbedBuilder()
                .setTitle("command Detail List");

        commandDetailList.forEach(commandDetail ->
                helpEmbed.addField(commandDetail.getFullCommand(), commandDetail.getDescription(), true)
                        .addField("Ways to call", commandDetail.getTutorial(), true)
                        .addBlankField(false));

        final MessageEmbed build = helpEmbed.build();
        message.getTextChannel().sendMessage(build).queue();

    }
}
