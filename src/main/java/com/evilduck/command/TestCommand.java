package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PrivateCommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.command.standards.UnstableCommand;
import com.evilduck.entity.SessionEntity;
import com.evilduck.repository.SessionRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Component
@IsACommand
public class TestCommand implements PublicCommand, UnstableCommand, PrivateCommand {    // DON'T TRY THIS AT HOME KIDS!

    private final SessionRepository sessionRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public TestCommand(final SessionRepository sessionRepository,
                       final CommandHelper commandHelper) {
        this.sessionRepository = sessionRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }

    @Override
    @ServiceActivator(inputChannel = "testCommandChannel")
    public void execute(final Message message) {

        final String rawMessage = message.getContentRaw();
        final List<String> args = commandHelper.getArgs(rawMessage);

        final Optional<SessionEntity> session = getSessionIfExists(message.getMember());
        if (session.isPresent()) {
            message.getTextChannel()
                    .sendMessage("I have found a session for you!\nYou saved: " +
                            session.get().getSavedDetail())
                    .queue();
            sessionRepository.deleteById(session.get().getId());
        } else {
            if (args.get(0).equalsIgnoreCase("save")) sessionRepository.save(new SessionEntity(
                    message.getMember().getUser().getId(),
                    commandHelper.getArgsAsString(rawMessage, 2)
            ));
        }

    }

    @NotNull
    private Optional<SessionEntity> getSessionIfExists(final Member member) {
        return sessionRepository.findById(member.getUser().getId());
    }

}
