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

public class MessageHandler {

    private boolean online;
    private Socket clientSocket;
    private static final String PATH_LOG = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"Client.log";
    private static final Logger LOGGER_CLIENT = Logger.getLogger("loggerClient");
    private FileHandler fileHandlerClient;
    private static final String PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"settings.json";
    private static final int PORT = Integer.parseInt(getResource("port"));
    private static final String HOST = getResource("host");
    private PrintWriter out;
    private BufferedReader in;

    public MessageHandler() {
        try {
            clientSocket = new Socket(HOST, PORT);
            fileHandlerClient = new FileHandler(PATH_LOG, true);
            LOGGER_CLIENT.addHandler(fileHandlerClient);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessages() {
        try {
            String input;
            online = true;
            while (online) {
                input = in.readLine();
                System.out.println(input);
                LOGGER_CLIENT.log(Level.INFO, input);
            }
            closeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessagesES() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Приветсвую в чате, рады, что заглянули!Введите Ваше имя:");
            String msg;
            while (true) {
                if (scanner.hasNextLine()) {
                    msg = scanner.nextLine();
                    if (msg.equals("exit")) {
                        out.println(" покинул чат");
                        online = false;
                        break;
                    }
                    out.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeAll() {
        try {
            clientSocket.close();
            in.close();
            out.close();
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

}

