package com.gmail.kamilkime.kimageterrain.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;

public class InventoryClickListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onEvent(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		if(e.getClickedInventory() == null) return;
		if(!e.getClickedInventory().getTitle().equals(StringUtils.color("&5&lKImageTerrain GUI"))) return;
		if(!Main.getSettings().preparingGUIs.containsKey(e.getWhoClicked().getUniqueId())) return;
		Main.getSettings().preparingGUIs.get(e.getWhoClicked().getUniqueId()).changeVariables(e.getSlot());
		e.setCancelled(true);
	}
}