package com.evilduck.repository;

import com.evilduck.entity.CommandDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandDetailRepository extends MongoRepository<CommandDetail, String> {

    CommandDetail findOneByFullCommand(final String fullCommand);

}
