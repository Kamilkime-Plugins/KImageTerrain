package com.gmail.kamilkime.kimageterrain.scheme;

import java.awt.Color;

import org.bukkit.block.Biome;

public class BiomeScheme extends Scheme {

	private Biome biome;
	
	public BiomeScheme(Color color, Biome biome) {
		super(color);
		this.biome = biome;
	}
	
	public Biome getBiome() {
		return this.biome;
	}
}