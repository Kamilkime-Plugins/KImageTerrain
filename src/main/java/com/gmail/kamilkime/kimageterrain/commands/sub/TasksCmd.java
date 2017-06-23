package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.task.Task;

public class TasksCmd implements KCommand {

	public void execute(CommandSender sender, String[] args) {
		if(Main.getSettings().runningTasks.isEmpty()){
			sender.sendMessage(StringUtils.getMessage("noTasksRunning", StringUtils.getMessage("prefix")));
			return;
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
		for(int i=0; i<Main.getSettings().runningTasks.size(); i++) {
			Task t = Main.getSettings().runningTasks.get(i);
			sender.sendMessage(StringUtils.color("&7&lTask &a&l#" + i + " &7&lfor image &a&l" + t.getImageName() + "&8&l:"));
			sender.sendMessage(StringUtils.color(" &8&l» &aTask type&8:  &7" + (t.isTerrainTask() ? "TERRAIN" : "BIOME")));
			sender.sendMessage(StringUtils.color(" &8&l» &aUsed scheme&8:  &7" + (t.isUsingUniversalScheme() ? "UNIVERSAL" : "OWN")));
			sender.sendMessage(StringUtils.color(" &8&l» &aModified world&8:  &7" + t.getWorld().getName()));
			sender.sendMessage(StringUtils.color(" &8&l» &aStart position&8:  &7" + "x&8=&7" + t.getStartX() + "   z&8=&7" + t.getStartZ()));
			sender.sendMessage(StringUtils.color(" &8&l» &aFinish position&8:  &7" + "x&8=&7" + (t.getStartX() + t.getImage().getWidth())
													+ "   z&8=&7" + (t.getStartZ() + t.getImage().getHeight())));
			sender.sendMessage(StringUtils.color(" &8&l» &aProgress&8:  &8[&7" + (t.getCurrentImgY()*t.getImage().getHeight() + t.getCurrentImgX())
													+ "&8/&7" + (t.getImage().getWidth()*t.getImage().getHeight()) + "&8]"));
			if(i !=Main.getSettings().runningTasks.size()-1) sender.sendMessage("");
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
	}
}