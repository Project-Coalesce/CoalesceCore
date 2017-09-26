package com.coalesce.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Command {
	
	/**
	 * The name of the command
	 */
	String name();
	
	/**
	 * The command description.
	 */
	String desc() default "";
	
	/**
	 * The command usage.
	 */
	String usage() default "";
	
	/**
	 * The minimum amount of arguments allowed in a command.
	 */
	int min() default -1;
	
	/**
	 * The maximum amount of arguments allowed in a command.
	 */
	int max() default -1;
	
}
