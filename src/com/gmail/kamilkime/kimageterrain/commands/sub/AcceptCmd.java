package com.gmail.kamilkime.kimageterrain.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.commands.KCommand;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTask;

public class AcceptCmd implements KCommand {
	
	public void execute(CommandSender sender, String[] args) {
		if(args.length !=2){
			sender.sendMessage(StringUtils.getMessage("correctUsage", StringUtils.getMessage("prefix"), "/kterrain accept <securityPassword>"));
			return;
		}
		if(!Main.getSettings().preparingTask.containsKey(sender.getName())){
			sender.sendMessage(StringUtils.getMessage("notPreparingAnyTask", StringUtils.getMessage("prefix")));
			return;
		}
		PreparedTask t = Main.getSettings().preparingTask.get(sender.getName());
		if(!t.isWaitingForAccept()){
			sender.sendMessage(StringUtils.getMessage("notPreparingAnyTask", StringUtils.getMessage("prefix")));
			return;
		}
		if(Main.getSettings().requireSecurityPassword) {
			if(!args[1].equals(Main.getSettings().securityPassword)){
				t.terminatePreparing(sender, StringUtils.getMessage("passwordIncorrect", StringUtils.getMessage("prefix")));
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.hasPermission("kimageterrain.notify")) {
						p.sendMessage(StringUtils.getMessage("notifyPasswordIncorrect", StringUtils.getMessage("prefix"), sender.getName()));
					}
				}
				for(int i=0; i<5; i++) Main.error(StringUtils.getMessage("notifyConsolePasswordIncorrect", sender.getName()));
				return;
			}
		}
		t.nextStep(sender, true, "ACCEPT");
	}
}