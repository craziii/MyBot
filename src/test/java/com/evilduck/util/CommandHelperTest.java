package com.evilduck.util;

import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.CommandDetailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandHelperTest {

    @Mock
    private CommandDetailRepository commandDetailRepository;

    @InjectMocks
    private CommandHelper commandHelper;

    @Test
    public void getCommandDetailList() {
        when(commandDetailRepository.findAll()).thenReturn(singletonList(new CommandDetail("command")));
        assertThat(commandHelper.getCommandDetailList().get(0).getFullCommand(),
                is("command"));

    }

    @Test
    public void getArgs() {
        final List<String> args = commandHelper.getArgs("!command arg1 arg2");
        assertThat(args, hasItems("arg1", "arg2"));
    }

    @Test
    public void getArgsAsString() {
    }

    @Test
    public void getArgsAsString1() {
    }

    @Test
    public void getArgsAsAString() {
    }

    @Test
    public void getArgsAsString2() {
    }

    @Test
    public void matchCommandString() {
    }
}