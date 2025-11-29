package edu.group12.experiment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperimentState {

    private Participant currentParticipant;
    private List<Question> questions = new ArrayList<>();
    private Map<Integer, Integer> answersByQuestionId = new HashMap<>();

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    public void setCurrentParticipant(Participant currentParticipant) {
        this.currentParticipant = currentParticipant;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        if (questions == null) {
            this.questions = new ArrayList<>();
        } else {
            this.questions = questions;
        }
    }

    public Map<Integer, Integer> getAnswersByQuestionId() {
        return answersByQuestionId;
    }

    public void setAnswerForQuestion(int questionId, int chosenOptionIndex) {
        answersByQuestionId.put(questionId, chosenOptionIndex);
    }

    public void clearAnswers() {
        answersByQuestionId.clear();
    }

    public int calculateScore() {
        if (questions == null || questions.isEmpty()) {
            return 0;
        }

        int score = 0;

        for (Question question : questions) {
            Integer chosen = answersByQuestionId.get(question.getId());
            if (chosen != null && chosen == question.getCorrectOptionIndex()) {
                score++;
            }
        }

        return score;
    }
}



