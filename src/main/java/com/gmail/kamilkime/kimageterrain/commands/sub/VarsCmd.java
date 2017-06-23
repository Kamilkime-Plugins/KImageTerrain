package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class VarsCmd implements KCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
		sender.sendMessage(StringUtils.color("&8&l» &a-i <imageName> &8- &7Selects image to process"));
		sender.sendMessage(StringUtils.color("&8&l» &a-t &8- &7Sets task type to TERRAIN"));
		sender.sendMessage(StringUtils.color("&8&l» &a-b &8- &7Sets task type to BIOME"));
		sender.sendMessage(StringUtils.color("&8&l» &a-g &8- &7Opens task creation GUI"));
		sender.sendMessage(StringUtils.color("&8&l» &a-u &8- &7Forces usage of UNIVERSAL scheme"));
		sender.sendMessage(StringUtils.color("&8&l» &a-s <startX,startZ> &8- &7Selects custom start location"));
		sender.sendMessage(StringUtils.color("&8&l» &a-w <worldName> &8- &7Selects modified world"));
		sender.sendMessage("");
		sender.sendMessage(StringUtils.color("&cIf you don't add &7-u &cvariable &8- &cplugin will try to use &7OWN &cscheme, then &7UNIVERSAL &cif &7OWN &cis not present!"));
		sender.sendMessage(StringUtils.color("                      &8&l--------------------"));
	}
}