package com.evilduck.util;

import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.CommandDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CommandHelper {

    private List commandDetailList;
    private final CommandDetailRepository commandDetailRepository;

    @Autowired
    public CommandHelper(final CommandDetailRepository commandDetailRepository) {
        this.commandDetailRepository = commandDetailRepository;
    }

    public List<CommandDetail> getCommandDetailList() {
        if (commandDetailList == null || commandDetailList.size() == 0)
            commandDetailList = commandDetailRepository.findAll();
        return commandDetailList;
    }

    public List<String> getArgs(final String rawContent) {
        final String cleanRawContent = rawContent.replaceAll("\\P{Print}", "");
        final List<String> args = new LinkedList<>(Arrays.asList(cleanRawContent.split("[ \t]+")));
        args.remove(args.get(0));
        return args;
    }

    public String getArgsAsString(final String rawContent,
                                  final int start,
                                  final int end) {
        final List<String> args = getArgs(rawContent);
        return getArgsAsString(args, start, end);
    }

    public String getArgsAsString(final String rawContent,
                                  final int start) {
        final List<String> args = getArgs(rawContent);
        final int end = args.size();
        return getArgsAsString(args, start, end);
    }

    public String getArgsAsAString(final List<String> args,
                                   final int start) {
        return getArgsAsString(args, start, args.size());
    }

    public String getArgsAsString(final List<String> args,
                                  final int start,
                                  final int end) {
        final StringBuilder argsStringBuilder = new StringBuilder();
        for (int i = start; i < end; i++) {
            argsStringBuilder.append(args.get(i))
                    .append(" ");
        }
        return argsStringBuilder.toString().trim();
    }

    public List<CommandDetail> matchCommandString(final String commandString) {

        final List<CommandDetail> commandDetailList = commandDetailRepository.findAll();

        return commandDetailList.stream().filter(commandDetail ->
                commandDetail.commandStringMatches(commandString)).collect(toList());

    }


}
