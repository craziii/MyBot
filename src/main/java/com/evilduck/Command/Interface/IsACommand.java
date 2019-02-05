package com.evilduck.Command.Interface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IsACommand {

    String description() default "";

    boolean management() default false;

    boolean automatic() default false;

    boolean manual() default true;


}
