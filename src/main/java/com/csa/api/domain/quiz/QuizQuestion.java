package com.csa.api.domain.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class QuizQuestion {
    private UUID id;
    private int page;
    private int maxPage;
    private String title;
    private List<Question> questionList;
    private List<AnswerChoice> answerChoiceList;
    private List<Feedback> feedbackList;

    public QuizQuestion() {
    }

    public QuizQuestion(UUID id, int page, int maxPage, String title, List<Question> questionList, List<AnswerChoice> answerChoiceList, List<Feedback> feedbackList) {
        this.id = id;
        this.page = page;
        this.maxPage = maxPage;
        this.title = title;
        this.questionList = questionList;
        this.answerChoiceList = answerChoiceList;
        this.feedbackList = feedbackList;
    }
    @JsonProperty("id")
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

    @JsonProperty("max_page")
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

    @JsonProperty("question")
    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    @JsonProperty("answer_choices")
    public List<AnswerChoice> getAnswerChoiceList() {
        return answerChoiceList;
    }

    public void setAnswerChoiceList(List<AnswerChoice> answerChoiceList) {
        this.answerChoiceList = answerChoiceList;
    }

    @JsonProperty("feedback")
    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + id +
                ", page=" + page +
                ", maxPage=" + maxPage +
                ", title='" + title + '\'' +
                ", questionList=" + questionList +
                ", answerChoiceList=" + answerChoiceList +
                ", feedbackList=" + feedbackList +
                '}';
    }
}