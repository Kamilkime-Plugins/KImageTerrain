package com.gmail.kamilkime.kimageterrain.commands.sub;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;

public class CheckCmd implements KCommand {

	public void execute(CommandSender sender, String[] args) {
		if(args.length < 2) {
			triggerHelp(sender);
			return;
		}
		if(!StringUtils.hasArgument("-i", args) || (!StringUtils.hasArgument("-t", args) && !StringUtils.hasArgument("-b", args))){
			triggerHelp(sender);
			return;
		}
		String imageName = StringUtils.listToString(StringUtils.getDataForArgument("-i", args), " ");
		boolean isTerrainTask = StringUtils.hasArgument("-t", args);
		boolean forceUniversalScheme = StringUtils.hasArgument("-u", args);
		if(imageName.isEmpty()){
			sender.sendMessage(StringUtils.getMessage("noImageNameGiven", StringUtils.getMessage("prefix")));
			return;
		}
		if(FileManager.getImage(imageName) == null) {
			sender.sendMessage(StringUtils.getMessage("noImageFound", StringUtils.getMessage("prefix"), imageName));
			return;
		}
		BufferedImage image = FileManager.getImage(imageName);
		boolean[] hasS = FileManager.imageHasSchemes(imageName);
		boolean usingUniversal = forceUniversalScheme ? true : (hasS[0] ? (isTerrainTask ? (hasS[1] ? false : true) : (hasS[2] ? false : true)) : true);
		Collection<Scheme> schemes = usingUniversal ? (isTerrainTask ? Main.getSettings().universalTerrainScheme : Main.getSettings().universalBiomeScheme) : FileManager.getSchemesForImage(imageName, isTerrainTask);
		Collection<Color> missingColors = new HashSet<Color>();
		for(int x=0; x<image.getWidth(); x++){
			for(int y=0; y<image.getWidth(); y++){
				Color c = new Color(image.getRGB(x, y), Main.getSettings().useAlpha);
				if(!hasMatchingScheme(c, schemes)) missingColors.add(c);
			}
		}
		sender.sendMessage(StringUtils.color("               &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&8&l» &aImage used&8:  &7" + imageName));
		sender.sendMessage(StringUtils.color("&8&l» &aTask type&8:  &7" + (isTerrainTask ? "TERRAIN" : "BIOME")));
		sender.sendMessage(StringUtils.color("&8&l» &aUsed scheme&8:  &7" + (usingUniversal ? "UNIVERSAL" : "OWN")));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color(missingColors.isEmpty() ? "&aAll colors are configured!" : "&aThis colors are not configured&8:"));
		if(!missingColors.isEmpty()) {
			for(Color c : missingColors) {
				sender.sendMessage(StringUtils.color(" &8&l» &a" + c.getRed() + " " + c.getGreen() + " " + c.getBlue()
														+ (Main.getSettings().useAlpha ? " " + c.getAlpha() : "")));
			}
		}
		sender.sendMessage(StringUtils.color("               &8&l--------------------"));
	}
	
	private void triggerHelp(CommandSender sender){
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&cAvailable &7/kterrain check &ccommand variables&8:"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-i <imageName>  &8[&7variable pointing to used image&8]"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-b &7or &c-t  &8[&7task type - TERRAIN or BIOME&8]"));
		sender.sendMessage(StringUtils.color(" &8&l» &c-u  &8[&7forces usage of UNIVERSAL scheme&8]"));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color("&7-u &cvariable is optional, other are obligatory!"));
		sender.sendMessage(StringUtils.color("&cFor more help type&8: &7/kterrain help"));
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
	}
	
	private boolean hasMatchingScheme(Color c, Collection<Scheme> usedSchemes) {
		for(Scheme s : usedSchemes) {
			if(c.getRed() == s.getColor().getRed() && c.getGreen() == s.getColor().getGreen() && c.getBlue() == s.getColor().getBlue()){
				if(Main.getSettings().useAlpha) {
					if(c.getAlpha() == s.getColor().getAlpha()) return true;
					else return false;
				}
				return true;
			}
		}
		return false;
	}
}