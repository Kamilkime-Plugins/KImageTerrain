package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.gui.PreparingGUI;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedBiomeTask;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTask;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTerrainTask;

public class VariableHandler {
	
	public static void handle(CommandSender sender, String[] args) {
		if(Main.getSettings().preparingTask.containsKey(sender.getName())){
			sender.sendMessage(StringUtils.getMessage("alreadyPreparing", StringUtils.getMessage("prefix")));
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
			sender.sendMessage(StringUtils.getMessage("noImageNameGiven", StringUtils.getMessage("prefix")));
			return;
		}
		if(FileManager.getImage(imageName) == null) {
			sender.sendMessage(StringUtils.getMessage("noImageFound", StringUtils.getMessage("prefix"), imageName));
			return;
		}
		if(StringUtils.hasArgument("-w", args)) {
			String worldName = StringUtils.listToString(StringUtils.getDataForArgument("-w", args), " ");
			if(Bukkit.getWorld(worldName) == null) {
				sender.sendMessage(StringUtils.getMessage("noWorldFound", StringUtils.getMessage("prefix"), worldName));
				return;
			}
			w = Bukkit.getWorld(worldName);
		}
		if(StringUtils.hasArgument("-s", args)) {
			String data = StringUtils.listToString(StringUtils.getDataForArgument("-s", args), " ");
			if(!data.contains(",")) {
				sender.sendMessage(StringUtils.getMessage("sVariableSyntax", StringUtils.getMessage("prefix")));
				return;
			}
			String[] spl = data.split(",");
			try {
				start = new int[]{Integer.parseInt(spl[0]), Integer.parseInt(spl[1])};
			} catch (NumberFormatException e) {
				sender.sendMessage(StringUtils.getMessage("sNotIntegers", StringUtils.getMessage("prefix"), spl[0], spl[1]));
				return;
			}
		}
		boolean[] hasS = FileManager.imageHasSchemes(imageName);
		boolean usingUniversal = forceUniversalScheme ? true : (hasS[0] ? (isTerrainTask ? (hasS[1] ? false : true) : (hasS[2] ? false : true)) : true);
		PreparedTask t = null;
		if(isTerrainTask) {
			t = new PreparedTerrainTask(imageName, (usingUniversal ? Main.getSettings().universalTerrainScheme : FileManager.getSchemesForImage(imageName, true)), usingUniversal, w);
		} else {
			t = new PreparedBiomeTask(imageName, (usingUniversal ? Main.getSettings().universalBiomeScheme : FileManager.getSchemesForImage(imageName, false)), usingUniversal, w);
		}
		t.setStartX(start[0]);
		t.setStartZ(start[1]);
		t.startPreparing(sender, false);
		Main.getSettings().byCmd++;
	}
	
	public static void triggerHelp(CommandSender sender) {
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&cProper &7/kterrain &ccommand &c&lMUST &r&ccontain following variables&8:"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-i <imageName>  &8[&7variable pointing to used image&8]"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-b &7or &c-t  &8[&7task type - TERRAIN or BIOME&8]"));
		sender.sendMessage(StringUtils.color("&7&lOR"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-g  &8[&7opens task creation GUI&8]"));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color("&cFor more help type&8: &7/kterrain help"));
		sender.sendMessage(StringUtils.color("&cTo list all available variables type&8: &7/kterrain vars"));
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		return;
	}
}