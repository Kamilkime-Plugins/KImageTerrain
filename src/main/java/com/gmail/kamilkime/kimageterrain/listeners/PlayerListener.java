package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTask;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onEvent(PlayerQuitEvent e) {
		Main.getSettings().preparingGUIs.remove(e.getPlayer().getUniqueId());
		Main.getSettings().preparingTask.remove(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onEvent(PlayerKickEvent e) {
		Main.getSettings().preparingGUIs.remove(e.getPlayer().getUniqueId());
		Main.getSettings().preparingTask.remove(e.getPlayer().getName());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onEvent(AsyncPlayerChatEvent e){
		if(!Main.getSettings().preparingTask.containsKey(e.getPlayer().getName())) return;
		PreparedTask t = Main.getSettings().preparingTask.get(e.getPlayer().getName());
		if(t.isWaitingForAccept()) return;
		t.nextStep(e.getPlayer(), true, e.getMessage());
		e.setCancelled(true);
	}
}