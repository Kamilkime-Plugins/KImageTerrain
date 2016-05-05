package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.objects.Task;

public class AsyncPlayerChatListener implements Listener{

	@EventHandler(ignoreCancelled=true)
	public void onEvent(AsyncPlayerChatEvent e){
		if(!Main.getSettings().preparingTask.containsKey(e.getPlayer().getName())) return;
		Task t = Main.getSettings().preparingTask.get(e.getPlayer().getName());
		if(t.isWaitingForAccept()) return;
		t.nextStep(e.getPlayer(), true, e.getMessage());
		e.setCancelled(true);
	}
}