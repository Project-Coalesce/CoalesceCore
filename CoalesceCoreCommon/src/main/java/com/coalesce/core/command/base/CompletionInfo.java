package com.coalesce.core.command.base;

import com.coalesce.core.command.annotation.Completion;

import java.lang.reflect.Method;

public final class CompletionInfo {
	
	private final Method method;
	private final Object container;
	private final String[] commands;
	
	public CompletionInfo(Object container, Method method) {
		Completion completion;
		if (!method.isAnnotationPresent(Completion.class)) {
			throw new RuntimeException("Cannot find completion annotation on method " + method.getName());
		} else completion = method.getAnnotation(Completion.class);
		this.commands = completion.value();
		this.container = container;
		this.method = method;
	}
	
	/**
	 * The commands this completion belongs to
	 * @return The command names
	 */
	public String[] getCommands() {
		return commands;
	}
	
	/**
	 * Gets the method this completion is contained in
	 * @return The completion method
	 */
	public Method getMethod() {
		return method;
	}
	
	/**
	 * Gets the object that holds this completion
	 * @return The containing object
	 */
	public Object getContainer() {
		return container;
	}
}
