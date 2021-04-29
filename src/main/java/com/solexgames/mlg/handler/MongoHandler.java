package com.solexgames.mlg.handler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

/**
 * @author GrowlyX
 * @since 3/25/2021
 */

@Getter
@Setter
public class MongoHandler {

    private final MongoClient client;
    private final MongoDatabase database;

    private final MongoCollection<Document> playerCollection;

    public MongoHandler() {
        this.client = new MongoClient(new MongoClientURI(CorePlugin.getInstance().getConfig().getString("mongodb.url")));
        this.database = client.getDatabase("SGSoftware");

        this.playerCollection = this.database.getCollection("MLGRush");
    }
}
