package com.evilduck.repository;

import com.evilduck.entity.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import static com.evilduck.Enum.ConfigurationKeys.PREFIX;

@org.springframework.context.annotation.Configuration
public class SimpleConfigurationRepository {

    private final ConfigurationRepository repository;

    @Autowired
    public SimpleConfigurationRepository(final ConfigurationRepository repository) {
        this.repository = repository;
    }

    public String getPrefix() {
        return repository.findById(PREFIX.toString())
                .map(Configuration::getValue)
                .orElse("!");
    }

}
