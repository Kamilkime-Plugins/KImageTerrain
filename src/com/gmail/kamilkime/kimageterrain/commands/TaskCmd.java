package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.Task;

public class TaskCmd {

	public void task(CommandSender sender, String[] args) {
		if(args.length !=3){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cCorrect usage: &7/kterrain task <taskID> <-t/-s/-ts>"));
			return;
		}
		int id = 0;
		try {
			id = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &c" + args[1] + " is not a valid integer!"));
			return;
		}
		if(id >= Main.getSettings().runningTasks.keySet().size()) {
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cThere's no task with id &7#" + id + "&c!"));
			return;
		}
		Task t = Main.getSettings().runningTasks.keySet().toArray(new Task[Main.getSettings().runningTasks.keySet().size()])[id];
		if(args[2].equalsIgnoreCase("-t")) {
			t.stopTask();
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aTask &7#" + id + " &ahas been terminated!"));
			return;
		}
		else if(args[2].equalsIgnoreCase("-s")) {
			Main.getSettings().saveTask(t);
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aTask &7#" + id + " &ahas been saved!"));
			return;
		}
		else if(args[2].equalsIgnoreCase("-ts")) {
			t.stopTask();
			Main.getSettings().saveTask(t);
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aTask &7#" + id + " &ahas been terminated and saved!"));
			return;
		} else {
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cCorrect usage: &7/kterrain task <taskID> <-t/-s/-ts>"));
			return;
		}
	}
}