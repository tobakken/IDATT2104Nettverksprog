package ntnu.idi.tobakken.asnmnt3;

import java.io.*;
import java.net.*;

public class Server{

    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        ServerSocket tjener = new ServerSocket(PORTNR);

        while (true) {
            Socket connection = null;
            try {
                System.out.println("Log for the server. Now we wait...");
                connection = tjener.accept();  // venter inntil noen tar kontakt
                /* Åpner strømmer for kommunikasjon med klientprogrammet */
                InputStreamReader leseforbindelse
                        = new InputStreamReader(connection.getInputStream());
                BufferedReader leseren = new BufferedReader(leseforbindelse);
                PrintWriter skriveren = new PrintWriter(connection.getOutputStream(), true);
                Thread t = new ClientHandler(connection, leseren, skriveren);
                t.start();
            }
            catch (Exception e) {
                connection.close();
                e.printStackTrace();
            }

        }
    }


}

class ClientHandler extends Thread {
    Socket connection;
    final BufferedReader reader;
    final PrintWriter writer;

    public ClientHandler(Socket connection, BufferedReader reader, PrintWriter writer) {
        this.connection = connection;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run(){
        super.run();
        writer.println("Hello, you have contact with the server!");
        String enLinje = "Lorem";
        String valgLinje = "Ipsum";
        int x = 0;
        int y = 0;

        while (enLinje != null) {  // forbindelsen på klientsiden er lukket
            try {
                writer.println("Type your first number:");
                enLinje = reader.readLine();  // mottar en linje med tekst
                x = Integer.parseInt(enLinje);

                writer.println("Type your second number:");
                enLinje = reader.readLine();
                y = Integer.parseInt(enLinje);

                writer.println("What do you want (choose 1 or 2 and press enter)" +
                        " (1) Addition" +
                        "  (2) Subtraction");
                valgLinje = reader.readLine();

                if (valgLinje.equals("1")){
                    writer.println("Result of addition: " + add(x, y) );  // sender svar til klienten
                } else if (valgLinje.equals("2")){
                    writer.println("result of subtraction: " + subtract(x, y) );
                } else {
                    writer.println("Something went wrong, feel free to try again");
                }

                writer.println("Type 'yes' to go again");

                enLinje = reader.readLine();
                System.out.println(enLinje);
                if (!enLinje.equals("yes")) enLinje = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.writer.close();
            this.reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int add(int x, int y){
        return x + y;
    }

    public static int subtract(int x, int y){
        return x - y;
    }
}
