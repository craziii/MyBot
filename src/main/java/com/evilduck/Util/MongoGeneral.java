package com.evilduck.Util;

import com.evilduck.Repository.BigDickRepository;
import com.evilduck.Repository.CommandDetailRepository;

public class MongoGeneral {

    private final BigDickRepository bigDickRepository;
    private final CommandDetailRepository commandDetailRepository;

    public MongoGeneral(final BigDickRepository bigDickRepository,
                        final CommandDetailRepository commandDetailRepository) {
        this.bigDickRepository = bigDickRepository;
        this.commandDetailRepository = commandDetailRepository;
    }

    public void clearAllRepositories() {
        bigDickRepository.deleteAll();
        commandDetailRepository.deleteAll();
    }

}
