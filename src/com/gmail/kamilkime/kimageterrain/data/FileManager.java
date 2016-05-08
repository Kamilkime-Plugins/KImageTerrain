package com.gmail.kamilkime.kimageterrain.data;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.scheme.BiomeScheme;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.scheme.TerrainScheme;

public class FileManager {

	public static final File CFG_FILE = new File(Main.getInst().getDataFolder(), "config.yml");
	public static final File MSG_FILE = new File(Main.getInst().getDataFolder(), "messages.yml");
	public static final File SAVE_FILE = new File(Main.getInst().getDataFolder(), "savedData.yml");
	public static final File IMG_FOLDER = new File(Main.getInst().getDataFolder(), "images");
	
	public static void checkFiles() {
		try {
			if(!Main.getInst().getDataFolder().exists()) Main.getInst().getDataFolder().mkdir();
			if(!CFG_FILE.exists()) Main.getInst().saveDefaultConfig();
			if(!MSG_FILE.exists()) Main.getInst().saveResource("messages.yml", true);
			if(!SAVE_FILE.exists()) SAVE_FILE.createNewFile();
			if(!IMG_FOLDER.exists()) IMG_FOLDER.mkdir();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scheme loadScheme(String s, boolean terrain, boolean ignoreColor) {
		Color c; Scheme scheme;
		String[] spl = s.split(" ");
		if(ignoreColor) {
			c = new Color(0,0,0,0);
		} else {
			String[] color = spl[0].split(",");
			try {
				c = new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]), (color.length > 3 ? Integer.parseInt(color[3]) : 255));
			} catch(Exception e) {
				Main.error(spl[0] + " is not a valid color!");
				return null;
			}
		}
		if(terrain) {
			MaterialData[] blocks = new MaterialData[257];
			for(int arg=(ignoreColor ? 0 : 1); arg<spl.length; arg++) {
				String[] part = spl[arg].split("@");
				int a,b;
				if(part[1].contains("-")) {
					try {
						a = Integer.parseInt(part[1].split("-")[0]);
						b = Integer.parseInt(part[1].split("-")[1]);
					} catch (Exception e) {
						Main.error("&a&l" + part[1].split("-")[0] + " &cAND&7/&cOR &a&l" + part[1].split("-")[1] + " &care not valid integers!");
						continue;
					}
				} else {
					try {
						a = Integer.parseInt(part[1]);
						b = Integer.parseInt(part[1]);
					} catch (Exception e) {
						Main.error(Integer.parseInt(part[1]) + " is not a valid integer!");
						continue;
					}
				}
				MaterialData md = stringToMaterialData(part[0]);
				for(;a<=b; a++) blocks[a] = md;
			}
			scheme = new TerrainScheme(c, blocks);
		} else {
			if(Biome.valueOf(spl[(ignoreColor ? 0 : 1)].toUpperCase()) == null) {
				Main.error("No biome named " + spl[1].toUpperCase() + " found!");
				return null;
			}
			scheme = new BiomeScheme(c, Biome.valueOf(spl[(ignoreColor ? 0 : 1)].toUpperCase()));
		}
		return scheme;
	}
	
	@SuppressWarnings("deprecation")
	public static MaterialData stringToMaterialData(String s) {
		if(s.contains(":")) {
			String[] spl = s.split(":");
			if(Material.getMaterial(spl[0].toUpperCase()) == null) {
				Main.error("No material named " + spl[0].toUpperCase() + " found!");
				return null;
			}
			return new MaterialData(Material.getMaterial(spl[0].toUpperCase()), Byte.parseByte(spl[1]));
		} else {
			if(Material.getMaterial(s.toUpperCase()) == null) {
				Main.error("No material named " + s.toUpperCase() + " found!");
				return null;
			}
			return new MaterialData(Material.getMaterial(s.toUpperCase()), (byte) 0);
		}
	}
	
	public static List<File> getImages() {
		List<File> files = new ArrayList<File>();
		for(File f : IMG_FOLDER.listFiles()) {
			if(f.getName().toLowerCase().contains(".png")) files.add(f);
		}
		return files;
	}
	
	public static List<String> getImagesNames() {
		List<String> names = new ArrayList<String>();
		for(File f : IMG_FOLDER.listFiles()) {
			if(f.getName().toLowerCase().contains(".png")) names.add(f.getName().toLowerCase().replace(".png", ""));
		}
		return names;
	}
	
	public static BufferedImage getImage(String imageName) {
		for(File f : IMG_FOLDER.listFiles()) {
			if(f.getName().toLowerCase().replace(".png", "").equalsIgnoreCase(imageName)) {
				try {
					return ImageIO.read(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static boolean[] imageHasSchemes(String imageName) {
		boolean[] toReturn = new boolean[]{false,false,false};
		for(File f : IMG_FOLDER.listFiles()) {
			if(f.getName().toLowerCase().replace(".yml", "").equalsIgnoreCase(imageName.toLowerCase())) {
				toReturn[0] = true;
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
				if(yml.get("terrainScheme") !=null) toReturn[1] = true;
				if(yml.get("biomeScheme") !=null) toReturn[2] = true;
			}
		}
		return toReturn;
	}
	
	public static File getSchemeFile(String imageName) {
		for(File f : IMG_FOLDER.listFiles()) {
			if(f.getName().toLowerCase().replace(".yml", "").equalsIgnoreCase(imageName.toLowerCase())) return f;
		}
		return null;
	}
	
	public static Collection<Scheme> getSchemesForImage(String imageName, boolean terrain) {
		Collection<Scheme> toReturn = new HashSet<Scheme>();
		boolean[] hasSchemes = imageHasSchemes(imageName);
		if(!hasSchemes[0]) return toReturn;
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(getSchemeFile(imageName));
		if(terrain) {
			if(!hasSchemes[1]) return toReturn;
			for(String s : yml.getStringList("terrainScheme")) toReturn.add(loadScheme(s, true, false));
		} else {
			if(!hasSchemes[2]) return toReturn;
			for(String s : yml.getStringList("biomeScheme")) toReturn.add(loadScheme(s, false, false));
		}
		
		return toReturn;
	}
}