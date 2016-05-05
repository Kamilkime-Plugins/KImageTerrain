package com.gmail.kamilkime.kimageterrain.data;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.objects.PreparingGUI;
import com.gmail.kamilkime.kimageterrain.objects.Task;
import com.gmail.kamilkime.kimageterrain.objects.scheme.BiomeScheme;
import com.gmail.kamilkime.kimageterrain.objects.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.objects.scheme.TerrainScheme;

public class Settings {
	
	private YamlConfiguration saveYml;
	public String securityPassword;
	public boolean useAlpha;
	public boolean alwaysPlaceBedrock;
	public boolean airIfNull;
	public boolean resumeSavedTasks;
	public boolean broadcastTask;
	public int terrainChangeAtOnce;
	public int biomeChangeAtOnce;
	public int intervalBetweenChanges;
	public TerrainScheme defaultIfTerrainNull;
	public BiomeScheme defaultIfBiomeNull;
	public Collection<Scheme> universalTerrainScheme = new HashSet<Scheme>();
	public Collection<Scheme> universalBiomeScheme = new HashSet<Scheme>();
	public Map<Task, BukkitTask> runningTasks = new HashMap<Task, BukkitTask>();
	public Map<String, Task> preparingTask = new HashMap<String, Task>();
	public Map<UUID, PreparingGUI> preparingGUIs = new HashMap<UUID, PreparingGUI>();
	
	public int tTasks;
	public int bTasks;
	public int uScheme;
	public int oScheme;
	public int byCmd;
	public int byGui;
	
	public void load() {
		loadConfig();
		if(resumeSavedTasks) {
			if(saveYml.getConfigurationSection("tasks") !=null && saveYml.getConfigurationSection("tasks").getKeys(false) != null
					&& !saveYml.getConfigurationSection("tasks").getKeys(false).isEmpty()) {
				for(String taskName : saveYml.getConfigurationSection("tasks").getKeys(false)) {
					ConfigurationSection cs = saveYml.getConfigurationSection("tasks." + taskName);
					Task t = new Task(taskName, (cs.getBoolean("usingUniversalScheme") ? (cs.getBoolean("isTerrainTask") ? universalTerrainScheme :
							universalBiomeScheme) : FileManager.getSchemesForImage(taskName, cs.getBoolean("isTerrainTask"))),
							cs.getBoolean("usingUniversalScheme"), cs.getBoolean("isTerrainTask"), Bukkit.getWorld(cs.getString("world")));
					t.setCurrentImgX(cs.getInt("currentImgX"));
					t.setCurrentImgY(cs.getInt("currentImgY"));
					t.setStartX(cs.getInt("startX"));
					t.setStartZ(cs.getInt("startZ"));
					t.startTask();
				}
			}
		}
		try {
			saveYml.set("tasks", null);
			saveYml.save(FileManager.SAVE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tTasks = saveYml.getInt("tTasks", 0);
		bTasks = saveYml.getInt("bTasks", 0);
		uScheme = saveYml.getInt("uScheme", 0);
		oScheme = saveYml.getInt("oScheme", 0);
		byCmd = saveYml.getInt("byCmd", 0);
		byGui = saveYml.getInt("byGui", 0);
	}
	
	public void save() {
		FileManager.checkFiles();
		for(Task task : runningTasks.keySet()) saveTask(task);
		saveYml.set("tTasks", tTasks);
		saveYml.set("bTasks", bTasks);
		saveYml.set("uScheme", uScheme);
		saveYml.set("oScheme", oScheme);
		saveYml.set("byCmd", byCmd);
		saveYml.set("byGui", byGui);
		try {
			saveYml.save(FileManager.SAVE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig() {
		Main.getInst().reloadConfig();
		loadConfig();
	}
	
	public void removeFromSaved(Task task) {
		FileManager.checkFiles();
		saveYml.set("tasks." + task.getImageName(), null);
		try {
			saveYml.save(FileManager.SAVE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveTask(Task task) {
		FileManager.checkFiles();
		if(saveYml == null) saveYml = YamlConfiguration.loadConfiguration(FileManager.SAVE_FILE);
		saveYml.set("tasks." + task.getImageName() + ".usingUniversalScheme", task.isUsingUniversalScheme());
		saveYml.set("tasks." + task.getImageName() + ".isTerrainTask", task.isTerrainTask());
		saveYml.set("tasks." + task.getImageName() + ".world", task.getWorld().getName());
		saveYml.set("tasks." + task.getImageName() + ".currentImgX", task.getCurrentImgX());
		saveYml.set("tasks." + task.getImageName() + ".currentImgY", task.getCurrentImgY());
		saveYml.set("tasks." + task.getImageName() + ".startX", task.getStartX());
		saveYml.set("tasks." + task.getImageName() + ".startZ", task.getStartZ());
		try {
			saveYml.save(FileManager.SAVE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadConfig() {
		FileManager.checkFiles();
		ConfigUtils.check();
		if(saveYml == null) saveYml = YamlConfiguration.loadConfiguration(FileManager.SAVE_FILE);
		this.securityPassword = Main.getInst().getConfig().getString("securityPassword");
		this.useAlpha = Main.getInst().getConfig().getBoolean("useAlpha", false);
		this.alwaysPlaceBedrock = Main.getInst().getConfig().getBoolean("alwaysPlaceBedrock", true);
		this.airIfNull = Main.getInst().getConfig().getBoolean("airIfNull", true);
		this.resumeSavedTasks = Main.getInst().getConfig().getBoolean("resumeSavedTasks", true);
		this.broadcastTask = Main.getInst().getConfig().getBoolean("broadcastTask", true);
		this.terrainChangeAtOnce = Main.getInst().getConfig().getInt("terrainChangeAtOnce", 1000);
		this.biomeChangeAtOnce = Main.getInst().getConfig().getInt("biomeChangeAtOnce", 20000);
		this.intervalBetweenChanges = Main.getInst().getConfig().getInt("intervalBetweenChanges", 30);
		
		defaultIfTerrainNull = (TerrainScheme) FileManager.loadScheme(Main.getInst().getConfig().getString("defaultIfTerrainNull"), true, true);
		defaultIfBiomeNull = (BiomeScheme) FileManager.loadScheme(Main.getInst().getConfig().getString("defaultIfBiomeNull"), false, true);
		
		for(String s : Main.getInst().getConfig().getStringList("universalTerrainScheme")) universalTerrainScheme.add(FileManager.loadScheme(s, true, false));
		for(String s : Main.getInst().getConfig().getStringList("universalBiomeScheme")) universalBiomeScheme.add(FileManager.loadScheme(s, false, false));
	}
}