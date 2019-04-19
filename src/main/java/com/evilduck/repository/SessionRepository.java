package com.evilduck.repository;

import com.evilduck.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}
