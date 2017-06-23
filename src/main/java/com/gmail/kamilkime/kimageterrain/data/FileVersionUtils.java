package com.gmail.kamilkime.kimageterrain.data;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.kamilkime.kimageterrain.Main;

@SuppressWarnings("deprecation")
public class FileVersionUtils {

	private final static int cfgVersion = YamlConfiguration.loadConfiguration(Main.getInst().getResource("config.yml")).getInt("configVersion");
	private final static int msgVersion = YamlConfiguration.loadConfiguration(Main.getInst().getResource("messages.yml")).getInt("messagesVersion");

	public static void checkCfg() {
		File f = new File(Main.getInst().getDataFolder(), "configOld@" + System.currentTimeMillis() + ".yml");
		if(Main.getInst().getConfig().get("configVersion") == null || Main.getInst().getConfig().getInt("configVersion") !=cfgVersion) {
			FileManager.CFG_FILE.renameTo(f);
			Main.getInst().saveResource("config.yml", true);
			return;
		}
		File temp = new File(Main.getInst().getDataFolder(), "tempCfg.yml");
		FileManager.CFG_FILE.renameTo(temp);
		Main.getInst().saveResource("config.yml", true);
		YamlConfiguration cYml = YamlConfiguration.loadConfiguration(FileManager.CFG_FILE);
		YamlConfiguration tYml = YamlConfiguration.loadConfiguration(temp);
		for(String s : cYml.getKeys(false)) {
			if(tYml.get(s) == null) {
				temp.renameTo(f);
				return;
			}
		}
		FileManager.CFG_FILE.delete();
		temp.renameTo(FileManager.CFG_FILE);
	}
	
	public static void checkMsg() {
		File f = new File(Main.getInst().getDataFolder(), "messagesOld@" + System.currentTimeMillis() + ".yml");
		YamlConfiguration msgYml = YamlConfiguration.loadConfiguration(FileManager.MSG_FILE);
		if(msgYml.get("messagesVersion") == null || msgYml.getInt("messagesVersion") !=msgVersion) {
			FileManager.MSG_FILE.renameTo(f);
			Main.getInst().saveResource("messages.yml", true);
			return;
		}
		File temp = new File(Main.getInst().getDataFolder(), "tempMsg.yml");
		FileManager.MSG_FILE.renameTo(temp);
		Main.getInst().saveResource("messages.yml", true);
		YamlConfiguration cYml = YamlConfiguration.loadConfiguration(FileManager.MSG_FILE);
		for(String s : cYml.getKeys(false)) {
			if(msgYml.get(s) == null) {
				temp.renameTo(f);
				return;
			}
		}
		FileManager.MSG_FILE.delete();
		temp.renameTo(FileManager.MSG_FILE);
	}
}