package com.evilduck.Repository;

import com.evilduck.Entity.CommandDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandDetailRepository extends MongoRepository<CommandDetail, String> {

    CommandDetail findOneByFullCommand(final String fullCommand);

}
