package com.csa.api;

import com.csa.api.domain.quiz.QuizQuestion;
import com.csa.api.repository.DatabaseService;
import com.csa.api.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import io.javalin.Javalin;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;



public class Application {

    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();

        var app = Javalin.create(config -> {
                    config.plugins.enableCors(cors -> {
                        cors.add(it -> {
//                          it.allowHost("http://127.0.0.1:5500");
                            it.anyHost();
                        });
                    });
                })
                .start(8080);


        app.get("/question", ctx -> {
            String response = "";
            ObjectMapper objectMapper = new ObjectMapper();
            String errorMsg = "";
            try {
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
                if (collection != null) {
//                    Document doc = posts.find().first();
//                    response = doc.toJson();

                    Bson projectionFields = Projections.fields(Projections.excludeId());

                    List<QuizQuestion> documentsList = new ArrayList<>();
                    MongoCursor<Document> iterator = collection
                            .find()
                            .projection(projectionFields)
                            .iterator();

                    while (iterator.hasNext()) {
                        Document currentDocument = iterator.next();
                        String currentJson = currentDocument.toJson();

                        QuizQuestion quizQuestion = objectMapper.readValue(currentJson, QuizQuestion.class);
                        documentsList.add(quizQuestion);
                    }

                    response = objectMapper.writeValueAsString(documentsList);
                }
                else {
                    errorMsg = "data not found";
                    response = "{ \"error\" : \"" + errorMsg  + "\"}";
                }
            }
            catch (Exception e) {
                e.printStackTrace();

                errorMsg = "Exception occurred";
                response = "{ \"error\" : \"" + errorMsg  + "\"}";
            }

            ctx.json(response);
        });


        app.get("/question/{id}", ctx -> {
            String id = ctx.pathParam("id");

            String value = ctx.queryParam("age");
            //System.out.println("-------- value = " + value);

            String response = "";
            try {
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");

                Bson projectionFields = Projections.fields(Projections.excludeId());

                Document document = collection.find(eq("id", id))
                        .projection(projectionFields)
                        .first();

                if (document != null) {
                    String json = document.toJson();

                    // any rules that needs to run we want to conver to java objects from json String
                    ObjectMapper objectMapper = new ObjectMapper();
                    QuizQuestion quizQuestion = objectMapper.readValue(json, QuizQuestion.class);

                    response = objectMapper.writeValueAsString(quizQuestion);
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

        app.get("/question/unit/{unit}/subgroup/{subgroup}", ctx -> {
            String unit = ctx.pathParam("unit");
            String subgroup = ctx.pathParam("subgroup");

            String response = "";
            ObjectMapper objectMapper = new ObjectMapper();
            String errorMsg = "";

            try {
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
                if (collection != null) {
                    Bson projectionFields = Projections.fields(Projections.excludeId());
                    List<QuizQuestion> documentsList = new ArrayList<>();
                    MongoCursor<Document> iterator = collection.find(and(eq("unit", unit), eq("subgroup", subgroup)))
                            .projection(projectionFields)
                            .iterator();
                    System.out.println(iterator.hasNext());
                    while(iterator.hasNext()) {
                        Document currentDocument = iterator.next();
                        String currentJson = currentDocument.toJson();

                        System.out.println("Current JSON: " + currentJson); // Debugging

                        QuizQuestion quizQuestion = objectMapper.readValue(currentJson, QuizQuestion.class);
                        documentsList.add(quizQuestion);
                    }

                    response = objectMapper.writeValueAsString(documentsList);
                } else {
                    errorMsg = "No data found";
                    response = "{ \"error\" : \"" + errorMsg + "\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = "Exception occurred: " + e.getMessage();
                response = "{ \"error\" : \"" + errorMsg + "\"}";
            }

            System.out.println("Response: " + response); // Debugging
            ctx.json(response);
        });

        app.get("/question/unit/{unit}", ctx -> {
            String unit = ctx.pathParam("unit");
            System.out.println("Unit extracted from URL: " + unit); // Debugging

            String response = "";
            ObjectMapper objectMapper = new ObjectMapper();
            String errorMsg = "";

            try {
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
                if (collection != null) {
                    Bson projectionFields = Projections.fields(Projections.excludeId());
                    List<QuizQuestion> documentsList = new ArrayList<>();
                    MongoCursor<Document> iterator = collection.find(eq("unit", unit))
                            .projection(projectionFields)
                            .iterator();
                    System.out.println(iterator.hasNext());
                    while(iterator.hasNext()) {
                        Document currentDocument = iterator.next();
                        String currentJson = currentDocument.toJson();

                        System.out.println("Current JSON: " + currentJson); // Debugging

                        QuizQuestion quizQuestion = objectMapper.readValue(currentJson, QuizQuestion.class);
                        documentsList.add(quizQuestion);
                    }

                    response = objectMapper.writeValueAsString(documentsList);
                } else {
                    errorMsg = "No data found";
                    response = "{ \"error\" : \"" + errorMsg + "\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = "Exception occurred: " + e.getMessage();
                response = "{ \"error\" : \"" + errorMsg + "\"}";
            }

            System.out.println("Response: " + response); // Debugging
            ctx.json(response);
        });




        app.post("/question", ctx -> {
            String msg = "Successful";
            String response = "{ \"status\" : \"" + msg + "\"}";

            try {
                String bodyJson = ctx.body();
                ObjectMapper objectMapper = new ObjectMapper();
                QuizQuestion quizQuestion = objectMapper.readValue(bodyJson, QuizQuestion.class);
                quizQuestion.setId(UUID.randomUUID());

                System.out.println("quizQuestion = " + quizQuestion.toString());

                String finalJson = JsonUtil.convertToJson(quizQuestion);
                final Document document = Document.parse(finalJson);

                MongoCollection<Document> posts = databaseService.getDb().getCollection("posts");
                if (posts == null) {
                    databaseService.getDb().createCollection("posts");
                } else {
                    databaseService.getDb().getCollection("posts").insertOne(document);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String errorMsg = "Exception occurred";
                response = "{ \"error\" : \"" + errorMsg + "\"}";
                ctx.status(400);
            }

            ctx.json(response);
        });


        app.put("/question", ctx -> {

            try {
                String json = ctx.body();
                //System.out.println("json = " + json);

                ObjectMapper objectMapper = new ObjectMapper();
                QuizQuestion quizQuestion = objectMapper.readValue(json, QuizQuestion.class);

                String finalJson = JsonUtil.convertToJson(quizQuestion);

                final Document document = Document.parse(finalJson);
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
                if (collection != null) {
                    databaseService.getDb().getCollection("posts").replaceOne(
                            Filters.eq("id", quizQuestion.getId().toString()),
                            document);
                }
            }
            catch (Exception e) {
                e.printStackTrace();

                String errorMsg = "Exception occurred";
                String response = "{ \"error\" : \"" + errorMsg  + "\"}";
                ctx.json(response);
            }


            String msg = "Successful";
            String response = "{ \"status\" : \"" + msg  + "\"}";
            ctx.json(response);

        });

        app.delete("/question/{id}", ctx -> {
            String id = ctx.pathParam("id");

            String response = "";
            try {
                MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
                if (collection != null) {
                    DeleteResult result = databaseService.getDb().getCollection("posts").deleteOne(Filters.eq("id", id));
                    if (result == null || result.getDeletedCount() == 0) {
                        String errorMsg = "Exception occurred";
                        response = "{ \"error\" : \"" + errorMsg  + "\"}";
                        ctx.json(response);
                    }
                }
                else {
                    String errorMsg = "data not found";
                    response = "{ \"error\" : \"" + errorMsg  + "\"}";
                    ctx.json(response);
                }
            }
            catch (Exception e) {
                e.printStackTrace();

                String errorMsg = "Exception occurred";
                response = "{ \"error\" : \"" + errorMsg  + "\"}";
                ctx.json(response);
            }

            String msg = "Deleted";
            response = "{ \"status\" : \"" + msg  + "\"}";
            ctx.json(response);
        });

    }
}
