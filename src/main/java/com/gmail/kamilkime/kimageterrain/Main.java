package com.gmail.kamilkime.kimageterrain;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.gmail.kamilkime.kimageterrain.commands.MainCmd;
import com.gmail.kamilkime.kimageterrain.commands.TabCompletion;
import com.gmail.kamilkime.kimageterrain.data.Settings;
import com.gmail.kamilkime.kimageterrain.data.StringUtils;
import com.gmail.kamilkime.kimageterrain.gui.PreparingGUI;
import com.gmail.kamilkime.kimageterrain.listeners.InventoryListener;
import com.gmail.kamilkime.kimageterrain.listeners.PlayerListener;

public class Main extends JavaPlugin {

	private Metrics metrics;
	private static Main inst;
	private static Settings settings;
	
	public Main() {
		inst = this;
	}
	
	@Override
	public void onEnable() {
		getSettings().load();
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		getCommand("kterrain").setExecutor(new MainCmd());
		getCommand("kterrain").setTabCompleter(new TabCompletion());
		startMetrics();
	}
	
	@Override
	public void onDisable() {
		for(PreparingGUI gui : getSettings().preparingGUIs.values()) gui.cancel();
		getSettings().save();
	}
	
	public static Main getInst() {
		return inst;
	}
	
	public static Settings getSettings(){
		if(settings == null) settings = new Settings();
		return settings;
	}
	
	public static void error(String msg){
		Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7&l[KImageTerrain] &c&l" + msg));
	}
	
	public static void info(String msg){
		Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7&l[KImageTerrain] &a&l" + msg));
	}
	
	private void startMetrics() {
		if(metrics == null) {
			try {
				metrics = new Metrics(Main.getInst());
			} catch(Exception e) {
				Main.error("Plugin metrics cannot be enabled!");
				return;
			}
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			public void run() {
				
				Metrics.Graph taskGraph = metrics.createGraph("Task types");
				taskGraph.addPlotter(new Metrics.Plotter("Terrain") {
					@Override
					public int getValue() {
						return getSettings().tTasks;
					}
				});
				taskGraph.addPlotter(new Metrics.Plotter("Biome") {
					@Override
					public int getValue() {
						return getSettings().bTasks;
					}
				});
				
				Metrics.Graph schemeGraph = metrics.createGraph("Used schemes");
				schemeGraph.addPlotter(new Metrics.Plotter("Universal") {
					@Override
					public int getValue() {
						return getSettings().uScheme;
					}
				});
				schemeGraph.addPlotter(new Metrics.Plotter("Own") {
					@Override
					public int getValue() {
						return getSettings().oScheme;
					}
				});
				
				Metrics.Graph creationGraph = metrics.createGraph("Task creation");
				creationGraph.addPlotter(new Metrics.Plotter("Command") {
					@Override
					public int getValue() {
						return getSettings().byCmd;
					}
				});
				
				creationGraph.addPlotter(new Metrics.Plotter("GUI") {
					@Override
					public int getValue() {
						return getSettings().byGui;
					}
				});
				
				metrics.start();
				Main.info("Plugin metrics successfully enabled!");
			}
		}, 20);
	}
}