package com.gmail.kamilkime.kimageterrain.task.prepared;

import java.util.Collection;

import org.bukkit.World;

import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.task.BiomeTask;

public class PreparedBiomeTask extends PreparedTask {

	public PreparedBiomeTask(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		super(imageName, usedSchemes, usingUniversalScheme, world);
	}

	@Override
	public boolean isTerrainTask() {
		return false;
	}

	@Override
	public void start() {
		BiomeTask t = new BiomeTask(imageName, usedSchemes, usingUniversalScheme, world);
		t.setStartX(startX);
		t.setStartZ(startZ);
		t.startTask();
	}
}