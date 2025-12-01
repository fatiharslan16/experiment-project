package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Participant;
import edu.group12.experiment.model.Question;
import edu.group12.experiment.utils.CSVWriter;
import edu.group12.experiment.utils.TimerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractQuizScreen implements HttpHandler {

    private final ExperimentState experimentState;
    private final int fromQuestionIdInclusive;
    private final int toQuestionIdInclusive;
    private final String nextPath;
    private final boolean lastPage;

    protected AbstractQuizScreen(
            ExperimentState experimentState,
            int fromQuestionIdInclusive,
            int toQuestionIdInclusive,
            String nextPath,
            boolean lastPage
    ) {
        this.experimentState = experimentState;
        this.fromQuestionIdInclusive = fromQuestionIdInclusive;
        this.toQuestionIdInclusive = toQuestionIdInclusive;
        this.nextPath = nextPath;
        this.lastPage = lastPage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Participant participant = experimentState.getCurrentParticipant();
        if (participant == null) {
            HttpUtil.redirect(exchange, "/pretest1");
            return;
        }

        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            renderGet(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange, participant);
        } else {
            HttpUtil.redirect(exchange, "/quiz1");
        }
    }

    private List<Question> getQuestionsInRange() {
        List<Question> result = new ArrayList<>();
        for (Question question : experimentState.getQuestions()) {
            int id = question.getId();
            if (id >= fromQuestionIdInclusive && id <= toQuestionIdInclusive) {
                result.add(question);
            }
        }
        return result;
    }

    private void renderGet(HttpExchange exchange) throws IOException {
        List<Question> questions = getQuestionsInRange();

        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head><title>Quiz</title></head><body>")
                .append("<h1>Quiz</h1><form method=\"post\">");

        for (Question question : questions) {
            int id = question.getId();
            sb.append("<div style=\"margin-bottom: 16px;\">")
                    .append("<strong>Q").append(id).append(".</strong> ")
                    .append(question.getText()).append("<br>");

            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                sb.append("<label>")
                        .append("<input type=\"radio\" name=\"q")
                        .append(id)
                        .append("\" value=\"")
                        .append(i)
                        .append("\" required> ")
                        .append(options.get(i))
                        .append("</label><br>");
            }

            sb.append("</div>");
        }

        sb.append("<button type=\"submit\">")
                .append(lastPage ? "Submit quiz" : "Next")
                .append("</button></form></body></html>");

        HttpUtil.sendHtml(exchange, sb.toString());
    }

    private void handlePost(HttpExchange exchange, Participant participant) throws IOException {
        Map<String, String> params = HttpUtil.readFormParams(exchange);

        for (Question question : getQuestionsInRange()) {
            int id = question.getId();
            String value = params.get("q" + id);
            if (value != null && !value.isEmpty()) {
                try {
                    int chosenIndex = Integer.parseInt(value);
                    experimentState.setAnswerForQuestion(id, chosenIndex);
                } catch (NumberFormatException ignored) {
                    // ignore invalid values
                }
            }
        }

        if (lastPage) {
            participant.setQuizEndMillis(TimerUtil.nowMillis());
            int score = experimentState.calculateScore();
            participant.setTotalScore(score);

            try {
                CSVWriter.appendResults(
                        participant,
                        experimentState.getQuestions(),
                        experimentState.getAnswersByQuestionId()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpUtil.redirect(exchange, "/finish");
        } else {
            HttpUtil.redirect(exchange, nextPath);
        }
    }
}

