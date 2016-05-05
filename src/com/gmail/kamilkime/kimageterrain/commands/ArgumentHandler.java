package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.PreparingGUI;
import com.gmail.kamilkime.kimageterrain.objects.Task;

public class ArgumentHandler {
	
	public void handle(CommandSender sender, String[] args) {
		if(Main.getSettings().preparingTask.containsKey(sender.getName())){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cYou are already preparing a task!"));
			return;
		}
		if((!StringUtils.hasArgument("-i", args) || (!StringUtils.hasArgument("-t", args) && !StringUtils.hasArgument("-b", args))) && !StringUtils.hasArgument("-g", args)){
			triggerHelp(sender);
			return;
		}
		if(StringUtils.hasArgument("-g", args) && (sender instanceof Player)){
			new PreparingGUI((Player)sender);
			return;
		}
		String imageName = StringUtils.listToString(StringUtils.getDataForArgument("-i", args), " ");
		boolean isTerrainTask = StringUtils.hasArgument("-t", args);
		boolean forceUniversalScheme = StringUtils.hasArgument("-u", args);
		World w = Bukkit.getWorlds().get(0);
		int[] start = new int[]{0,0};
		if(imageName.isEmpty()){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cYou did not give an image name!"));
			return;
		}
		if(FileManager.getImage(imageName) == null) {
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cNo image named &7" + imageName + " &cfound!"));
			return;
		}
		if(StringUtils.hasArgument("-w", args)) {
			String worldName = StringUtils.listToString(StringUtils.getDataForArgument("-w", args), " ");
			if(Bukkit.getWorld(worldName) == null) {
				sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cNo world named &7" + worldName + " &cfound!"));
				return;
			}
			w = Bukkit.getWorld(worldName);
		}
		if(StringUtils.hasArgument("-s", args)) {
			String data = StringUtils.listToString(StringUtils.getDataForArgument("-s", args), " ");
			if(!data.contains(",")) {
				sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cCorrect argument syntax: &7-s <startX,startZ>"));
				return;
			}
			String[] spl = data.split(",");
			try {
				start = new int[]{Integer.parseInt(spl[0]), Integer.parseInt(spl[1])};
			} catch (NumberFormatException e) {
				sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cProbably &7" + spl[0] + " &cor &7" + spl[1] + " &cis not a valid integer!"));
				return;
			}
		}
		boolean[] hasS = FileManager.imageHasSchemes(imageName);
		boolean usingUniversal = forceUniversalScheme ? true : (hasS[0] ? (isTerrainTask ? (hasS[1] ? false : true) : (hasS[2] ? false : true)) : true);
		Task t = new Task(imageName, (usingUniversal ? (isTerrainTask ? Main.getSettings().universalTerrainScheme : Main.getSettings().universalBiomeScheme) : FileManager.getSchemesForImage(imageName, isTerrainTask)),
				usingUniversal, isTerrainTask, w);
		t.setStartX(start[0]);
		t.setStartZ(start[1]);
		t.startPreparing(sender, false);
		Main.getSettings().byCmd++;
	}
	
	public void triggerHelp(CommandSender sender) {
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&cProper &7/kterrain &ccommand &c&lMUST &r&ccontain following arguments&8:"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-i <imageName>  &8[&7argument pointing to used image&8]"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-b &7or &c-t  &8[&7task type - TERRAIN or BIOME&8]"));
		sender.sendMessage(StringUtils.color("&7&lOR"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-g  &8[&7opens task creation GUI&8]"));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color("&cFor more help type&8: &7/kterrain help"));
		sender.sendMessage(StringUtils.color("&cTo list all available arguments type&8: &7/kterrain args"));
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		return;
	}
	
	public void triggerArgs(CommandSender sender) {
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&8&l» &a-i <imageName> &8- &7Selects image to process"));
		sender.sendMessage(StringUtils.color("&8&l» &a-t &8- &7Sets task type to TERRAIN"));
		sender.sendMessage(StringUtils.color("&8&l» &a-b &8- &7Sets task type to BIOME"));
		sender.sendMessage(StringUtils.color("&8&l» &a-g &8- &7Opens task creation GUI"));
		sender.sendMessage(StringUtils.color("&8&l» &a-u &8- &7Forces usage of UNIVERSAL scheme"));
		sender.sendMessage(StringUtils.color("&8&l» &a-s <startX,startZ> &8- &7Selects custom start location"));
		sender.sendMessage(StringUtils.color("&8&l» &a-w <worldName> &8- &7Selects modified world"));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color("&cIf you don't add &7-u &cargument &8- &cplugin will try to use &7OWN &cscheme, then &7UNIVERSAL &cif &7OWN &cis not present!"));
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
	}
}