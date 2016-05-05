package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class InfoCmd {

	public void info(CommandSender sender) {
		sender.sendMessage(StringUtils.color("                       &8&l--------------------"));
		sender.sendMessage(StringUtils.color("                &7&lKImageTerrain v" + Main.getInst().getDescription().getVersion() + " &6&lby Kamilkime"));
		sender.sendMessage(StringUtils.color("                        &a&lhttps://goo.gl/BLqvns"));
		sender.sendMessage(StringUtils.color("             &6&lFor plugin help type &7&l/kterrain help"));
		sender.sendMessage(StringUtils.color("                       &8&l--------------------"));
	}
}