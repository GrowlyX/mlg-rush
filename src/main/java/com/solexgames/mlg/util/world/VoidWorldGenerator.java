package com.solexgames.mlg.util.world;

import com.solexgames.mlg.CorePlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.Collections;
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
		return Collections.emptyList();
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return CorePlugin.getInstance().getLocationHandler().getSpawnLocation();
	}
}
