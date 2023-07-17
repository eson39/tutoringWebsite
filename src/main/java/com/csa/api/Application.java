package com.csa.api;

import com.csa.api.domain.quiz.QuizQuestion;
import com.csa.api.repository.DatabaseService;
import com.csa.api.ultil.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.javalin.Javalin;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();

        var app = Javalin.create(/*config*/)
                .start(8080);

        app.get("/posts/question", ctx -> {
            String response = "";
            try {
                MongoCollection<Document> posts = databaseService.getDb().getCollection("posts");
                if (posts != null) {
                    Document doc = posts.find().first();
                    response = doc.toJson();
                }
                else {
                    String errorMsg = "data not found";
                    response = "{ \"error\" : \"" + errorMsg  + "\"}";
                }
            }
            catch (Exception e) {
                e.printStackTrace();

                String errorMsg = "Exception occurred";
                response = "{ \"error\" : \"" + errorMsg  + "\"}";
            }

            ctx.json(response);
        });

        app.post("/posts/question", ctx -> {

            try {
                String bodyJson = ctx.body();
                ObjectMapper objectMapper = new ObjectMapper();
                QuizQuestion quizQuestion = objectMapper.readValue(bodyJson, QuizQuestion.class);
                quizQuestion.setId(UUID.randomUUID());
                System.out.println("quizQuestion = "+ quizQuestion.toString());
                String finalJson = JsonUtil.convertToJson(quizQuestion);
                final Document document = Document.parse(bodyJson);
                MongoCollection<Document> posts = databaseService.getDb().getCollection("posts");
                if (posts == null) {
                    databaseService.getDb().createCollection("posts");
                }
                else {
                    databaseService.getDb().getCollection("posts").insertOne(document);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });

    }
}

