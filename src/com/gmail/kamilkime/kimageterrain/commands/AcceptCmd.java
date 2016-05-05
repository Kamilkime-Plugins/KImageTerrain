package com.gmail.kamilkime.kimageterrain.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.Task;

public class AcceptCmd {
	
	public void accept(CommandSender sender, String[] args) {
		if(args.length !=2){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cCorrect usage: &7/kterrain accept <securityPassword>"));
			return;
		}
		if(!Main.getSettings().preparingTask.containsKey(sender.getName())){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cYou are not preparing any task!"));
			return;
		}
		Task t = Main.getSettings().preparingTask.get(sender.getName());
		if(!t.isWaitingForAccept()){
			sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &cYour task is not waiting for acceptance!"));
			return;
		}
		if(!args[1].equals(Main.getSettings().securityPassword)){
			t.terminatePreparing(sender, "&7&l[KImageTerrain] &cSecurity password incorrect, admins informed!");
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("kimageterrain.notify")) p.sendMessage(StringUtils.color("&7&l[KImageTerrain] &4" + sender.getName() + " &ctyped incorrect security password!"));
			}
			for(int i=0; i<5; i++) Main.error(sender.getName() + " typed incorrect security password!");
			return;
		}
		t.nextStep(sender, true, "ACCEPT");
	}
}