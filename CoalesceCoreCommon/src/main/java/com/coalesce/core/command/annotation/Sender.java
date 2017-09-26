package com.coalesce.core.command.annotation;

import com.coalesce.core.SenderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Sender {
	
	/**
	 * The senders allowed to execute a command
	 */
	SenderType[] value() default SenderType.ALL;

}
