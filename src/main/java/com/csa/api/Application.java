package com.csa.api;

import com.csa.api.domain.Student;
import com.csa.api.ultil.JsonUtil;
import io.javalin.Javalin;

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
            var app = Javalin.create(/*config*/)
                    .start(8080)
                    .get("/", ctx -> ctx.result(JsonUtil.convertToJson(mockCreateStudents())));

    }
}

