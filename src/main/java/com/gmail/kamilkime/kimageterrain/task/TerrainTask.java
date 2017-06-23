package com.gmail.kamilkime.kimageterrain.task;

import java.awt.Color;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.scheme.TerrainScheme;

public class TerrainTask extends Task{

	public TerrainTask(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		super(imageName, usedSchemes, usingUniversalScheme, world);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void change() {
		Scheme s = getScheme(new Color(image.getRGB(currentImgX, currentImgY), Main.getSettings().useAlpha));
		if(s == null) s = Main.getSettings().defaultIfTerrainNull;
		Block b = world.getBlockAt(new Location(world, currentImgX + startX, 0, currentImgY + startZ));
		if(!b.getChunk().isLoaded()) b.getChunk().load(true);
		if(Main.getSettings().alwaysPlaceBedrock) b.setType(Material.BEDROCK);
		for(int i=(Main.getSettings().alwaysPlaceBedrock ? 1 : 0); i<=256; i++){
			MaterialData md = ((TerrainScheme) s).getBlocks()[i];
			if(md == null) {
				if(Main.getSettings().airIfNull) b.getRelative(0, i, 0).setType(Material.AIR);
				else continue;
			} else {
				b.getRelative(0, i, 0).setType(md.getItemType());
				b.getRelative(0, i, 0).setData(md.getData());
			}
		}
	}

	@Override
	public boolean isTerrainTask() {
		return true;
	}

	@Override
	public int getChanges() {
		return Main.getSettings().terrainChangeAtOnce;
	}

	@Override
	public void incrementTaskType() {
		Main.getSettings().tTasks++;
	}
	
	@Override
	public void start(){}
}