package com.evilduck.repository;

import com.evilduck.entity.BannedPhraseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannedPhraseRepository extends MongoRepository<BannedPhraseEntity, String> {

    BannedPhraseEntity findByIdLike(final String id);
}
