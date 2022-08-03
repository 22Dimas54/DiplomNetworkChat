package Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final String PATH = "src\\main\\resources\\settings.json";
    private static final String PATH_LOG = "src\\main\\resources\\Server.log";
    private static final int PORT = Integer.parseInt(getResource("port"));
    private static final Logger LOGGER_SERVER = Logger.getLogger("loggerServer");
    private final BlockingQueue<ClientHandler> clients = new LinkedBlockingQueue<>();
    private FileHandler fileHandlerServer;

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Socket socket = null;
        try {
            fileHandlerServer = new FileHandler(PATH_LOG, true);
            ServerSocket serverSocket = new ServerSocket(PORT);
            LOGGER_SERVER.addHandler(fileHandlerServer);
            LOGGER_SERVER.log(Level.INFO, "Server started");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    clients.add(clientHandler);
                    executorService.submit(clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                fileHandlerServer.close();
                executorService.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMsg(msg);
        }
    }
}

