package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtil {
    private HttpUtil() {}

    public static void sendHtml(HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (var os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    public static Map<String, String> readFormParams(HttpExchange exchange) throws IOException {
        Map<String, String> map = new HashMap<>();

        String query = exchange.getRequestURI().getRawQuery();
        if (query != null && !query.isEmpty()) {
            parseUrlEncodedIntoMap(query, map);
        }

        byte[] body = readAll(exchange.getRequestBody());
        if (body.length > 0) {
            parseUrlEncodedIntoMap(new String(body, StandardCharsets.UTF_8), map);
        }

        return map;
    }

    private static void parseUrlEncodedIntoMap(String s, Map<String, String> out) {
        for (String pair : s.split("&")) {
            int i = pair.indexOf('=');
            if (i >= 0) {
                String key = URLDecoder.decode(pair.substring(0, i), StandardCharsets.UTF_8);
                String val = URLDecoder.decode(pair.substring(i + 1), StandardCharsets.UTF_8);
                out.put(key, val);
            } else if (!pair.isEmpty()) {
                String key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
                out.put(key, "");
            }
        }
    }

    private static byte[] readAll(InputStream in) throws IOException {
        try (in; var buf = new ByteArrayOutputStream()) {
            byte[] tmp = new byte[4096];
            int n;
            while ((n = in.read(tmp)) > 0) {
                buf.write(tmp, 0, n);
            }
            return buf.toByteArray();
        }
    }
}