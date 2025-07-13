package test.http;

import main.http.server.HttpTaskServer;
import main.manager.Managers;
import main.manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class HttpTaskServerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer server;

    @BeforeEach
    void start() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    void stop() {
        server.stop();
    }
}