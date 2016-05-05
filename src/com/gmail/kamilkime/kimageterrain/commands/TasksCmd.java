package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.Task;

public class TasksCmd {

	public void tasks(CommandSender sender) {
		Task[] tasks = Main.getSettings().runningTasks.keySet().toArray(new Task[Main.getSettings().runningTasks.keySet().size()]);
		if(tasks.length == 0){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cNo tasks are running now!"));
			return;
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
		for(int i=0; i<tasks.length; i++) {
			Task t = tasks[i];
			sender.sendMessage(StringUtils.color("&7&lTask &a&l#" + i + " &7&lfor image &a&l" + t.getImageName() + "&8&l:"));
			sender.sendMessage(StringUtils.color(" &8&l» &aTask type&8:  &7" + (t.isTerrainTask() ? "TERRAIN" : "BIOME")));
			sender.sendMessage(StringUtils.color(" &8&l» &aUsed scheme&8:  &7" + (t.isUsingUniversalScheme() ? "UNIVERSAL" : "OWN")));
			sender.sendMessage(StringUtils.color(" &8&l» &aModified world&8:  &7" + t.getWorld().getName()));
			sender.sendMessage(StringUtils.color(" &8&l» &aStart position&8:  &7" + "x&8=&7" + t.getStartX() + "   z&8=&7" + t.getStartZ()));
			sender.sendMessage(StringUtils.color(" &8&l» &aFinish position&8:  &7" + "x&8=&7" + (t.getStartX() + t.getImage().getWidth())
													+ "   z&8=&7" + (t.getStartZ() + t.getImage().getHeight())));
			sender.sendMessage(StringUtils.color(" &8&l» &aProgress&8:  &8[&7" + (t.getCurrentImgY()*t.getImage().getHeight() + t.getCurrentImgX())
													+ "&8/&7" + (t.getImage().getWidth()*t.getImage().getHeight()) + "&8]"));
			if(i !=tasks.length-1) sender.sendMessage("");
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
	}
}