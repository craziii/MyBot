package com.evilduck.command.interfaces;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IsACommand {

    String description();

    String tutorial();

    String[] aliases() default "No Aliases";

    boolean management() default false;

    boolean callable() default true;

}
