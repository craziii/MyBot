package com.evilduck.Repository;

import com.evilduck.Entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BigDickRepository extends MongoRepository<Member, String> {

    Member findByName(final String name);

}
