package com.solexgames.mlg.leaderboard;

import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author puugz
 * @since 30/05/2021 20:09
 */
@Getter
public class Leaderboard {

	private final Map<String, Integer> leaderboard;
	private final String name;
	private final String object;

	private final int amount;

	public Leaderboard(String name, String object, int amount) {
		this.leaderboard = new HashMap<>();
		this.name = name;
		this.object = object;
		this.amount = amount;

		this.load();
	}

	public void update() {
		this.leaderboard.clear();
		this.load();
	}

	public void load() {
		CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getMongoHandler().getPlayerCollection().find().sort(new Document(this.object, -1)).limit(this.amount).iterator())
				.thenAccept(cursor -> {
					final Map<String, Integer> map = new HashMap<>();

					cursor.forEachRemaining(document -> map.put(document.getString("name"), document.getInteger(this.object)));
					map.keySet().iterator().forEachRemaining(s -> this.leaderboard.put(s, map.get(s)));
				});
	}
}
