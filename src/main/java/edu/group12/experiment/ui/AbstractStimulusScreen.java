public abstract class AbstractStimulusScreen implements HttpHandler {

    protected AbstractStimulusScreen(
            ExperimentState experimentState,
            int pageNumber,
            int totalPages,
            String nextPath,
            boolean lastPage
    ) {
        // store parameters in fields
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // read participant from ExperimentState
        // If GET -> show appropriate stimulus page
        // If POST -> either go to next stimulus or start quiz
    }

    protected String getStimulusHtmlForPage(String condition, int pageNumber) {
        // return different HTML based on VIDEO/TEXT and pageNumber
        return "<p>TODO stimulus content</p>";
    }
}
