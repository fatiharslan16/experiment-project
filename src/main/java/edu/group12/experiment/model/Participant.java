package edu.group12.experiment.model;

public class Participant {

    private String participantId;
    private String ageRange;
    private String gender;
    private String learningPreference; // VIDEO, TEXT, NO_PREFERENCE
    private String condition;          // VIDEO or TEXT 

    private String consentTimestampIso;

    private long stimuliStartMillis;
    private long stimuliEndMillis;
    private long quizStartMillis;
    private long quizEndMillis;

    private int totalScore;

    public Participant() {
    }

    public Participant(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLearningPreference() {
        return learningPreference;
    }

    public void setLearningPreference(String learningPreference) {
        this.learningPreference = learningPreference;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConsentTimestampIso() {
        return consentTimestampIso;
    }

    public void setConsentTimestampIso(String consentTimestampIso) {
        this.consentTimestampIso = consentTimestampIso;
    }

    public long getStimuliStartMillis() {
        return stimuliStartMillis;
    }

    public void setStimuliStartMillis(long stimuliStartMillis) {
        this.stimuliStartMillis = stimuliStartMillis;
    }

    public long getStimuliEndMillis() {
        return stimuliEndMillis;
    }

    public void setStimuliEndMillis(long stimuliEndMillis) {
        this.stimuliEndMillis = stimuliEndMillis;
    }

    public long getQuizStartMillis() {
        return quizStartMillis;
    }

    public void setQuizStartMillis(long quizStartMillis) {
        this.quizStartMillis = quizStartMillis;
    }

    public long getQuizEndMillis() {
        return quizEndMillis;
    }

    public void setQuizEndMillis(long quizEndMillis) {
        this.quizEndMillis = quizEndMillis;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public long getStimuliDurationSeconds() {
        if (stimuliStartMillis <= 0 || stimuliEndMillis <= 0) {
            return 0;
        }
        return (stimuliEndMillis - stimuliStartMillis) / 1000;
    }

    public long getQuizDurationSeconds() {
        if (quizStartMillis <= 0 || quizEndMillis <= 0) {
            return 0;
        }
        return (quizEndMillis - quizStartMillis) / 1000;
    }
}
