package com.evilduck.Entity;

import com.google.common.base.CaseFormat;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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
        aliases.add(generateCamelCaseAlias(fullCommand));
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
        return commandString.matches(fullCommand) || aliases.stream().anyMatch(alias -> alias.matches(commandString));
    }

    @Override
    public String toString() {
        return "CommandDetail{" +
                "id='" + id + '\'' +
                ", fullCommand='" + fullCommand + '\'' +
                ", camelCaseAlias='" + camelCaseAlias + '\'' +
                ", description='" + description + '\'' +
                ", aliases=" + aliases +
                ", aliases='" + tutorial + '\'' +
                '}';
    }

}
