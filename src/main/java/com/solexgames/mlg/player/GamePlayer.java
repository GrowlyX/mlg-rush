package com.solexgames.mlg.player;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.factory.GsonFactory;
import com.solexgames.mlg.model.Layout;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Player profile where statistics are saved.
 * <p></p>
 *
 * @author GrowlyX
 * @since 4/29/2021
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

    private Layout layout;

    public GamePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.loadPlayerData();
    }

    public void savePlayerData(boolean remove) {
        CompletableFuture.runAsync(() -> CorePlugin.getInstance().getMongoHandler().getPlayerCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), this.getDocument(), new ReplaceOptions().upsert(true)));

        if (remove) {
            CorePlugin.getInstance().getPlayerHandler().getPlayerList().remove(this);
        }

        System.out.println("saved data");
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

        document.put("layout", GsonFactory.getPrettyGson().toJson(this.layout));

        return document;
    }

    private void loadPlayerData() {
        System.out.println("loading data");

        CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("uuid", this.uuid.toString())).first())
                .thenAccept(document -> {
                    if (document == null) {
                        System.out.println("player data null for: " + this.name);

                        this.layout = new Layout(this.uuid);
                        this.layout.setupDefaultInventory();

                        CorePlugin.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(CorePlugin.getInstance(), () -> this.savePlayerData(false), 20L);
                    } else {
                        // for debug because Completeablefuture#suppylasync alr catches excetipns and doesnt rprint them for some weird ass reason
                        try {
                            if (document.getDouble("kdr") != null) {
                                this.kdr = document.getDouble("kdr");
                            }
                            if (document.getInteger("kills") != null) {
                                this.kills = document.getInteger("kills");
                            }
                            if (document.getInteger("deaths") != null) {
                                this.deaths = document.getInteger("deaths");
                            }
                            if (document.getInteger("totalWins") != null) {
                                this.wins = document.getInteger("totalWins");
                            }
                            if (document.getInteger("totalLosses") != null) {
                                this.losses = document.getInteger("totalLosses");
                            }
                            if (document.getString("layout") != null) {
                                this.layout = GsonFactory.getPrettyGson().fromJson(document.getString("layout"), Layout.class);
                            } else {
                                this.layout = new Layout(this.uuid);
                            }

                            this.layout.setupDefaultInventory();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
    }
}
