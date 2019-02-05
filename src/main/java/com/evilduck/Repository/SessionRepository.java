package com.evilduck.Repository;

import com.evilduck.Entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}
