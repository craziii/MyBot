package com.evilduck.repository;

import com.evilduck.entity.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import static com.evilduck.Enum.ConfigurationKeys.PREFIX;

@org.springframework.context.annotation.Configuration
public class SimpleConfigurationRepository {

    private final ConfigurationRepository repository;

    @Autowired
    public SimpleConfigurationRepository(final ConfigurationRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "prefix", key = "#key")
    public String getPrefix() {
        return repository.findById(PREFIX.toString())
                .map(Configuration::getValue)
                .orElse("!");
    }

}
