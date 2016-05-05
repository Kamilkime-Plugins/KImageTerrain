package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()){
			new InfoCmd().info(sender);
			return true;
		}
		if(args.length == 0){
			new ArgumentHandler().triggerHelp(sender);
		} else {
			if(args[0].equalsIgnoreCase("accept")){
				new AcceptCmd().accept(sender, args);
			}
			else if(args[0].equalsIgnoreCase("args")){
				new ArgumentHandler().triggerArgs(sender);
			}
			else if(args[0].equalsIgnoreCase("check")){
				new CheckCmd().check(sender, args);
			}
			else if(args[0].equalsIgnoreCase("help")){
				new HelpCmd().help(sender);
			}
			else if(args[0].equalsIgnoreCase("images")){
				new ImagesCmd().images(sender);
			}
			else if(args[0].equalsIgnoreCase("info")){
				new InfoCmd().info(sender);
			}
			else if(args[0].equalsIgnoreCase("reload")){
				new ReloadCmd().reload(sender);
			}
			else if(args[0].equalsIgnoreCase("task")){
				new TaskCmd().task(sender, args);
			}
			else if(args[0].equalsIgnoreCase("tasks")){
				new TasksCmd().tasks(sender);
			} else {
				new ArgumentHandler().handle(sender, args);
			}
		}
		return false;
	}
}