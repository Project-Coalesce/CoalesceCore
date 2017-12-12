package com.coalesce.core.command.base;

import com.coalesce.core.Color;
import com.coalesce.core.RegistrationType;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.annotation.Alias;
import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Permission;
import com.coalesce.core.command.annotation.Sender;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.wrappers.CoSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandInfo {
	
	private final int min;
	private final int max;
	private final String name;
	private final String desc;
	private final String usage;
	private final ICoPlugin plugin;
	private Set<String> aliases;
	private String permission = null;
	private SenderType[] senderTypes = null;
	private final RegistrationType registrationType;
	
	//Annotation Only
	private Method method;
	private Object container;
	private CompletionInfo completionInfo;
	
	//Builder Only
	private TabExecutor tabExecutor;
	private CommandExecutor commandExecutor;
	
	public CommandInfo(Object container, Method method, CompletionInfo completionInfo, ICoPlugin plugin) {
		Command command;
		Alias alias = null;
		Sender sender = null;
		Permission permission = null;
		
		if (!method.isAnnotationPresent(Command.class)) {
			throw new RuntimeException("Cannot find command annotation on method " + method.getName());
		} else command = method.getAnnotation(Command.class);
		if (method.isAnnotationPresent(Alias.class)) alias = method.getAnnotation(Alias.class);
		if (method.isAnnotationPresent(Sender.class)) sender = method.getAnnotation(Sender.class);
		if (method.isAnnotationPresent(Permission.class)) permission = method.getAnnotation(Permission.class);
		
		this.plugin = plugin;
		this.method = method;
		this.min = command.min();
		this.max = command.max();
		this.name = command.name();
		this.desc = command.desc();
		this.container = container;
		this.usage = command.usage();
		this.completionInfo = completionInfo;
		this.registrationType = RegistrationType.ANNOTATION;
		this.permission = permission == null ? null : permission.value();
		this.senderTypes = sender == null ? new SenderType[]{SenderType.ALL} : sender.value();
		this.aliases = alias == null ? new HashSet<>() : Stream.of(alias.value()).map(String::toLowerCase).collect(Collectors.toSet());
	}
	
	public CommandInfo(ProcessedCommand command, ICoPlugin plugin) {
		this.plugin = plugin;
		this.min = command.getMin();
		this.max = command.getMax();
		this.name = command.getName();
		this.usage = command.getUsage();
		this.aliases = command.getAliases();
		this.desc = command.getDescription();
		this.permission = command.getPermission();
		this.tabExecutor = command.getTabExecutor();
		this.registrationType = RegistrationType.BUILDER;
		this.commandExecutor = command.getCommandExecutor();
		this.senderTypes = command.getSenders() == null ? new SenderType[]{SenderType.ALL} : command.getSenders();
		
	}
	
	/**
	 * @see Command#min()
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 *
	 * @see Command#max()
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * @see Command#name()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @see Command#desc()
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * @see Command#usage()
	 */
	public String getUsage() {
		return usage;
	}
	
	/**
	 * The method in which the command is held in.
	 */
	public Method getMethod() {
		return method;
	}
	
	/**
	 * The command container. (the object the method is stored in)
	 */
	public Object getContainer() {
		return container;
	}
	
	/**
	 * @see Alias#value()
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	
	/**
	 * @see Permission#value()
	 */
	public String getPermission() {
		return permission;
	}
	
	/**
	 * @see Sender#value()
	 */
	public SenderType[] getSenderTypes() {
		return senderTypes;
	}
	
	/**
	 * Returns the tab completion information for this command.
	 * @return The tab completion information.
	 */
	public CompletionInfo getCompletionInfo() {
		return completionInfo;
	}
	
	public boolean run(CoSender sender, String... args) {
		boolean senderType = false;
		
		if (!Arrays.equals(this.senderTypes, new SenderType[]{SenderType.ALL})) {
			for (SenderType type : this.senderTypes) {
				if (sender.getType().equals(type)) {
					senderType = true;
					break;
				}
			}
		} else senderType = true;
		
		if (this.permission != null) {
			if (!sender.hasPermission(this.permission)) {
				sender.pluginMessage(Color.RED + "You do not have sufficient permission to run " +
						"this command! Permission required: " + Color.SILVER + this.permission);
				return true;
			}
		}
		
		if (!senderType) {
			StringBuilder builder = new StringBuilder();
			for (SenderType type : senderTypes) builder.append(type.toString()).append(", ");
			sender.pluginMessage(Color.RED + "You are not the correct sender type to run this " +
					"command! One of the following sender type(s) are required: " + Color.SILVER + builder.toString().trim());
			return true;
		}
		
		if (args.length < min && this.getMin() > -1) {
			sender.pluginMessage(
					Color.RED + "Not enough arguments supplied to run command!" +
					Color.RED + " Minimum: " + Color.SILVER + min +
					Color.RED + " Given: " + Color.SILVER + args.length);
			return true;
		}
		if (args.length > max && this.getMax() > -1) {
			sender.pluginMessage(
					Color.RED + "Too many arguments supplied to run command!" +
					Color.RED + " Maximum: " + Color.SILVER + max +
					Color.RED + " Given: " + Color.SILVER + args.length);
			return true;
		}
		if (registrationType == RegistrationType.ANNOTATION) {
			try {
				this.method.invoke(container, new CommandContext(sender, args, plugin));
			}
			catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		else commandExecutor.run(new CommandContext(sender, args, plugin));
		return true;
	}
	
	public List<String> complete(CoSender sender, String... args) {
		
		List<String> sub = new ArrayList<>();
		
		TabContext context = new TabContext(new CommandContext(sender, args, plugin), this, sender, args);
		String startString = context.getCommandContext().argAt(context.getCommandContext().getArgs().size() - 1);
		
		if (startString == null) return null;
		
		if (registrationType == RegistrationType.ANNOTATION) {
			if (this.completionInfo != null) {
				try {
					this.completionInfo.getMethod().invoke(this.completionInfo.getContainer(), context);
				}
				catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				for (String completion : context.currentPossibleCompletion()) {
					if (completion.toLowerCase().startsWith(startString)) {
						sub.add(completion);
					}
				}
				return sub;
			}
		}
		else {
			if (tabExecutor != null) {
				tabExecutor.run(context);
				
				for (String completion : context.currentPossibleCompletion()) {
					if (completion.toLowerCase().startsWith(startString)) {
						sub.add(completion);
					}
				}
				return sub;
				
			}
		}
		return null;
	}
	
}
