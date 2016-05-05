package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.kamilkime.kimageterrain.Main;

public class PlayerQuitListener implements Listener {
	
	@EventHandler
	public void onEvent(PlayerQuitEvent e) {
		if(Main.getSettings().preparingGUIs.containsKey(e.getPlayer().getUniqueId())) Main.getSettings().preparingGUIs.remove(e.getPlayer().getUniqueId());
		if(Main.getSettings().preparingTask.containsKey(e.getPlayer().getName())) Main.getSettings().preparingTask.remove(e.getPlayer().getName());
	}
}