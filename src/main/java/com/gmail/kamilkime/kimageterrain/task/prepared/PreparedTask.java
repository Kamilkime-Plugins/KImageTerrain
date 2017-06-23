package com.gmail.kamilkime.kimageterrain.task.prepared;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.FileManager;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;

public abstract class PreparedTask {

	private List<PrepareStep> prepareSteps;
	protected Collection<Scheme> usedSchemes;
	private BukkitTask acceptTask;
	protected BufferedImage image;
	protected boolean usingUniversalScheme;
	protected String imageName;
	protected World world;
	protected int startX = 0;
	protected int startZ = 0;
	
	public abstract boolean isTerrainTask();
	public abstract void start();
	
	public PreparedTask(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		this.imageName = imageName;
		this.usedSchemes = usedSchemes;
		this.usingUniversalScheme = usingUniversalScheme;
		this.world = world;
		this.image = FileManager.getImage(imageName);
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
					terminatePreparing(sender, StringUtils.getMessage("valueNotInteger", StringUtils.getMessage("prefix")));
				}
			} else {
				sender.sendMessage(StringUtils.getMessage("enterStartCoordinate", StringUtils.getMessage("prefix"), "x"));
			}
			break;
		case NEED_STARTZ:
			if(withValue) {
				try {
					startZ = Integer.parseInt(value);
					prepareSteps.remove(0);
					nextStep(sender, false, "");
				} catch(NumberFormatException e) {
					terminatePreparing(sender, StringUtils.getMessage("valueNotInteger", StringUtils.getMessage("prefix")));
				}
			} else {
				sender.sendMessage(StringUtils.getMessage("enterStartCoordinate", StringUtils.getMessage("prefix"), "z"));
			}
			break;
		case NEED_ACCEPT:
			if(withValue) {
				acceptTask.cancel();
				start();
				terminatePreparing(sender, StringUtils.getMessage("taskStarted", StringUtils.getMessage("prefix")));
			} else {
				sender.sendMessage(StringUtils.color("&8-----------------<[&6&lTask summary&8]>------------------"));
				sender.sendMessage(StringUtils.color("&8&l» &aImage used&8:  &7" + imageName));
				sender.sendMessage(StringUtils.color("&8&l» &aTask type&8:  &7" + (isTerrainTask() ? "TERRAIN" : "BIOME")));
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
						terminatePreparing(sender, StringUtils.getMessage("taskNotAccepted", StringUtils.getMessage("prefix")));
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
		sender.sendMessage(msg);
	}
	
	public boolean isWaitingForAccept() {
		return this.prepareSteps !=null && !this.prepareSteps.isEmpty() && this.prepareSteps.get(0).equals(PrepareStep.NEED_ACCEPT);
	}
	
	public boolean isUsingUniversalScheme() {
		return this.usingUniversalScheme;
	}

	public String getImageName() {
		return this.imageName;
	}

	public int getStartX() {
		return this.startX;
	}

	public int getStartZ() {
		return this.startZ;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public Collection<Scheme> getUsedSchemes() {
		return this.usedSchemes;
	}
	
	public void setStartX(int startX) {
		this.startX = startX;
	}

	public void setStartZ(int startZ) {
		this.startZ = startZ;
	}
	
	private enum PrepareStep {
		NEED_STARTX,
		NEED_STARTZ,
		NEED_ACCEPT;
	}
}