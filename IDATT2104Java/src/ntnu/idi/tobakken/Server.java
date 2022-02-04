package ntnu.idi.tobakken;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Objects;

public class Server {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Log for the server. Now we wait...");
        Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt

        /* Åpner strømmer for kommunikasjon med klientprogrammet */
        InputStreamReader leseforbindelse
                = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */
        skriveren.println("Hello, you have contact with the server!");
        String enLinje = "Lorem";
        String valgLinje = "Ipsum";
        int x = 0;
        int y = 0;

        while (enLinje != null) {  // forbindelsen på klientsiden er lukket
            skriveren.println("Type your first number:");
            enLinje = leseren.readLine();  // mottar en linje med tekst
            x = Integer.parseInt(enLinje);

            skriveren.println("Type your second number:");
            enLinje = leseren.readLine();
            y = Integer.parseInt(enLinje);

            skriveren.println("What do you want (choose 1 or 2 and press enter)" +
                    " (1) Addition" +
                    "  (2) Subtraction");
            valgLinje = leseren.readLine();

            if (valgLinje.equals("1")){
                skriveren.println("Result of addition: " + add(x, y) );  // sender svar til klienten
            } else if (valgLinje.equals("2")){
                skriveren.println("result of subtraction: " + subtract(x, y) );
            } else {
                skriveren.println("Something went wrong, feel free to try again");
            }

            skriveren.println("Type 'yes' to go again");

            enLinje = leseren.readLine();
            System.out.println(enLinje);
            if (!enLinje.equals("yes")) enLinje = null;
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }

    public static int add(int x, int y){
        return x + y;
    }

    public static int subtract(int x, int y){
        return x - y;
    }
}

