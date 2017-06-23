package com.gmail.kamilkime.kimageterrain.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.gui.PreparingGUI;
import com.gmail.kamilkime.kimageterrain.scheme.BiomeScheme;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.scheme.TerrainScheme;
import com.gmail.kamilkime.kimageterrain.task.BiomeTask;
import com.gmail.kamilkime.kimageterrain.task.Task;
import com.gmail.kamilkime.kimageterrain.task.TerrainTask;
import com.gmail.kamilkime.kimageterrain.task.prepared.PreparedTask;

public class Settings {
	
	private FileConfiguration cfg;
	private YamlConfiguration saveYml;
	private YamlConfiguration msgYml;
	public String securityPassword;
	public boolean useAlpha;
	public boolean alwaysPlaceBedrock;
	public boolean airIfNull;
	public boolean resumeSavedTasks;
	public boolean broadcastTask;
	public boolean requireSecurityPassword;
	public int terrainChangeAtOnce;
	public int biomeChangeAtOnce;
	public int intervalBetweenChanges;
	public TerrainScheme defaultIfTerrainNull;
	public BiomeScheme defaultIfBiomeNull;
	public List<Task> runningTasks = new ArrayList<Task>();
	public Collection<Scheme> universalTerrainScheme = new HashSet<Scheme>();
	public Collection<Scheme> universalBiomeScheme = new HashSet<Scheme>();
	public Map<String, PreparedTask> preparingTask = new HashMap<String, PreparedTask>();
	public Map<UUID, PreparingGUI> preparingGUIs = new HashMap<UUID, PreparingGUI>();
	public Map<String, String> messages = new HashMap<String, String>();
	
	public int tTasks;
	public int bTasks;
	public int uScheme;
	public int oScheme;
	public int byCmd;
	public int byGui;
	
	public void load() {
		loadConfig();
		loadMessages();
		if(resumeSavedTasks) {
			if(saveYml.getConfigurationSection("tasks") !=null && saveYml.getConfigurationSection("tasks").getKeys(false) != null
					&& !saveYml.getConfigurationSection("tasks").getKeys(false).isEmpty()) {
				for(String taskName : saveYml.getConfigurationSection("tasks").getKeys(false)) {
					ConfigurationSection cs = saveYml.getConfigurationSection("tasks." + taskName);
					Task t = null;
					if(cs.getBoolean("isTerrainTask")) { 
						t = new TerrainTask(taskName, (cs.getBoolean("usingUniversalScheme") ? universalTerrainScheme : FileManager.getSchemesForImage(taskName, true)),
							cs.getBoolean("usingUniversalScheme"), Bukkit.getWorld(cs.getString("world")));
					} else {
						t = new BiomeTask(taskName, (cs.getBoolean("usingUniversalScheme") ? universalBiomeScheme : FileManager.getSchemesForImage(taskName, false)),
								cs.getBoolean("usingUniversalScheme"), Bukkit.getWorld(cs.getString("world")));
					}
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
		for(Task task : runningTasks) saveTask(task);
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
		loadMessages();
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
		FileVersionUtils.checkCfg();
		if(cfg == null) cfg = Main.getInst().getConfig();
		if(saveYml == null) saveYml = YamlConfiguration.loadConfiguration(FileManager.SAVE_FILE);
		this.securityPassword = cfg.getString("securityPassword");
		this.useAlpha = cfg.getBoolean("useAlpha", false);
		this.alwaysPlaceBedrock = cfg.getBoolean("alwaysPlaceBedrock", true);
		this.airIfNull = cfg.getBoolean("airIfNull", true);
		this.resumeSavedTasks = cfg.getBoolean("resumeSavedTasks", true);
		this.broadcastTask = cfg.getBoolean("broadcastTask", true);
		this.requireSecurityPassword = cfg.getBoolean("requireSecurityPassword", true);
		this.terrainChangeAtOnce = cfg.getInt("terrainChangeAtOnce", 1000);
		this.biomeChangeAtOnce = cfg.getInt("biomeChangeAtOnce", 20000);
		this.intervalBetweenChanges = cfg.getInt("intervalBetweenChanges", 30);
		
		defaultIfTerrainNull = (TerrainScheme) FileManager.loadScheme(cfg.getString("defaultIfTerrainNull"), true, true);
		defaultIfBiomeNull = (BiomeScheme) FileManager.loadScheme(cfg.getString("defaultIfBiomeNull"), false, true);
		
		for(String s : cfg.getStringList("universalTerrainScheme")) universalTerrainScheme.add(FileManager.loadScheme(s, true, false));
		for(String s : cfg.getStringList("universalBiomeScheme")) universalBiomeScheme.add(FileManager.loadScheme(s, false, false));
	}
	
	private void loadMessages() {
		FileManager.checkFiles();
		FileVersionUtils.checkMsg();
		if(msgYml == null) msgYml = YamlConfiguration.loadConfiguration(FileManager.MSG_FILE);
		for(String s : msgYml.getKeys(false)) {
			if(s.equalsIgnoreCase("messagesVersion")) continue;
			messages.put(s, StringUtils.color(msgYml.getString(s)));
		}
	}
}