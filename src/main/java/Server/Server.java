package Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final String PATH = "src\\main\\resources\\settings.json";
    private static final int PORT = Integer.parseInt(getResource("port"));
    private static final Logger LOGGER_SERVER = Logger.getLogger("loggerServer");
    private static final Date DATE = new Date();
    private final BlockingQueue<ClientHandler> clients = new LinkedBlockingQueue<>();

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");
            LOGGER_SERVER.log(Level.INFO, "Server started " + DATE.toString());
            ExecutorService executorService = Executors.newCachedThreadPool();
            while (true) {
                try {
                    ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), this);
                    clients.add(clientHandler);
                    executorService.submit(clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getResource(String key) {
        try (FileReader reader = new FileReader(PATH)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            return (String) jsonObject.get(key);
        } catch (IOException | ParseException e) {
            return "";
        }
    }

    public void sendLogger(Level level, String information) {
        LOGGER_SERVER.log(level, information);
    }

    public void sendMsgClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }
}

