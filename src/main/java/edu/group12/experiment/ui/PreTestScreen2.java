package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Participant;
import edu.group12.experiment.utils.Randomizer;
import edu.group12.experiment.utils.TimerUtil;

import java.io.IOException;
import java.util.Map;

public class PreTestScreen2 implements HttpHandler {

    private final ExperimentState experimentState;

    public PreTestScreen2(ExperimentState experimentState) {
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
            String body = ""
                    + "<!doctype html>"
                    + "<html><head><title>Demographics</title></head><body>"
                    + "<h1>Demographics & Learning Preference</h1>"
                    + "<form method=\"post\">"
                    + "Age range: "
                    + "<select name=\"ageRange\" required>"
                    + "<option value=\"18-24\">18-24</option>"
                    + "<option value=\"25-34\">25-34</option>"
                    + "<option value=\"35-44\">35-44</option>"
                    + "<option value=\"45+\">45+</option>"
                    + "</select><br><br>"
                    + "Gender: "
                    + "<select name=\"gender\" required>"
                    + "<option value=\"female\">Female</option>"
                    + "<option value=\"male\">Male</option>"
                    + "<option value=\"nonbinary\">Non-binary</option>"
                    + "<option value=\"prefer_not_to_say\">Prefer not to say</option>"
                    + "</select><br><br>"
                    + "Learning preference: "
                    + "<select name=\"learningPreference\" required>"
                    + "<option value=\"VIDEO\">Prefer videos</option>"
                    + "<option value=\"TEXT\">Prefer text</option>"
                    + "<option value=\"NO_PREFERENCE\">No preference</option>"
                    + "</select><br><br>"
                    + "<button type=\"submit\">Continue to learning content</button>"
                    + "</form>"
                    + "</body></html>";
            HttpUtil.sendHtml(exchange, body);
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = HttpUtil.readFormParams(exchange);

            participant.setAgeRange(params.get("ageRange"));
            participant.setGender(params.get("gender"));
            participant.setLearningPreference(params.get("learningPreference"));

            String assignedCondition = Randomizer.assignCondition(); // VIDEO or TEXT
            participant.setCondition(assignedCondition);

            participant.setStimuliStartMillis(TimerUtil.nowMillis());

            HttpUtil.redirect(exchange, "/stimulus1");
        } else {
            HttpUtil.redirect(exchange, "/pretest2");
        }
    }
}
