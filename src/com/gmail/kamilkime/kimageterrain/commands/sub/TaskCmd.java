package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.task.Task;

public class TaskCmd implements KCommand {

	public void execute(CommandSender sender, String[] args) {
		if(args.length !=3){
			sender.sendMessage(StringUtils.getMessage("correctUsage", StringUtils.getMessage("prefix"), "/kterrain task <taskID> <-t/-s/-ts>"));
			return;
		}
		int id = 0;
		try {
			id = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			sender.sendMessage(StringUtils.getMessage("taskIDNotInt", StringUtils.getMessage("prefix"), args[1]));
			return;
		}
		if(id >= Main.getSettings().runningTasks.size()) {
			sender.sendMessage(StringUtils.getMessage("noTaskWithID", StringUtils.getMessage("prefix"), id));
			return;
		}
		Task t = Main.getSettings().runningTasks.get(id);
		if(args[2].equalsIgnoreCase("-t")) {
			t.stopTask();
			sender.sendMessage(StringUtils.getMessage("taskTerminated", StringUtils.getMessage("prefix"), id));
			return;
		}
		else if(args[2].equalsIgnoreCase("-s")) {
			Main.getSettings().saveTask(t);
			sender.sendMessage(StringUtils.getMessage("taskSaved", StringUtils.getMessage("prefix"), id));
			return;
		}
		else if(args[2].equalsIgnoreCase("-ts")) {
			t.stopTask();
			Main.getSettings().saveTask(t);
			sender.sendMessage(StringUtils.getMessage("taskTerminatedAndSaved", StringUtils.getMessage("prefix"), id));
			return;
		} else {
			sender.sendMessage(StringUtils.getMessage("correctUsage", StringUtils.getMessage("prefix"), "/kterrain task <taskID> <-t/-s/-ts>"));
			return;
		}
	}
}