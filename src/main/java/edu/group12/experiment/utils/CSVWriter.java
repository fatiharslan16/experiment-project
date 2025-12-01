package edu.group12.experiment.utils;

import edu.group12.experiment.model.Participant;
import edu.group12.experiment.model.Question;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class CSVWriter {

    private static final String RESULTS_DIRECTORY = "data";
    private static final String RESULTS_FILE_NAME = "results.csv";

    public static void appendResults(
            Participant participant,
            List<Question> questions,
            Map<Integer, Integer> answersByQuestionId
    ) throws IOException {

        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.err.println("Warning: Could not create results directory: " + RESULTS_DIRECTORY);
            }
        }

        File file = new File(directory, RESULTS_FILE_NAME);
        boolean writeHeader = !file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            if (writeHeader) {
                writer.write("timestamp,participantId,condition,learningPreference,"
                        + "questionId,chosenOptionIndex,correctOptionIndex,isCorrect,"
                        + "stimuliDurationSeconds,quizDurationSeconds,totalScore");
                writer.newLine();
            }

            String timestamp = Instant.now().toString();

            long stimuliDurationSeconds = participant.getStimuliDurationSeconds();
            long quizDurationSeconds = participant.getQuizDurationSeconds();
            int totalScore = participant.getTotalScore();

            for (Question question : questions) {
                Integer chosenOptionIndex = answersByQuestionId.get(question.getId());
                if (chosenOptionIndex == null) {
                    chosenOptionIndex = -1;
                }

                int correctOptionIndex = question.getCorrectOptionIndex();
                boolean isCorrect = chosenOptionIndex == correctOptionIndex;

                String line = String.join(",",
                        escapeCsv(timestamp),
                        escapeCsv(participant.getParticipantId()),
                        escapeCsv(participant.getCondition()),
                        escapeCsv(participant.getLearningPreference()),
                        String.valueOf(question.getId()),
                        String.valueOf(chosenOptionIndex),
                        String.valueOf(correctOptionIndex),
                        String.valueOf(isCorrect),
                        String.valueOf(stimuliDurationSeconds),
                        String.valueOf(quizDurationSeconds),
                        String.valueOf(totalScore)
                );

                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean mustQuote = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");

        if (mustQuote) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}

