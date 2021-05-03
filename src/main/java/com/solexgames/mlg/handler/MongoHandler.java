package com.solexgames.mlg.handler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@Setter
public class MongoHandler {

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> playerCollection;

    public MongoHandler() {
        try {
            this.client = new MongoClient(new MongoClientURI(CorePlugin.getInstance().getConfig().getString("mongodb.url")));
            this.database = client.getDatabase("SGSoftware");

            this.playerCollection = this.database.getCollection("MLGRush");
        } catch (Exception exception) {
            System.out.println("[MLGRush] Couldn't connect to the mongo database.");
            Bukkit.getPluginManager().disablePlugin(CorePlugin.getInstance());
        }
    }
}
