public final class HttpUtil {

    public static void sendHtml(HttpExchange exchange, String body) throws IOException {
        // get Content-Type, write body, close response
    }

    public static void redirect(HttpExchange exchange, String location) throws IOException {
        // set Location header and 302 status
    }

    public static Map<String, String> readFormParams(HttpExchange exchange) throws IOException {
        // TODO: read request body, parse x-www-form-urlencoded 
        return new HashMap<>();
    }
}
