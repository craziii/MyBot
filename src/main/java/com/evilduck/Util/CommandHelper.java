package com.evilduck.Util;

import com.evilduck.Entity.CommandDetail;
import com.evilduck.Repository.CommandDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

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
        return asList(rawContent.split(" "));
    }

}
