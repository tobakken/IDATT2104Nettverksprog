package ntnu.idi.stud.tobakken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocket {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        ServerSocket server = new ServerSocket(4545);
        try {
            System.out.println("Server started on 127.0.0.1:4545 \r\n Waiting for connection...");
            Socket client = server.accept();
            System.out.println("A client connected");

            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            Scanner scanner = new Scanner(in, "UTF-8");

            String data = scanner.useDelimiter("\\r\\n\\r\\n").next();
            System.out.println(data);
            String test = data.substring(data.lastIndexOf("Sec-WebSocket-Key: "), data.length());
            // System.out.println(test);
            Matcher get = Pattern.compile("^GET").matcher(data);

            if (get.find()) {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                match.find();
                //System.out.println(match.group(1));
                byte[] response = ("HTTP/1.1 101 Switching Protocols\\r\\n" +
                        "Connection: Upgrade\\r\\n\"\n" +
                        "Upgrade: websocket\\r\\n\"\n" +
                        "Sec-WebSocket-Accept: " +
                        Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1).getBytes(StandardCharsets.UTF_8) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B1").getBytes(StandardCharsets.UTF_8)))
                        + "\\r\\n\\r\\n").getBytes(StandardCharsets.UTF_8);
                System.out.println(response);
                out.write(response, 0, response.length);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
