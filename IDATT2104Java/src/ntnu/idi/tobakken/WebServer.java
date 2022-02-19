package ntnu.idi.tobakken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 80;

        ServerSocket server = new ServerSocket(PORTNR);
        Socket connection = server.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        PrintWriter pw = new PrintWriter(connection.getOutputStream(), true);

        StringBuilder head = new StringBuilder("<ul>");
        for (int i = 0; i < 16; i++) {
            head.append("<li>").append(reader.readLine()).append("</li>");
        }
        head.append("</ul>");

        System.out.println(head);

        String msg = "HTTP/1.0 200 OK \n" +
                "Content-Type: text/html; charset=utf-8 \n" +
                "\n\n" +
                "<html> <body>" +
                "<h1> Yo dawg! Du har koblet deg opp til min enkle web-tjener</h1>" +
                "<p>Header fra Klient er: </p>" +
                head +
                "</body></html>";
        pw.println(msg);
        connection.close();
        pw.close();
    }
}
