package com.evilduck.Repository;

import com.evilduck.Session.PollSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollSessionRepository extends MongoRepository<PollSession, String> {
}
