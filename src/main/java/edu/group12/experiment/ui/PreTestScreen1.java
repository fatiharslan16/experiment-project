package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Participant;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class PreTestScreen1 implements HttpHandler {

    private final ExperimentState experimentState;

    public PreTestScreen1(ExperimentState experimentState) {
        this.experimentState = experimentState;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = ""
                    + "<!doctype html>"
                    + "<html><head><title>Consent</title></head><body>"
                    + "<h1>Consent Form</h1>"
                    + "<p>Put your full consent text here.</p>"
                    + "<form method=\"post\">"
                    + "<label><input type=\"checkbox\" name=\"consent\" value=\"yes\" required> "
                    + "I consent to participate in this study.</label><br><br>"
                    + "<label>Participant ID: "
                    + "<input type=\"text\" name=\"participantId\" required></label><br><br>"
                    + "<button type=\"submit\">Continue</button>"
                    + "</form>"
                    + "</body></html>";
            HttpUtil.sendHtml(exchange, body);
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = HttpUtil.readFormParams(exchange);
            String participantId = params.get("participantId");

            Participant participant = new Participant(participantId);
            participant.setConsentTimestampIso(Instant.now().toString());
            experimentState.setCurrentParticipant(participant);

            HttpUtil.redirect(exchange, "/pretest2");
        } else {
            HttpUtil.redirect(exchange, "/pretest1");
        }
    }
}

