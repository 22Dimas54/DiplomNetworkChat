package Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void start() {
        try {
            System.out.println("Server started");
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(getResource("port")));
            while (true) {
                try (Socket socket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getResource(String key) {
        try (FileReader reader = new FileReader("src\\main\\resources\\settings.json")) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            return (String) jsonObject.get(key);
        } catch (IOException | ParseException e) {
            return "";
        }
    }
}

