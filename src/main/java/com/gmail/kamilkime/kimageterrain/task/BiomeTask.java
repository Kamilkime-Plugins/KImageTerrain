package com.gmail.kamilkime.kimageterrain.task;

import java.awt.Color;
import java.util.Collection;

import org.bukkit.World;
import org.bukkit.block.Block;

import com.gmail.kamilkime.kimageterrain.Main;
import com.gmail.kamilkime.kimageterrain.scheme.BiomeScheme;
import com.gmail.kamilkime.kimageterrain.scheme.Scheme;

public class BiomeTask extends Task {

	public BiomeTask(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		super(imageName, usedSchemes, usingUniversalScheme, world);
	}

	@Override
	public void change() {
		Scheme s = getScheme(new Color(image.getRGB(currentImgX, currentImgY), Main.getSettings().useAlpha));
		if(s == null) s = Main.getSettings().defaultIfBiomeNull;
		Block b = world.getHighestBlockAt(currentImgX + startX, currentImgY + startZ);
		if(!b.getChunk().isLoaded()) b.getChunk().load(true);
		b.setBiome(((BiomeScheme) s).getBiome());
		world.refreshChunk(b.getChunk().getX(), b.getChunk().getZ());
	}

	@Override
	public boolean isTerrainTask() {
		return false;
	}

	@Override
	public int getChanges() {
		return Main.getSettings().biomeChangeAtOnce;
	}

	@Override
	public void incrementTaskType() {
		Main.getSettings().bTasks++;
	}

	@Override
	public void start(){}
}