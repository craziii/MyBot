package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.entity.SessionEntity;
import com.evilduck.repository.SessionRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
@IsACommand(description = "This is MY fucking command, don't TOUCH!", tutorial = "What did I say mofo?!")
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

        final Optional<SessionEntity> session = getSessionIfExists(message.getMember());
        if (session.isPresent()) {
            message.getTextChannel()
                    .sendMessage("I have found a session for you!\nYou saved: " +
                            session.get().getNextStep())
                    .queue();
            sessionRepository.deleteById(session.get().getAuthorId());
        } else {
        }

    }

    @NotNull
    private Optional<SessionEntity> getSessionIfExists(final Member member) {
        return sessionRepository.findById(member.getUser().getId());
    }

}
