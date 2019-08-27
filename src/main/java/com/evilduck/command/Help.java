package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.CommandDetailRepository;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@IsACommand(
        tutorial = "Use !help",
        description = "Displays information about every command this bot has",
        aliases = "h")
public class Help implements PublicCommand {

    private final CommandDetailRepository commandDetailRepository;

    @Autowired
    public Help(final CommandDetailRepository commandDetailRepository) {
        this.commandDetailRepository = commandDetailRepository;
    }

    @Override
    @ServiceActivator(inputChannel = "helpChannel")
    public void execute(final Message message) {
        final List<CommandDetail> commandDetailList = commandDetailRepository.findAll();
        final int noOfCommands = commandDetailList.size();
        for (int nextStepStart = 0; nextStepStart < noOfCommands; nextStepStart += 4) {
            final int nextStepEnd = (nextStepStart + 4);
            final List<CommandDetail> nextEmbedGroup = commandDetailList.subList(
                    nextStepStart,
                    (nextStepEnd > noOfCommands) ? noOfCommands : nextStepEnd);
            final EmbedBuilder helpEmbed = new EmbedBuilder()
                    .setTitle(String.format("%d | JeffBot Command List!", nextStepEnd / 4));
            nextEmbedGroup.forEach(addCommandDetailToEmbed(helpEmbed));
            final MessageEmbed build = helpEmbed.build();
            message.getTextChannel().sendMessage(build).queue();
        }
    }

    private static Consumer<CommandDetail> addCommandDetailToEmbed(final EmbedBuilder helpEmbed) {
        return commandDetail -> helpEmbed.addField(":question: " + commandDetail.getFullCommand(), commandDetail.getTutorial(), false)
                .addField("What it Does", commandDetail.getDescription() +
                        "\n :heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign:", false);
    }
}
