package com.gmail.kamilkime.kimageterrain.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.objects.scheme.BiomeScheme;
import com.gmail.kamilkime.kimageterrain.objects.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.objects.scheme.TerrainScheme;

public class Task {

	private List<PrepareStep> prepareSteps;
	private Collection<Scheme> usedSchemes;
	private BukkitTask acceptTask;
	private boolean usingUniversalScheme;
	private boolean isTerrainTask;
	private String imageName;
	private BufferedImage image;
	private World world;
	private int currentImgX = 0;
	private int currentImgY = 0;
	private int startX = 0;
	private int startZ = 0;
	private int changes = 0;

	public Task(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, boolean isTerrainTask, World world) {
		this.imageName = imageName.toLowerCase();
		this.usedSchemes = usedSchemes;
		this.usingUniversalScheme = usingUniversalScheme;
		this.isTerrainTask = isTerrainTask;
		this.world = world;
		this.image = FileManager.getImage(imageName);
	}
	
	@SuppressWarnings("deprecation")
	public void nextChanges() {
		while(changes < (isTerrainTask ? Main.getSettings().terrainChangeAtOnce : Main.getSettings().biomeChangeAtOnce)) {
			if(currentImgX >= image.getWidth()) {
				currentImgX = 0;
				currentImgY++;
			}
			if(currentImgY >= image.getHeight()) {
				stopTask();
				return;
			}
			Scheme s = getScheme(new Color(image.getRGB(currentImgX, currentImgY), Main.getSettings().useAlpha));
			if(s == null) s = isTerrainTask ? Main.getSettings().defaultIfTerrainNull : Main.getSettings().defaultIfBiomeNull;
			if(!isTerrainTask) {
				Block b = world.getHighestBlockAt(currentImgX + startX, currentImgY + startZ);
				if(!b.getChunk().isLoaded()) b.getChunk().load(true);
				b.setBiome(((BiomeScheme) s).getBiome());
				world.refreshChunk(b.getChunk().getX(), b.getChunk().getZ());
			} else {
				TerrainScheme t = (TerrainScheme) s;
				Block b = world.getBlockAt(new Location(world, currentImgX + startX, 0, currentImgY + startZ));
				if(!b.getChunk().isLoaded()) b.getChunk().load(true);
				if(Main.getSettings().alwaysPlaceBedrock) b.setType(Material.BEDROCK);
				for(int i=(Main.getSettings().alwaysPlaceBedrock ? 1 : 0); i<=256; i++){
					MaterialData md = t.getBlocks()[i];
					if(md == null) {
						if(Main.getSettings().airIfNull) b.getRelative(0, i, 0).setType(Material.AIR);
						else continue;
					} else {
						b.getRelative(0, i, 0).setType(md.getItemType());
						b.getRelative(0, i, 0).setData(md.getData());
					}
				}
				
			}
			currentImgX++;
			changes++;
		}
		Main.getSettings().saveTask(this);
		changes = 0;
	}

	public void startPreparing(CommandSender sender, boolean needStartPoint) {
		prepareSteps = new ArrayList<PrepareStep>();
		if(needStartPoint) {
			prepareSteps.add(PrepareStep.NEED_STARTX);
			prepareSteps.add(PrepareStep.NEED_STARTZ);
		}
		prepareSteps.add(PrepareStep.NEED_ACCEPT);
		Main.getSettings().preparingTask.put(sender.getName(), this);
		nextStep(sender, false, "");
	}
	
	public void nextStep(final CommandSender sender, boolean withValue, String value) {
		if(prepareSteps == null || prepareSteps.isEmpty()) return;
		switch(prepareSteps.get(0)) {
		case NEED_STARTX:
			if(withValue) {
				try {
					startX = Integer.parseInt(value);
					prepareSteps.remove(0);
					nextStep(sender, false, "");
				} catch(NumberFormatException e) {
					terminatePreparing(sender, "&7&l[KImageTerrain] &cGiven value must be an integer, task preparing terminated!");
				}
			} else {
				sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aEnter the &7x &acoordinate of the task start point&8:"));
			}
			break;
		case NEED_STARTZ:
			if(withValue) {
				try {
					startZ = Integer.parseInt(value);
					prepareSteps.remove(0);
					nextStep(sender, false, "");
				} catch(NumberFormatException e) {
					terminatePreparing(sender, "&7&l[KImageTerrain] &cGiven value must be an integer, task preparing terminated!");
				}
			} else {
				sender.sendMessage(StringUtils.color("&7&l[KImageTerrain] &aEnter the &7z &acoordinate of the task start point&8:"));
			}
			break;
		case NEED_ACCEPT:
			if(withValue) {
				acceptTask.cancel();
				startTask();
				terminatePreparing(sender, "&7&l[KImageTerrain] &aTask started! Use &7/kterrain tasks &acommand to check its progress!");
			} else {
				sender.sendMessage(StringUtils.color("&8-----------------<[&6&lTask summary&8]>------------------"));
				sender.sendMessage(StringUtils.color("&8&l» &aImage used&8:  &7" + imageName));
				sender.sendMessage(StringUtils.color("&8&l» &aTask type&8:  &7" + (isTerrainTask ? "TERRAIN" : "BIOME")));
				sender.sendMessage(StringUtils.color("&8&l» &aUsed scheme&8:  &7" + (usingUniversalScheme ? "UNIVERSAL" : "OWN")));
				sender.sendMessage(StringUtils.color("&8&l» &aModified world&8:  &7" + world.getName()));
				sender.sendMessage(StringUtils.color("&8&l» &aStart position&8:  &7" + "x&8=&7" + startX + "   z&8=&7" + startZ));
				sender.sendMessage(StringUtils.color("&8&l» &aFinish position&8:  &7" + "x&8=&7" + (startX + image.getWidth()) + "   z&8=&7" + (startZ + image.getHeight())));
				sender.sendMessage(StringUtils.color("&8&l» &aColumns to be changed&8:  &7" + (image.getWidth()*image.getHeight())));
				sender.sendMessage("");
				sender.sendMessage(StringUtils.color("&aTo start this task use&8: &7/kterrain accept <securityPassword>"));
				sender.sendMessage(StringUtils.color("&aYou have &730 seconds &ato accept or the task will be terminated!"));
				sender.sendMessage(StringUtils.color("&8---------------------------------------------------"));
				this.acceptTask = new BukkitRunnable() {
					public void run() {
						terminatePreparing(sender, "&7&l[KImageTerrain] &cTask not accepted, preparing terminated!");
					}
				}.runTaskLater(Main.getInst(), 20*30);
			}
			break;
		default:
			nextStep(sender, false, "");
		}
	}
	
	public void terminatePreparing(CommandSender sender, String msg) {
		Main.getSettings().preparingTask.remove(sender.getName());
		prepareSteps = null;
		if(acceptTask !=null) acceptTask.cancel();
		acceptTask = null;
		sender.sendMessage(StringUtils.color(msg));
	}
	
	public void startTask() {
		Main.getSettings().runningTasks.put(this, new BukkitRunnable() {
			public void run() {
				nextChanges();
			}
		}.runTaskTimer(Main.getInst(), 0, Main.getSettings().intervalBetweenChanges));
		if(isTerrainTask) Main.getSettings().tTasks++;
		else Main.getSettings().bTasks++;
		if(usingUniversalScheme) Main.getSettings().uScheme++;
		else Main.getSettings().oScheme++;
		if(Main.getSettings().broadcastTask) Bukkit.broadcastMessage(StringUtils.color("&7&l[KImageTerrain] &aTask for image &7" + imageName + " &ahas just started!"));
	}
	
	public void stopTask(){
		Main.getSettings().runningTasks.remove(this).cancel();
		Main.getSettings().removeFromSaved(this);
		if(Main.getSettings().broadcastTask) Bukkit.broadcastMessage(StringUtils.color("&7&l[KImageTerrain] &aTask for image &7" + imageName + " &ahas just finished!"));
	}
	
	private Scheme getScheme(Color c) {
		for(Scheme s : usedSchemes) {
			if(c.getRed() == s.getColor().getRed() && c.getGreen() == s.getColor().getGreen() && c.getBlue() == s.getColor().getBlue()){
				if(Main.getSettings().useAlpha) {
					if(c.getAlpha() == s.getColor().getAlpha()) return s;
					else return null;
				}
				return s;
			}
		}
		return null;
	}
	
	public boolean isUsingUniversalScheme() {
		return this.usingUniversalScheme;
	}

	public String getImageName() {
		return this.imageName;
	}

	public int getCurrentImgX() {
		return this.currentImgX;
	}

	public int getCurrentImgY() {
		return this.currentImgY;
	}

	public int getStartX() {
		return this.startX;
	}

	public int getStartZ() {
		return this.startZ;
	}

	public boolean isTerrainTask() {
		return isTerrainTask;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setCurrentImgX(int currentImgX) {
		this.currentImgX = currentImgX;
	}

	public void setCurrentImgY(int currentImgY) {
		this.currentImgY = currentImgY;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public void setStartZ(int startZ) {
		this.startZ = startZ;
	}
	
	public boolean isWaitingForAccept() {
		return this.prepareSteps !=null && !this.prepareSteps.isEmpty() && this.prepareSteps.get(0).equals(PrepareStep.NEED_ACCEPT);
	}
	
	private enum PrepareStep {
		NEED_STARTX,
		NEED_STARTZ,
		NEED_ACCEPT;
	}
}