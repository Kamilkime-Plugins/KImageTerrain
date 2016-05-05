package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class ImagesCmd {

	public void images(CommandSender sender) {
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
		for(String s : FileManager.getImagesNames()) {
			sender.sendMessage(StringUtils.color("&8&l» &a" + s + (FileManager.imageHasSchemes(s)[0] ? "  &8[&7has own scheme file&8]" : "")));
		}
		sender.sendMessage(StringUtils.color("                 &8&l--------------------"));
	}
}