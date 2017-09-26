package com.coalesce.core.command.base;

import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.annotation.Alias;
import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Permission;
import com.coalesce.core.command.annotation.Sender;
import com.coalesce.core.plugin.ICoPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandInfo {
	
	private final int min;
	private final int max;
	private final String name;
	private final String desc;
	private final String usage;
	private final Method method;
	private final ICoPlugin plugin;
	private final Object container;
	private String[] aliases = null;
	private String[] permissions = null;
	private SenderType[] senderTypes = null;
	private final CompletionInfo completionInfo;
	
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
		this.aliases = alias == null ? new String[]{""} : alias.value();
		this.permissions = permission == null ? new String[]{""} : permission.value();
		this.senderTypes = sender == null ? new SenderType[]{SenderType.ALL} : sender.value();
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
	public String[] getAliases() {
		return aliases;
	}
	
	/**
	 * @see Permission#value()
	 */
	public String[] getPermissions() {
		return permissions;
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
	
	public boolean run(com.coalesce.core.Sender sender, String... args) {
		boolean permission = false;
		boolean senderType = false;
		
		if (this.permissions.length != 0) {
			for (String node : this.permissions) {
				if (sender.hasPermission(node)) {
					permission = true;
					break;
				}
			}
		} else permission = true;
		
		if (!Arrays.equals(this.senderTypes, new SenderType[]{SenderType.ALL})) {
			for (SenderType type : this.senderTypes) {
				if (sender.getType().equals(type)) {
					senderType = true;
					break;
				}
			}
		} else senderType = true;
		
		if (!permission) {
			StringBuilder builder = new StringBuilder();
			for (String node : permissions) builder.append(node).append(", ");
			sender.pluginMessage(Color.RED + "You do not have sufficient permission to run " +
					"this command! Permission(s) required: " + Color.SILVER + builder.toString().trim());
			return true;
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
					Color.RED + "Not enough arguments supplied to run command!",
					Color.RED + "Minimum: " + Color.SILVER + min,
					Color.RED + "Given: " + Color.SILVER + args.length);
			return true;
		}
		if (args.length > max && this.getMax() > -1) {
			sender.pluginMessage(
					Color.RED + "Too many arguments supplied to run command!",
					Color.RED + "Maximum: " + Color.SILVER + max,
					Color.RED + "Given: " + Color.SILVER + args.length);
			return true;
		}
		
		try {
			this.method.invoke(container, new CommandContext(sender, args, plugin));
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public List<String> complete(com.coalesce.core.Sender sender, String... args) {
		
		List<String> sub = new ArrayList<>();
		
		if (this.completionInfo != null) {
			TabContext context = new TabContext(new CommandContext(sender, args, plugin), this, sender, args);
			try {
				this.completionInfo.getMethod().invoke(this.completionInfo.getContainer(), context);
			}
			catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			for (String completion : context.currentPossibleCompletion()) {
				if (completion.toLowerCase().startsWith(context.getCommandContext().argAt(context.getCommandContext().getArgs().size() - 1))) {
					sub.add(completion);
				}
			}
			return sub;
		}
		return null;
	}
	
}
