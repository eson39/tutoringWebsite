package com.csa.api.repository;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DatabaseService {

    private MongoClient mongoClient;

    private MongoDatabase db;

    public DatabaseService() {
        initializeDB();
    }

    public MongoDatabase getDb() {
        return db;
    }

    private void initializeDB() {
        String user = "csa-api-user";
        String password = "test123";
        String databaseName = "csa-api-db";
        String connectionStringUri = "mongodb+srv://" + user + ":" + password + "@cluster0.fpb9ni4.mongodb.net/?retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(6, MILLISECONDS);
                    builder.readTimeout(6, MILLISECONDS);
                })
                .applyToClusterSettings( builder -> builder.serverSelectionTimeout(6, MILLISECONDS))
                .applyConnectionString(new ConnectionString(connectionStringUri))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try {
            mongoClient = MongoClients.create(settings);
            // Send a ping to confirm a successful connection
            db = mongoClient.getDatabase(databaseName);
            db.runCommand(new Document("ping", 1));

            String status = "Pinged your deployment. You successfully connected to MongoDB!";
            System.out.println("status = " + status);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
