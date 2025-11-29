public abstract class AbstractQuizScreen implements HttpHandler {

    protected AbstractQuizScreen(
            ExperimentState experimentState,
            int fromQuestionIdInclusive,
            int toQuestionIdInclusive,
            String nextPath,
            boolean lastPage
    ) {
        // store in fields
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // GET -> render HTML for questions in [from..to]
        // POST -> read answers, store in ExperimentState,
        //         if lastPage: finish quiz, call CSVWriter.appendResults, redirect /finish
        //         else redirect to nextPath
    }
}
