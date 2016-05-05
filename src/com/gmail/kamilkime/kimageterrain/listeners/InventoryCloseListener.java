package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.PreparingGUI;

public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onEvent(InventoryCloseEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		if(e.getInventory() == null) return;
		if(!e.getInventory().getTitle().equals(StringUtils.color("&5&lKImageTerrain GUI"))) return;
		if(!Main.getSettings().preparingGUIs.containsKey(e.getPlayer().getUniqueId())) return;
		PreparingGUI gui = Main.getSettings().preparingGUIs.get(e.getPlayer().getUniqueId());
		if(gui.hasJustReopened()) gui.acknowledgeReopen();
		else Main.getSettings().preparingGUIs.remove(e.getPlayer().getUniqueId());
	}
}