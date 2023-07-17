package com.csa.api.domain.quiz;

import java.util.List;
import java.util.UUID;

public class QuizQuestion {
    private UUID id;
    private int page;
    private int maxPage;
    private String title;
    private List<Question> question;
    private List<AnswerChoices> answerChoices;
    private List<Feedback> feedback;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    public List<AnswerChoices> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoices> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }
}
