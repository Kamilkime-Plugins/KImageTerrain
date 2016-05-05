package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class ReloadCmd {

	public void reload(CommandSender sender) {
		Main.getSettings().reloadConfig();
		sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aConfig file successfully reloaded!"));
	}
}