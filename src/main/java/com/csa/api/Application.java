package com.csa.api;

import com.csa.api.domain.quiz.QuizQuestion;
import com.csa.api.domain.quiz.Unit;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;



public class Application {
    public static String queryItems(DatabaseService databaseService, org.bson.conversions.Bson filter) {
        String errorMsg = "No data found";
        String response = "{ \"error\" : \"" + errorMsg + "\"}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
            if (collection != null) {
                Bson projectionFields = Projections.fields(Projections.excludeId());
                List<QuizQuestion> documentsList = new ArrayList<>();
                MongoCursor<Document> iterator = null;
                if (filter != null) {
                    iterator = collection
                            .find(filter)
                            .projection(projectionFields)
                            .iterator();
                } else {
                    iterator = collection
                            .find()
                            .projection(projectionFields)
                            .iterator();
                }

                if (iterator != null) {
                    while(iterator.hasNext()) {
                        Document currentDocument = iterator.next();
                        String currentJson = currentDocument.toJson();

                        //System.out.println("Current JSON: " + currentJson); // Debugging

                        QuizQuestion quizQuestion = objectMapper.readValue(currentJson, QuizQuestion.class);
                        documentsList.add(quizQuestion);
                    }
                    response = objectMapper.writeValueAsString(documentsList);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            errorMsg = "Exception occurred: " + e.getMessage();
            response = "{ \"error\" : \"" + errorMsg + "\"}";
        }
        return response;
    }

    public static List<Unit> convertToUnitSubgroupJson(List<Document> allDocuments) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<QuizQuestion> documentsList = new ArrayList<>();
        for(Document currentDocument: allDocuments) {
            String currentJson = currentDocument.toJson();
            try {
                QuizQuestion quizQuestion = objectMapper.readValue(currentJson, QuizQuestion.class);
                documentsList.add(quizQuestion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Unit> unitList = new ArrayList<>();
        for (QuizQuestion currentQuestion: documentsList) {
            Unit unit = new Unit();
            unit.setUnit(currentQuestion.getUnit());
            unit.setSubgroup(currentQuestion.getSubgroup());
            unitList.add(unit);
        }
        return unitList;
    }


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
            String response = queryItems(databaseService, null);
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

        app.get("/question/list/unique", ctx -> {
            List<Document> allDocuments = new ArrayList<>();
            MongoCollection<Document> collection = databaseService.getDb().getCollection("posts");
            Bson projectionFields = Projections.fields(Projections.excludeId());

            if (collection != null) {
                MongoCursor<Document> iterator = collection
                        .find()
                        .projection(projectionFields)
                        .iterator();

                Set<String> uniqueCombinations = new HashSet<>();

                while (iterator.hasNext()) {
                    Document currentDocument = iterator.next();
                    String unit = currentDocument.getString("unit");
                    String subgroup = currentDocument.getString("subgroup");
                    String combination = unit + " " + subgroup;

                    if (!uniqueCombinations.contains(combination)) {
                        uniqueCombinations.add(combination);
                        allDocuments.add(currentDocument);
                    }
                }
                List<Unit> unitList = convertToUnitSubgroupJson(allDocuments);
                ctx.json(unitList);
            }
        });

        app.get("/question/unit/{unit}/subgroup/{subgroup}", ctx -> {
            System.out.println("testing");
            String unit = ctx.pathParam("unit");
            String subgroup = ctx.pathParam("subgroup");
            String response = queryItems(databaseService, and(eq("unit", unit), eq("subgroup", subgroup)));
            ctx.json(response);
        });

        app.get("/question/unit/{unit}", ctx -> {
            String unit = ctx.pathParam("unit");
            String response = queryItems(databaseService, eq("unit", unit));
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
