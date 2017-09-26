package com.coalesce.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Permission {
	
	/**
	 * The permission(s) needed to run this command.
	 */
	String[] value() default "";
}
