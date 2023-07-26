package com.csa.api.domain.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Feedback {
    private UUID id;
    private String correctAnswer;
    private String explanation;

    public Feedback() {
    }

    public Feedback(UUID id, String correctAnswer, String explanation) {
        this.id = id;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("correct_answer")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
