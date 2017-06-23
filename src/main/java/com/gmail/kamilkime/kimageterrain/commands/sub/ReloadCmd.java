package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.command.CommandSender;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class ReloadCmd implements KCommand {

	public void execute(CommandSender sender, String[] args) {
		Main.getSettings().reloadConfig();
		sender.sendMessage(StringUtils.getMessage("configReloaded", StringUtils.getMessage("prefix")));
	}
}