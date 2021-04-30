package com.solexgames.mlg.player;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Player profile where statistics are saved.
 * <p>
 *
 * @author GrowlyX
 * @since 3/25/2021
 */

@Getter
@Setter
public class GamePlayer {

    @SerializedName("_id")
    private final UUID uuid;
    private final String name;

    private double kdr;
    private int kills;
    private int deaths;

    private int wins;
    private int losses;

    private boolean inGame = false;

    /**
     * Creates a new instance of {@link GamePlayer}
     * <p>
     *
     * @param uuid Player UUID
     * @param name Player Name
     */
    public GamePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.loadPlayerData();
    }

    public void savePlayerData() {
        CompletableFuture.runAsync(() -> {
            CorePlugin.getInstance().getMongoHandler().getPlayerCollection().replaceOne(Filters.eq("_id", this.uuid), this.getDocument(), new ReplaceOptions().upsert(true));
            CorePlugin.getInstance().getPlayerHandler().getPlayerList().remove(this);
        });
    }

    public Document getDocument() {
        final Document document = new Document("_id", this.uuid);

        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);

        document.put("kdr", this.kdr);
        document.put("kills", this.kills);
        document.put("deaths", this.deaths);

        document.put("totalWins", this.wins);
        document.put("totalLosses", this.losses);

        return document;
    }

    private void loadPlayerData() {
        CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("_id", this.uuid)).first())
                .thenAccept(document -> {
                    if (document == null) {
                        CorePlugin.getInstance().getServer().getScheduler()
                                .runTaskLaterAsynchronously(CorePlugin.getInstance(), this::savePlayerData, 20L);
                        return;
                    }

                    if (document.getDouble("kdr") != null) {
                        this.kdr = document.getDouble("kdr");
                    }
                    if (document.getDouble("kills") != null) {
                        this.kills = document.getInteger("kills");
                    }
                    if (document.getDouble("deaths") != null) {
                        this.deaths = document.getInteger("deaths");
                    }
                    if (document.getDouble("totalWins") != null) {
                        this.wins = document.getInteger("totalWins");
                    }
                    if (document.getDouble("totalLosses") != null) {
                        this.losses = document.getInteger("totalLosses");
                    }
                });
    }
}
