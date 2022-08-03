package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

public class ClientHandler implements Runnable {
    private Server server;
    private PrintWriter out;
    private Scanner in;
    private String nameParticipant;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            nameParticipant = in.nextLine();
            server.sendLogger(Level.INFO, "В чате новый участник " + nameParticipant);
            server.sendMsgClients("В чате новый участник " + nameParticipant);
            while (true) {
                if (in.hasNext()) {
                    String msg = in.nextLine();
                    if (msg.equals("exit")) {
                        break;
                    }
                    server.sendLogger(Level.INFO, "Участник '" + nameParticipant + "': '" + msg + "'");
                    server.sendMsgClients(this.nameParticipant + ": " + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
