package com.gmail.kamilkime.kimageterrain.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {

	private String[] arguments = new String[]{"accept", "args", "check", "help", "images", "info", "reload", "task", "tasks"};
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> toReturn = new ArrayList<String>();
		if(args.length == 0) {
			for(String s : arguments) toReturn.add(s);
		}
		if(args.length == 1) {
			for(String s : arguments) {
				if(s.startsWith(args[0].toLowerCase())) toReturn.add(s);
			}
		}
		return toReturn;
	}
}