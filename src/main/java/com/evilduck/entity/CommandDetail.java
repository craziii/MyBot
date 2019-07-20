package com.evilduck.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.CaseFormat;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static java.util.Arrays.asList;

@JsonSerialize
public class CommandDetail {

    @Id
    public String id;

    private String fullCommand;
    private String camelCaseAlias;
    private String description;
    private List<String> aliases;
    private String tutorial;

    public CommandDetail(final String fullCommand) {
        this.fullCommand = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fullCommand);
        aliases = new ArrayList<>();
//        aliases.add(generateCamelCaseAlias(fullCommand));
    }

    public String getFullCommand() {
        return fullCommand;
    }

    public void setFullCommand(final String fullCommand) {
        this.fullCommand = fullCommand;
    }

    public String getCamelCaseAlias() {
        return camelCaseAlias;
    }

    public void setCamelCaseAlias(final String camelCaseAlias) {
        this.camelCaseAlias = camelCaseAlias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(final String tutorial) {
        this.tutorial = tutorial;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(final List<String> aliases) {
        this.aliases = aliases;
    }

    public void generateCamelCaseAlias() {
        aliases.add(generateCamelCaseAlias(fullCommand));
    }

    private String generateCamelCaseAlias(final String rawCommand) {
        final String snakeCaseCommand = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rawCommand);
        final List<Character> firstLetters = new ArrayList<>();
        asList(snakeCaseCommand.split("_")).forEach(word -> firstLetters.add(word.charAt(0)));

        final StringBuilder firstLettersAliasBuilder = new StringBuilder();
        firstLetters.forEach(firstLettersAliasBuilder::append);
        return firstLettersAliasBuilder.toString();
    }

    public boolean commandStringMatches(final String commandString) {
        return commandString.toUpperCase().matches(fullCommand.toUpperCase()) || aliases.stream()
                .anyMatch(alias -> {
                    try {
                        return alias.toLowerCase().matches(commandString.toLowerCase());
                    } catch (PatternSyntaxException e) {
                        LoggerFactory.getLogger(CommandDetail.class)
                                .warn("Could not perform regex matching on input! Message: {}, Pattern: {}",
                                        e.getMessage(),
                                        e.getPattern());
                    }
                    return false;
                });
    }

    @Override
    public String toString() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return "CommandDetail: " + objectMapper.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Command Serialize Issue";
    }


}
