package com.gmail.kamilkime.kimageterrain.task;

import java.awt.Color;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTask;

public abstract class Task extends PreparedTask {

	private BukkitTask mainTask;
	protected int currentImgX = 0;
	protected int currentImgY = 0;

	public abstract void change();
	public abstract void incrementTaskType();
	public abstract int getChanges();
	public abstract boolean isTerrainTask();
	
	public Task(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		super(imageName, usedSchemes, usingUniversalScheme, world);
	}
	
	public void nextChanges() {
		for(int c=0; c < getChanges(); c++) {
			if(currentImgX >= image.getWidth()) {
				currentImgX = 0;
				currentImgY++;
			}
			if(currentImgY >= image.getHeight()) {
				stopTask();
				return;
			}
			change();
			currentImgX++;
		}
		Main.getSettings().saveTask(this);
	}

	public void startTask() {
		Main.getSettings().runningTasks.add(this);
		this.mainTask = new BukkitRunnable() {
			public void run() {
				nextChanges();
			}
		}.runTaskTimer(Main.getInst(), 0, Main.getSettings().intervalBetweenChanges);
		incrementTaskType();
		if(usingUniversalScheme) Main.getSettings().uScheme++;
		else Main.getSettings().oScheme++;
		if(Main.getSettings().broadcastTask) Bukkit.broadcastMessage(StringUtils.getMessage("broadcastTask", StringUtils.getMessage("prefix"), imageName, "started"));
	}
	
	public void stopTask() {
		this.mainTask.cancel();
		Main.getSettings().runningTasks.remove(this);
		Main.getSettings().removeFromSaved(this);
		if(Main.getSettings().broadcastTask) Bukkit.broadcastMessage(StringUtils.getMessage("broadcastTask", StringUtils.getMessage("prefix"), imageName, "finished"));
	}
	
	protected Scheme getScheme(Color c) {
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
	
	public int getCurrentImgX() {
		return this.currentImgX;
	}

	public int getCurrentImgY() {
		return this.currentImgY;
	}
	
	public void setCurrentImgX(int currentImgX) {
		this.currentImgX = currentImgX;
	}

	public void setCurrentImgY(int currentImgY) {
		this.currentImgY = currentImgY;
	}
}