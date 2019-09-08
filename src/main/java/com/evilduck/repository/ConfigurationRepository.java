package com.evilduck.repository;

import com.evilduck.entity.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigurationRepository extends MongoRepository<Configuration, String> {

}
