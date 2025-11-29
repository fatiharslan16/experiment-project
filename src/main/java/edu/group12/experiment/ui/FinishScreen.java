package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Participant;

import java.io.IOException;

public class FinishScreen implements HttpHandler {

    private final ExperimentState experimentState;

    public FinishScreen(ExperimentState experimentState) {
        this.experimentState = experimentState;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Participant participant = experimentState.getCurrentParticipant();
        if (participant == null) {
            HttpUtil.redirect(exchange, "/pretest1");
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            int totalQuestions = experimentState.getQuestions().size();
            int score = participant.getTotalScore();

            String body = ""
                    + "<!doctype html>"
                    + "<html><head><title>Thank you</title></head><body>"
                    + "<h1>Thank you for participating!</h1>"
                    + "<p>Your score: " + score + " / " + totalQuestions + "</p>"
                    + "<p>You can close this window now.</p>"
                    + "</body></html>";
            HttpUtil.sendHtml(exchange, body);
        } else {
            HttpUtil.redirect(exchange, "/finish");
        }
    }
}
