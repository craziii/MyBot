package com.evilduck.Repository;

import com.evilduck.Entity.BannedPhraseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannedPhraseRepository extends MongoRepository<BannedPhraseEntity, String> {

    BannedPhraseEntity findByIdLike(final String id);
}
