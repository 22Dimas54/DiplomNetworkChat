package Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void start() {
        try (Socket clientSocket = new Socket(getResource("host"), Integer.parseInt(getResource("port")));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            String msg;
            while (true) {
                msg = scanner.nextLine();
                if (msg.equals("exit")) {
                    out.println(this.getName() + " покинул чат");
                    break;
                }
                out.println(msg);
                System.out.println(this.getName() + " " + in.readLine());
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

