package com.gmail.kamilkime.kimageterrain.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedBiomeTask;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTerrainTask;
import com.gmail.kamilkime.kimageterrain.utils.ItemStackUtils;

public class PreparingGUI {

	private String image;
	private boolean usingUniversalScheme = true;
	private boolean isTerrainTask = true;
	private boolean withStartPoint = false;
	private boolean justReopened = false;
	private World world;
	private Inventory inv;
	private Player p;
	
	private int[] blankSlots = new int[]{0,1,3,5,7,8,10,11,13,15,16};
	private ItemStack blank = ItemStackUtils.create(Material.STAINED_GLASS_PANE, 1, (short) 15, " ");
	private ItemStack cancel = ItemStackUtils.create(Material.WOOL, 1, (short) 14, StringUtils.color("&c&lCANCEL"));
	private ItemStack finish = ItemStackUtils.create(Material.WOOL, 1, (short) 5, StringUtils.color("&a&lFINISH"), StringUtils.color(Arrays.asList("", "&b&lCheck everything", "&b&lbefore finishing!")));
	private ItemStack img_sel = ItemStackUtils.create(Material.PAINTING, 1, (short) 0);
	private ItemStack world_sel = ItemStackUtils.create(Material.GRASS, 1, (short) 0);
	private ItemStack type_sel = ItemStackUtils.create(Material.DIAMOND_SPADE, 1, (short) 0);
	private ItemStack scheme_sel = ItemStackUtils.create(Material.PAPER, 1, (short) 0);
	private ItemStack start_sel = ItemStackUtils.create(Material.COMPASS, 1, (short) 0);
	
	public PreparingGUI(Player p) {
		this.image = FileManager.getImagesNames().get(0);
		this.world = Bukkit.getWorlds().get(0);
		this.p = p;
		Main.getSettings().preparingGUIs.put(p.getUniqueId(), this);
		reopenInventory(false);
	}
	
	public void changeVariables(int slot) {
		switch(slot) {
		case 9:
			cancel();
			return;
		case 17:
			finish();
			return;
		case 2:
			image = FileManager.getImagesNames()
					.get(FileManager.getImagesNames().indexOf(image)+1 >= FileManager.getImagesNames().size() ? 0 : FileManager.getImagesNames().indexOf(image)+1);
			break;
		case 4:
			world = Bukkit.getWorlds().get(Bukkit.getWorlds().indexOf(world)+1 >= Bukkit.getWorlds().size() ? 0 : Bukkit.getWorlds().indexOf(world)+1);
			break;
		case 6:
			isTerrainTask = !isTerrainTask;
			break;
		case 12:
			usingUniversalScheme = !usingUniversalScheme;
			break;
		case 14:
			withStartPoint = !withStartPoint;
			break;
		default: break;
		}
		reopenInventory(true);
	}
	
	public Player getPlayer(){
		return this.p;
	}
	
	public boolean hasJustReopened(){
		return this.justReopened;
	}
	
	public void acknowledgeReopen(){
		this.justReopened = false;
	}
	
	public void finish() {
		p.closeInventory();
		if(Main.getSettings().preparingGUIs.containsKey(p.getUniqueId())) Main.getSettings().preparingGUIs.remove(p.getUniqueId());
		if(isTerrainTask) {
			new PreparedTerrainTask(image, (usingUniversalScheme ? Main.getSettings().universalTerrainScheme : FileManager.getSchemesForImage(image, true)),
					usingUniversalScheme, world).startPreparing(p, withStartPoint);
		} else {
			new PreparedBiomeTask(image, (usingUniversalScheme ? Main.getSettings().universalBiomeScheme : FileManager.getSchemesForImage(image, false)),
					usingUniversalScheme, world).startPreparing(p, withStartPoint);
		}
		Main.getSettings().byGui++;
	}
	
	public void cancel() {
		p.closeInventory();
		if(Main.getSettings().preparingGUIs.containsKey(p.getUniqueId())) Main.getSettings().preparingGUIs.remove(p.getUniqueId());
	}
	
	private void reopenInventory(boolean hasReopened) {
		recreateInv();
		if(hasReopened) justReopened = true;
		p.openInventory(inv);
	}
	
	private void recreateInv(){
		renameItemStacks();
		if(inv == null) {
			inv = Bukkit.createInventory(null, 18, StringUtils.color("&5&lKImageTerrain GUI"));
			for(int bs : blankSlots) inv.setItem(bs, blank);
			inv.setItem(9, cancel);
			inv.setItem(17, finish);
		}
		inv.setItem(2, img_sel);
		inv.setItem(4, world_sel);
		inv.setItem(6, type_sel);
		inv.setItem(12, scheme_sel);
		inv.setItem(14, start_sel);
	}
	
	private void renameItemStacks() {
		ItemStackUtils.name(img_sel, StringUtils.color("&a&lImage: &7" + image + ".png"));
		if(FileManager.getImagesNames().size() > 1) ItemStackUtils.lore(img_sel, Arrays.asList("", StringUtils.color("&b&lClick to change")));
		
		ItemStackUtils.name(world_sel, StringUtils.color("&a&lWorld: &7" + world.getName()));
		if(Bukkit.getWorlds().size() > 1) ItemStackUtils.lore(world_sel, Arrays.asList("", StringUtils.color("&b&lClick to change")));
		
		type_sel.setType(isTerrainTask ? Material.DIAMOND_SPADE : Material.SAPLING);
		ItemStackUtils.name(type_sel, StringUtils.color("&a&lTask type: &7" + (isTerrainTask ? "TERRAIN" : "BIOME")));
		ItemStackUtils.lore(type_sel, Arrays.asList("", StringUtils.color("&b&lClick to change")));
		
		boolean[] hasOwn = FileManager.imageHasSchemes(image);
		if(!usingUniversalScheme && (!hasOwn[0] || (isTerrainTask && !hasOwn[1]) || (!isTerrainTask && !hasOwn[2]))) usingUniversalScheme = true;
		scheme_sel.setType(usingUniversalScheme ? Material.PAPER : Material.EMPTY_MAP);
		ItemStackUtils.name(scheme_sel, StringUtils.color("&a&lScheme: &7" + (usingUniversalScheme ? "UNIVERSAL" : "OWN")));
		if(!usingUniversalScheme || (usingUniversalScheme && hasOwn[0] && ((isTerrainTask && hasOwn[1]) || (!isTerrainTask && hasOwn[2])))) {
			ItemStackUtils.lore(scheme_sel, Arrays.asList("", StringUtils.color("&b&lClick to change")));
		} else {
			ItemStackUtils.lore(scheme_sel, null);
		}
		
		ItemStackUtils.name(start_sel, StringUtils.color("&a&lCustom startpoint: &7" + (withStartPoint ? "YES" : "NO")));
		ItemStackUtils.lore(start_sel, Arrays.asList("", StringUtils.color("&b&lClick to change")));
	}
}