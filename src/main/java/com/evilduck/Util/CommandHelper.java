package com.evilduck.Util;

import com.evilduck.Entity.CommandDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public class CommandHelper {

    private List commandDetailList;
    private final MongoRepository mongoRepository;


    @Autowired
    public CommandHelper(final MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    public List<CommandDetail> getCommandDetailList() {
        if (commandDetailList == null || commandDetailList.size() == 0)
            commandDetailList = mongoRepository.findAll();
        return commandDetailList;
    }

}
