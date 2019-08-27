package com.evilduck.command.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IsACommand {

    String description() default "No Description Available";

    String tutorial() default "No Tutorial Available";

    String[] aliases() default "No Aliases";

    boolean management() default false;

    boolean callable() default true;

}
