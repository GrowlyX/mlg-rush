package com.solexgames.mlg.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author puugz
 * @since 28/05/2021 20:39
 */
public class VoidWorldGenerator extends ChunkGenerator {

	@Override
	public byte[] generate(World world, Random random, int x, int z) {
		return new byte[32768];
	}

	@Override
	public boolean canSpawn(World world, int x, int z) {
		return true;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList(new BlockPopulator[0]);
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 0, 62, 0);
	}
}
