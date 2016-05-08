package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.gui.PreparingGUI;

public class InventoryListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEvent(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		if(e.getClickedInventory() == null) return;
		if(!e.getClickedInventory().getTitle().equals(StringUtils.color("&5&lKImageTerrain GUI"))) return;
		if(!Main.getSettings().preparingGUIs.containsKey(e.getWhoClicked().getUniqueId())) return;
		Main.getSettings().preparingGUIs.get(e.getWhoClicked().getUniqueId()).changeVariables(e.getSlot());
		e.setCancelled(true);
	}
	
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