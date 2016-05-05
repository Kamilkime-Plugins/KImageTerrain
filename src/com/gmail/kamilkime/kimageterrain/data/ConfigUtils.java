package com.gmail.kamilkime.kimageterrain.data;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.kamilkime.kimageterrain.Main;

public class ConfigUtils {

	@SuppressWarnings("deprecation")
	private final static int version = YamlConfiguration.loadConfiguration(Main.getInst().getResource("config.yml")).getInt("configVersion");
	private static File mainDir = Main.getInst().getDataFolder();
	private static File cfgFile = new File(mainDir, "config.yml");
	
	public static void check() {
		File f = new File(mainDir, "configOld@" + System.currentTimeMillis() + ".yml");
		if(Main.getInst().getConfig().get("configVersion") == null || Main.getInst().getConfig().getInt("configVersion") !=version) {
			cfgFile.renameTo(f);
			Main.getInst().saveResource("config.yml", true);
			return;
		}
		File temp = new File(mainDir, "tempCfg.yml");
		cfgFile.renameTo(temp);
		Main.getInst().saveResource("config.yml", true);
		YamlConfiguration cYml = YamlConfiguration.loadConfiguration(cfgFile);
		YamlConfiguration tYml = YamlConfiguration.loadConfiguration(temp);
		for(String s : cYml.getKeys(false)) {
			if(tYml.get(s) == null) {
				temp.renameTo(f);
				return;
			}
		}
		cfgFile.delete();
		temp.renameTo(cfgFile);
	}
}