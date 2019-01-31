package com.evilduck.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannedPhraseRepository extends MongoRepository<String, String> {
}
