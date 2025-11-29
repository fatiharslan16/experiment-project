package edu.group12.experiment;

import com.sun.net.httpserver.HttpServer;
import edu.group12.experiment.model.ExperimentState;
import edu.group12.experiment.model.Question;
import edu.group12.experiment.ui.FinishScreen;
import edu.group12.experiment.ui.MainScreen;
import edu.group12.experiment.ui.PreTestScreen1;
import edu.group12.experiment.ui.PreTestScreen2;
import edu.group12.experiment.ui.QuizScreen1;
import edu.group12.experiment.ui.QuizScreen2;
import edu.group12.experiment.ui.QuizScreen3;
import edu.group12.experiment.ui.QuizScreen4;
import edu.group12.experiment.ui.StimulusScreen1;
import edu.group12.experiment.ui.StimulusScreen2;
import edu.group12.experiment.ui.StimulusScreen3;
import edu.group12.experiment.ui.StimulusScreen4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        ExperimentState experimentState = new ExperimentState();
        experimentState.setQuestions(initQuestions());

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new MainScreen());
        server.createContext("/pretest1", new PreTestScreen1(experimentState));
        server.createContext("/pretest2", new PreTestScreen2(experimentState));

        server.createContext("/stimulus1", new StimulusScreen1(experimentState));
        server.createContext("/stimulus2", new StimulusScreen2(experimentState));
        server.createContext("/stimulus3", new StimulusScreen3(experimentState));
        server.createContext("/stimulus4", new StimulusScreen4(experimentState));

        server.createContext("/quiz1", new QuizScreen1(experimentState));
        server.createContext("/quiz2", new QuizScreen2(experimentState));
        server.createContext("/quiz3", new QuizScreen3(experimentState));
        server.createContext("/quiz4", new QuizScreen4(experimentState));

        server.createContext("/finish", new FinishScreen(experimentState));

        server.setExecutor(null);

        System.out.println("Experiment server running at http://localhost:8080");
        server.start();
    }

    private static List<Question> initQuestions() {
        List<Question> questions = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            List<String> options = new ArrayList<>();
            options.add("Option A");
            options.add("Option B");
            options.add("Option C");
            options.add("Option D");

            Question question = new Question(
                    i,
                    "Placeholder question " + i + " (replace with real content).",
                    options,
                    0 // right now A is correct
            );

            questions.add(question);
        }

        return questions;
    }
}
