package com.gmail.kamilkime.kimageterrain.objects.scheme;

import java.awt.Color;

import org.bukkit.material.MaterialData;

public class TerrainScheme extends Scheme {

	private MaterialData[] blocks;
	
	public TerrainScheme(Color color, MaterialData[] blocks) {
		super(color);
		this.blocks = blocks;
	}
	
	public MaterialData[] getBlocks(){
		return this.blocks;
	}
}