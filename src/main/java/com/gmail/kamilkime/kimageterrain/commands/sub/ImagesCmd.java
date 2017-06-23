package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class ImagesCmd implements KCommand {

	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
		for(String s : FileManager.getImagesNames()) {
			sender.sendMessage(StringUtils.color("&8&l» &a" + s + (FileManager.imageHasSchemes(s)[0] ? "  &8[&7has own scheme file&8]" : "")));
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
	}
}