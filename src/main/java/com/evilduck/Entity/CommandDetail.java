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
    private List<String> aliases;

    public CommandDetail(final String fullCommand) {
        this.fullCommand = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fullCommand);
        aliases = new ArrayList<>();
    }

    public String getFullCommand() {
        return fullCommand;
    }

    public void setFullCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    public List<String> getAliases() {
        generateCamelCaseAlias();
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void generateCamelCaseAlias() {
        final String snakeCaseCommand = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fullCommand);
        final List<Character> firstLetters = new ArrayList<>();
        asList(snakeCaseCommand.split("_")).forEach(word -> firstLetters.add(word.charAt(0)));
        final StringBuilder firstLettersAliasBuilder = new StringBuilder();
        firstLetters.forEach(firstLettersAliasBuilder::append);
        aliases.add(firstLettersAliasBuilder.toString());
    }

    @Override
    public String toString() {
        return "CommandDetail{" +
                "id='" + id + '\'' +
                ", fullCommand='" + fullCommand + '\'' +
                ", aliases=" + aliases +
                '}';
    }
}
