package com.gmail.kamilkime.kimageterrain.task.prepared;

import java.util.Collection;

import org.bukkit.World;

import com.gmail.kamilkime.kimageterrain.scheme.Scheme;
import com.gmail.kamilkime.kimageterrain.task.TerrainTask;

public class PreparedTerrainTask extends PreparedTask {

	public PreparedTerrainTask(String imageName, Collection<Scheme> usedSchemes, boolean usingUniversalScheme, World world) {
		super(imageName, usedSchemes, usingUniversalScheme, world);
	}

	@Override
	public boolean isTerrainTask() {
		return true;
	}

	@Override
	public void start() {
		TerrainTask t = new TerrainTask(imageName, usedSchemes, usingUniversalScheme, world);
		t.setStartX(startX);
		t.setStartZ(startZ);
		t.startTask();
	}
}