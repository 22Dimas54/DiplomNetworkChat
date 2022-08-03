package Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static final String PATH = "src\\main\\resources\\settings.json";
    private static final String PATH_LOG = "src\\main\\resources\\Client.log";
    private FileHandler fileHandlerClient;
    private static final int PORT = Integer.parseInt(getResource("port"));
    private static final String HOST = getResource("host");
    private static final Logger LOGGER_CLIENT = Logger.getLogger("loggerClient");

    public void start() {
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            fileHandlerClient = new FileHandler(PATH_LOG, true);
            LOGGER_CLIENT.addHandler(fileHandlerClient);
            System.out.println("Приветсвую в чате, рады, что заглянули!Введите Ваше имя:");
            String msg;
            msg = scanner.nextLine();
            out.println(msg);
            LOGGER_CLIENT.log(Level.INFO, in.readLine());
            while (true) {
                msg = scanner.nextLine();
                if (msg.equals("exit")) {
                    out.println(" покинул чат");
                    LOGGER_CLIENT.log(Level.INFO, in.readLine());
                    break;
                }
                out.println(msg);
                LOGGER_CLIENT.log(Level.INFO, in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileHandlerClient.close();
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
}

