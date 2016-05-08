package com.gmail.kamilkime.kimageterrain.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.commands.sub.AcceptCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.VariableHandler;
import com.gmail.kamilkime.kimageterrain.commands.sub.CheckCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.HelpCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.ImagesCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.InfoCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.ReloadCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.TaskCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.TasksCmd;
import com.gmail.kamilkime.kimageterrain.commands.sub.VarsCmd;

public class MainCmd implements CommandExecutor {

	private Map<String, KCommand> registeredArguments = new HashMap<String, KCommand>();
	
	public MainCmd() {
		registeredArguments.put("accept", new AcceptCmd());
		registeredArguments.put("check", new CheckCmd());
		registeredArguments.put("help", new HelpCmd());
		registeredArguments.put("images", new ImagesCmd());
		registeredArguments.put("info", new InfoCmd());
		registeredArguments.put("reload", new ReloadCmd());
		registeredArguments.put("task", new TaskCmd());
		registeredArguments.put("tasks", new TasksCmd());
		registeredArguments.put("vars", new VarsCmd());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()){
			registeredArguments.get("info").execute(sender, args);;
			return true;
		}
		if(args.length == 0){
			VariableHandler.triggerHelp(sender);
		} else {
			if(registeredArguments.containsKey(args[0].toLowerCase())) {
				registeredArguments.get(args[0].toLowerCase()).execute(sender, args);
			} else {
				VariableHandler.handle(sender, args);
			}
		}
		return false;
	}
}