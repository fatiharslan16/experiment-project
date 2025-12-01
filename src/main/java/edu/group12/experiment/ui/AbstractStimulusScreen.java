package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Participant;
import edu.group12.experiment.utils.TimerUtil;

import java.io.IOException;

public abstract class AbstractStimulusScreen implements HttpHandler {

    private final ExperimentState experimentState;
    private final int pageNumber;
    private final int totalPages;
    private final String nextPath;
    private final boolean lastPage;

    protected AbstractStimulusScreen(
            ExperimentState experimentState,
            int pageNumber,
            int totalPages,
            String nextPath,
            boolean lastPage
    ) {
        this.experimentState = experimentState;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
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

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String condition = participant.getCondition();
            if (condition == null) {
                condition = "TEXT";
            }

            String contentHtml = getStimulusHtmlForPage(condition, pageNumber);

            String body = ""
                    + "<!doctype html>"
                    + "<html><head><title>Learning Content</title></head><body>"
                    + "<h1>Learning Content (" + condition + ")</h1>"
                    + "<h2>Page " + pageNumber + " of " + totalPages + "</h2>"
                    + contentHtml
                    + "<form method=\"post\">"
                    + "<button type=\"submit\">"
                    + (lastPage ? "Continue to quiz" : "Next page")
                    + "</button>"
                    + "</form>"
                    + "</body></html>";
            HttpUtil.sendHtml(exchange, body);
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            if (lastPage) {
                participant.setStimuliEndMillis(TimerUtil.nowMillis());
                participant.setQuizStartMillis(TimerUtil.nowMillis());
                HttpUtil.redirect(exchange, "/quiz1");
            } else {
                HttpUtil.redirect(exchange, nextPath);
            }
        } else {
            HttpUtil.redirect(exchange, "/stimulus1");
        }
    }

    /**
     * chage this to real content per page and condition.
     */
    protected String getStimulusHtmlForPage(String condition, int pageNumber) {
        // Replace with real text or video embeds for each page.
        if ("VIDEO".equalsIgnoreCase(condition)) {
            return "<p>Placeholder for VIDEO stimulus page " + pageNumber + ".</p>";
        } else {
            return "<p>Placeholder for TEXT stimulus page " + pageNumber + ".</p>";
        }
    }
}
