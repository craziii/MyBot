package com.evilduck.repository;

import com.evilduck.session.PollSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollSessionRepository extends MongoRepository<PollSession, String> {
}
