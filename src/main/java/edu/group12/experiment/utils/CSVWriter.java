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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

      
        BufferedWriter desktopWriter = null;
        boolean desktopWriteHeader = false;
        try {
            Path baseDir = Paths.get(System.getProperty("user.home"), "Desktop", "ExperimentResults");
            Files.createDirectories(baseDir);

            String id = sanitizeFileComponent(participant != null ? participant.getParticipantId() : null);
            Path perParticipant = baseDir.resolve(id + ".csv");
            desktopWriteHeader = !Files.exists(perParticipant);
            desktopWriter = new BufferedWriter(new FileWriter(perParticipant.toFile(), true));
        } catch (IOException e) {
            
            System.err.println("Note: Could not open Desktop participant file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
           
            if (writeHeader) {
                String header = "timestamp,participantId,condition,learningPreference,"
                        + "questionId,chosenOptionIndex,correctOptionIndex,isCorrect,"
                        + "stimuliDurationSeconds,quizDurationSeconds,totalScore";
                writer.write(header);
                writer.newLine();
            }
            if (desktopWriter != null && desktopWriteHeader) {
                String header = "timestamp,participantId,condition,learningPreference,"
                        + "questionId,chosenOptionIndex,correctOptionIndex,isCorrect,"
                        + "stimuliDurationSeconds,quizDurationSeconds,totalScore";
                desktopWriter.write(header);
                desktopWriter.newLine();
            }

            String timestamp = Instant.now().toString();
            long stimuliDurationSeconds = participant != null ? participant.getStimuliDurationSeconds() : 0;
            long quizDurationSeconds = participant != null ? participant.getQuizDurationSeconds() : 0;
            int totalScore = participant != null ? participant.getTotalScore() : 0;

            String participantId = participant != null ? participant.getParticipantId() : "";
            String condition = participant != null ? participant.getCondition() : "";
            String learningPreference = participant != null ? participant.getLearningPreference() : "";

            for (Question question : questions) {
                Integer chosenOptionIndex = answersByQuestionId.get(question.getId());
                if (chosenOptionIndex == null) {
                    chosenOptionIndex = -1;
                }

                int correctOptionIndex = question.getCorrectOptionIndex();
                boolean isCorrect = chosenOptionIndex == correctOptionIndex;

                String line = String.join(",",
                        escapeCsv(timestamp),
                        escapeCsv(participantId),
                        escapeCsv(condition),
                        escapeCsv(learningPreference),
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

                if (desktopWriter != null) {
                    desktopWriter.write(line);
                    desktopWriter.newLine();
                }
            }
        } finally {
            if (desktopWriter != null) {
                try { desktopWriter.close(); } catch (IOException ignored) {}
            }
        }
    }

    private static String sanitizeFileComponent(String raw) {
        String s = (raw == null || raw.isBlank())
                ? ("participant-" + System.currentTimeMillis())
                : raw.trim();
        
        return s.replaceAll("[\\\\/:*?\"<>|]", "_");
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