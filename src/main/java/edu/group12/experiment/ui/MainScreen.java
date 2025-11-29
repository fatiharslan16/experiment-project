package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MainScreen implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = ""
                    + "<!doctype html>"
                    + "<html><head><title>Experiment</title></head><body>"
                    + "<h1>Welcome to the Learning Experiment</h1>"
                    + "<p>This system will guide you through consent, pre-test questions, "
                    + "learning content and a quiz.</p>"
                    + "<a href=\"/pretest1\">Start</a>"
                    + "</body></html>";
            HttpUtil.sendHtml(exchange, body);
        } else {
            HttpUtil.redirect(exchange, "/");
        }
    }
}
