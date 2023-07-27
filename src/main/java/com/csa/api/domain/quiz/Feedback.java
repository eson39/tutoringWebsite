package com.csa.api.domain.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Feedback {
    private String correctAnswer;
    private String explanation;

    public Feedback() {
    }

    public Feedback( String correctAnswer, String explanation) {
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
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
                ", correctAnswer='" + correctAnswer + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
