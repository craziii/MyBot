package com.evilduck.repository;

import com.evilduck.entity.AudioContextState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AudioContextStateRepository extends MongoRepository<AudioContextState, String> {
}
