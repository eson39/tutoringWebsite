package com.csa.api.domain.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Question {
    private UUID id;
    private String questionHeader;
    private String questionMain;
    private QuestionCode questionCode;

    public Question() {
    }

    public Question(UUID id, String questionHeader, String questionMain, QuestionCode questionCode) {
        this.id = id;
        this.questionHeader = questionHeader;
        this.questionMain = questionMain;
        this.questionCode = questionCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("question_header")
    public String getQuestionHeader() {
        return questionHeader;
    }

    public void setQuestionHeader(String questionHeader) {
        this.questionHeader = questionHeader;
    }

    @JsonProperty("question_main")
    public String getQuestionMain() {
        return questionMain;
    }

    public void setQuestionMain(String questionMain) {
        this.questionMain = questionMain;
    }

    @JsonProperty("question_code")
    public QuestionCode getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(QuestionCode questionCode) {
        this.questionCode = questionCode;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionHeader='" + questionHeader + '\'' +
                ", questionMain='" + questionMain + '\'' +
                ", questionCode=" + questionCode +
                '}';
    }
}