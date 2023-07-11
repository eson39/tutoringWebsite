package com.csa.api;

import com.csa.api.domain.Student;
import com.csa.api.repository.DatabaseService;
import com.csa.api.ultil.JsonUtil;
import com.mongodb.client.MongoCollection;
import io.javalin.Javalin;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {

    private static Map<String, Student> mockCreateStudents() {
        Map<String, Student> studentMap = new HashMap<>();

        String id = UUID.randomUUID().toString();
        Student student1 = new Student(id, "Eric", "Son", 14, 8);
        studentMap.put(id, student1);

        id = UUID.randomUUID().toString();
        Student student2 = new Student(id, "John", "Doe", 16, 10);
        studentMap.put(id, student2);

        return studentMap;
    }


    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();

        var app = Javalin.create(/*config*/)
                .start(8080)
                .get("/", ctx -> ctx.result(JsonUtil.convertToJson(mockCreateStudents())));

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

