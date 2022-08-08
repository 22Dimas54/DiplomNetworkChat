package Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        MessageHandler messageHandler = new MessageHandler();
        executorService.submit(new Thread(null, () -> messageHandler.readMessages()));
        executorService.submit(new Thread(null, () -> messageHandler.writeMessagesES()));
        executorService.shutdown();
    }
}

