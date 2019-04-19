package com.evilduck.util;

import com.evilduck.repository.BigDickRepository;
import com.evilduck.repository.CommandDetailRepository;

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
