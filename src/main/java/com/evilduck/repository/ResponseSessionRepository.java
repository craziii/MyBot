package com.evilduck.repository;

import com.evilduck.session.ResponseSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResponseSessionRepository extends MongoRepository<ResponseSession, String> {
}
